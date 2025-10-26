package top.qiyuey.ipv6.domain.models

/**
 * IPv6 地址类型
 */
enum class IPv6AddressType {
    /** 未指定地址 ::/128 */
    UNSPECIFIED,

    /** 回环地址 ::1/128 */
    LOOPBACK,

    /** 链路本地地址 fe80::/10 */
    LINK_LOCAL,

    /** 唯一本地地址 fc00::/7 */
    UNIQUE_LOCAL,

    /** 全局单播地址 2000::/3 */
    GLOBAL_UNICAST,

    /** 组播地址 ff00::/8 */
    MULTICAST,

    /** 未知类型 */
    UNKNOWN
}

/**
 * IPv6 地址数据模型
 */
data class IPv6Address(
    /** 原始地址字符串 */
    val raw: String,
    /** 展开的完整地址 */
    val expanded: String,
    /** 压缩的地址 */
    val compressed: String,
    /** 地址类型 */
    val type: IPv6AddressType,
    /** 是否有效 */
    val isValid: Boolean
)

/**
 * IPv6 子网信息
 */
data class IPv6Subnet(
    /** 网络地址 */
    val networkAddress: String,
    /** 前缀长度 */
    val prefixLength: Int,
    /** CIDR 表示 */
    val cidr: String,
    /** 第一个可用地址 */
    val firstAddress: String,
    /** 最后一个可用地址 */
    val lastAddress: String,
    /** 地址总数 (如果可计算) */
    val totalAddresses: String
)
