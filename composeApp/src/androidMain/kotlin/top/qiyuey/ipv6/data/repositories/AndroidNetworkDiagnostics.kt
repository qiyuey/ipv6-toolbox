package top.qiyuey.ipv6.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import top.qiyuey.ipv6.domain.models.*
import top.qiyuey.ipv6.domain.repositories.NetworkDiagnostics
import top.qiyuey.ipv6.domain.repositories.PlatformCapabilities
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.time.Duration

/**
 * Android 平台网络诊断实现
 */
class AndroidNetworkDiagnostics : NetworkDiagnostics {

    override fun getCapabilities(): PlatformCapabilities {
        return PlatformCapabilities(
            supportsPing = true,
            supportsTraceroute = true,
            supportsTcpConnection = true,
            supportsIcmpPing = true,
            platformName = "Android"
        )
    }

    override suspend fun ping(
        address: String,
        count: Int,
        timeout: Duration
    ): Flow<Result<PingResult>> = flow {
        try {
            val process = withContext(Dispatchers.IO) {
                Runtime.getRuntime().exec(
                    arrayOf("ping6", "-c", count.toString(), "-W", timeout.inWholeSeconds.toString(), address)
                )
            }

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var sequence = 0

            reader.useLines { lines ->
                lines.forEach { line ->
                    val result = parsePingLine(line, address, sequence)
                    if (result != null) {
                        emit(Result.success(result))
                        sequence++
                    }
                }
            }

            process.waitFor()

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun traceroute(
        address: String,
        maxHops: Int
    ): Flow<Result<HopResult>> = flow {
        try {
            val process = withContext(Dispatchers.IO) {
                Runtime.getRuntime().exec(
                    arrayOf("traceroute6", "-m", maxHops.toString(), address)
                )
            }

            val reader = BufferedReader(InputStreamReader(process.inputStream))

            reader.useLines { lines ->
                lines.forEach { line ->
                    val result = parseTracerouteLine(line)
                    if (result != null) {
                        emit(Result.success(result))
                    }
                }
            }

            process.waitFor()

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun tcpConnect(
        address: String,
        port: Int,
        timeout: Duration
    ): Result<TcpConnectionResult> = withContext(Dispatchers.IO) {
        try {
            val startTime = System.currentTimeMillis()
            val socket = Socket()

            socket.connect(
                InetSocketAddress(address, port),
                timeout.inWholeMilliseconds.toInt()
            )

            val connectionTime = System.currentTimeMillis() - startTime
            socket.close()

            Result.success(
                TcpConnectionResult(
                    host = address,
                    port = port,
                    success = true,
                    connectionTime = connectionTime
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

    private fun parsePingLine(line: String, host: String, sequence: Int): PingResult? {
        // 解析 ping 输出: 64 bytes from 2001:db8::1: icmp_seq=1 ttl=64 time=1.23 ms
        if (!line.contains("bytes from")) return null

        val timeRegex = Regex("time=([\\d.]+)\\s*ms")
        val ttlRegex = Regex("ttl=(\\d+)")
        val seqRegex = Regex("icmp_seq=(\\d+)")

        val timeMatch = timeRegex.find(line)
        val ttlMatch = ttlRegex.find(line)
        val seqMatch = seqRegex.find(line)

        val responseTime = timeMatch?.groupValues?.get(1)?.toDoubleOrNull()?.toLong()
        val ttl = ttlMatch?.groupValues?.get(1)?.toIntOrNull()
        val seq = seqMatch?.groupValues?.get(1)?.toIntOrNull() ?: sequence

        return PingResult(
            sequence = seq,
            host = host,
            responseTime = responseTime,
            ttl = ttl,
            success = responseTime != null
        )
    }

    private fun parseTracerouteLine(line: String): HopResult? {
        // 解析 traceroute 输出: 1  gateway (fe80::1)  1.234 ms  1.567 ms  1.890 ms
        val parts = line.trim().split(Regex("\\s+"))
        if (parts.isEmpty()) return null

        val hop = parts[0].toIntOrNull() ?: return null

        if (parts.size < 2) {
            return HopResult(
                hop = hop,
                host = null,
                ip = null,
                rtt1 = null,
                rtt2 = null,
                rtt3 = null,
                timeout = true
            )
        }

        val host = if (parts.size > 1 && !parts[1].startsWith("(")) parts[1] else null
        val ip = parts.find { it.startsWith("(") && it.endsWith(")") }?.removeSurrounding("(", ")")

        val times = parts.filter { it.endsWith("ms") }
            .mapNotNull { it.removeSuffix("ms").toDoubleOrNull()?.toLong() }

        return HopResult(
            hop = hop,
            host = host,
            ip = ip,
            rtt1 = times.getOrNull(0),
            rtt2 = times.getOrNull(1),
            rtt3 = times.getOrNull(2),
            timeout = times.isEmpty()
        )
    }
}
