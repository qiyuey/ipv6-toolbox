package top.qiyuey.ipv6.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import platform.Foundation.*
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.dispatch_time
import top.qiyuey.ipv6.domain.models.*
import top.qiyuey.ipv6.domain.repositories.NetworkDiagnostics
import top.qiyuey.ipv6.domain.repositories.PlatformCapabilities
import kotlin.time.Duration

/**
 * iOS 平台网络诊断实现
 * 注意: iOS 不支持 ICMP,使用 TCP 连接探测作为降级方案
 */
class IosNetworkDiagnostics : NetworkDiagnostics {

    override fun getCapabilities(): PlatformCapabilities {
        return PlatformCapabilities(
            supportsPing = true,
            supportsTraceroute = false,  // iOS 不支持 traceroute
            supportsTcpConnection = true,
            supportsIcmpPing = false,  // 使用 TCP 探测
            platformName = "iOS"
        )
    }

    /**
     * iOS 平台使用 TCP 连接探测模拟 ping
     * 默认探测端口 80 和 443
     */
    override suspend fun ping(
        address: String,
        count: Int,
        timeout: Duration
    ): Flow<Result<PingResult>> = flow {
        val ports = listOf(80, 443)

        repeat(count) { sequence ->
            var success = false
            var minTime: Long? = null

            // 尝试连接常用端口
            for (port in ports) {
                val result = tcpConnect(address, port, timeout)
                result.getOrNull()?.let { tcpResult ->
                    if (tcpResult.success) {
                        success = true
                        minTime = tcpResult.connectionTime
                        break
                    }
                }
            }

            emit(
                Result.success(
                    PingResult(
                        sequence = sequence,
                        host = address,
                        responseTime = minTime,
                        ttl = null,
                        success = success,
                        errorMessage = if (!success) "TCP connection failed (ports 80, 443)" else null
                    )
                )
            )
        }
    }

    /**
     * iOS 不支持 traceroute
     */
    override suspend fun traceroute(
        address: String,
        maxHops: Int
    ): Flow<Result<HopResult>> = flow {
        emit(
            Result.failure(
                UnsupportedOperationException("Traceroute is not supported on iOS platform")
            )
        )
    }

    /**
     * TCP 连接测试
     */
    override suspend fun tcpConnect(
        address: String,
        port: Int,
        timeout: Duration
    ): Result<TcpConnectionResult> = withContext(Dispatchers.IO) {
        try {
            val startTime = NSDate().timeIntervalSince1970 * 1000

            // 创建 URL
            val url = NSURL.URLWithString("http://[$address]:$port")
                ?: return@withContext Result.success(
                    TcpConnectionResult(
                        host = address,
                        port = port,
                        success = false,
                        connectionTime = null,
                        errorMessage = "Invalid URL"
                    )
                )

            // 创建请求
            val request = NSMutableURLRequest.requestWithURL(url).apply {
                setTimeoutInterval(timeout.inWholeSeconds.toDouble())
                setHTTPMethod("HEAD")
            }

            var resultData: TcpConnectionResult? = null
            var completed = false

            // 发送请求
            val session = NSURLSession.sharedSession
            val task = session.dataTaskWithRequest(request) { data, response, error ->
                val connectionTime = (NSDate().timeIntervalSince1970 * 1000 - startTime).toLong()

                resultData = if (error == null) {
                    TcpConnectionResult(
                        host = address,
                        port = port,
                        success = true,
                        connectionTime = connectionTime
                    )
                } else {
                    TcpConnectionResult(
                        host = address,
                        port = port,
                        success = false,
                        connectionTime = null,
                        errorMessage = error.localizedDescription
                    )
                }
                completed = true
            }

            task.resume()

            // 等待完成
            val maxWaitTime = timeout.inWholeMilliseconds + 1000
            val checkInterval = 100L
            var waited = 0L

            while (!completed && waited < maxWaitTime) {
                NSThread.sleepForTimeInterval(checkInterval / 1000.0)
                waited += checkInterval
            }

            Result.success(
                resultData ?: TcpConnectionResult(
                    host = address,
                    port = port,
                    success = false,
                    connectionTime = null,
                    errorMessage = "Connection timeout"
                )
            )

        } catch (e: Exception) {
            Result.success(
                TcpConnectionResult(
                    host = address,
                    port = port,
                    success = false,
                    connectionTime = null,
                    errorMessage = e.message
                )
            )
        }
    }
}
