package top.qiyuey.ipv6.domain.repositories

import kotlinx.coroutines.flow.Flow
import top.qiyuey.ipv6.domain.models.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * 平台能力声明
 */
data class PlatformCapabilities(
    val supportsPing: Boolean,
    val supportsTraceroute: Boolean,
    val supportsTcpConnection: Boolean,
    val supportsIcmpPing: Boolean,  // 是否支持真正的 ICMP ping
    val platformName: String
)

/**
 * 网络诊断接口
 * 使用 expect/actual 模式实现跨平台
 */
interface NetworkDiagnostics {

    /**
     * 获取平台能力
     */
    fun getCapabilities(): PlatformCapabilities

    /**
     * Ping6 - ICMPv6 Echo Request
     * @param address IPv6 地址
     * @param count 发送数量
     * @param timeout 超时时间
     * @return Flow<Result<PingResult>>
     */
    suspend fun ping(
        address: String,
        count: Int = 4,
        timeout: Duration = 5.seconds
    ): Flow<Result<PingResult>>

    /**
     * Traceroute6 - 路径追踪
     * @param address IPv6 地址
     * @param maxHops 最大跳数
     * @return Flow<Result<HopResult>>
     */
    suspend fun traceroute(
        address: String,
        maxHops: Int = 30
    ): Flow<Result<HopResult>>

    /**
     * TCP 连接测试
     * @param address IPv6 地址
     * @param port 端口
     * @param timeout 超时时间
     * @return Result<TcpConnectionResult>
     */
    suspend fun tcpConnect(
        address: String,
        port: Int,
        timeout: Duration = 5.seconds
    ): Result<TcpConnectionResult>
}
