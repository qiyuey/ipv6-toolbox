package top.qiyuey.ipv6.domain.models

/**
 * Ping 结果
 */
data class PingResult(
    val sequence: Int,
    val host: String,
    val responseTime: Long?, // 毫秒，null 表示超时
    val ttl: Int?,
    val success: Boolean,
    val errorMessage: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Ping 统计信息
 */
data class PingStatistics(
    val packetsSent: Int,
    val packetsReceived: Int,
    val packetLoss: Double, // 百分比
    val minRtt: Long?,
    val avgRtt: Long?,
    val maxRtt: Long?,
    val stdDevRtt: Double?
)

/**
 * Traceroute 跳数结果
 */
data class HopResult(
    val hop: Int,
    val host: String?,
    val ip: String?,
    val rtt1: Long?, // 毫秒
    val rtt2: Long?,
    val rtt3: Long?,
    val timeout: Boolean,
    val errorMessage: String? = null
)

/**
 * TCP 连接测试结果
 */
data class TcpConnectionResult(
    val host: String,
    val port: Int,
    val success: Boolean,
    val connectionTime: Long?, // 毫秒
    val errorMessage: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * 网络诊断错误类型
 */
sealed class NetworkDiagnosticError {
    data class UnsupportedPlatform(val feature: String, val platform: String) : NetworkDiagnosticError()
    data class PermissionDenied(val permission: String) : NetworkDiagnosticError()
    data class InvalidAddress(val address: String) : NetworkDiagnosticError()
    data class Timeout(val message: String) : NetworkDiagnosticError()
    data class NetworkError(val message: String) : NetworkDiagnosticError()
    data class UnknownError(val message: String) : NetworkDiagnosticError()
}
