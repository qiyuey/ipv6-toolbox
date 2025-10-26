package top.qiyuey.ipv6.domain.usecases

import top.qiyuey.ipv6.domain.models.IPv6Subnet
import kotlin.math.pow

/**
 * IPv6 子网计算器
 */
class IPv6SubnetCalculator(
    private val validator: IPv6AddressValidator = IPv6AddressValidator()
) {

    /**
     * 计算子网信息
     */
    fun calculate(cidr: String): Result<IPv6Subnet> {
        if (!cidr.contains('/')) {
            return Result.failure(IllegalArgumentException("Invalid CIDR format. Expected format: address/prefix"))
        }

        val parts = cidr.split('/')
        if (parts.size != 2) {
            return Result.failure(IllegalArgumentException("Invalid CIDR format"))
        }

        val address = parts[0]
        val prefix = parts[1].toIntOrNull() ?: return Result.failure(
            IllegalArgumentException("Invalid prefix length")
        )

        if (prefix !in 0..128) {
            return Result.failure(IllegalArgumentException("Prefix length must be between 0 and 128"))
        }

        if (!validator.validate(address)) {
            return Result.failure(IllegalArgumentException("Invalid IPv6 address"))
        }

        val expanded = validator.expand(address)
        val networkAddress = calculateNetworkAddress(expanded, prefix)
        val firstAddress = calculateFirstAddress(networkAddress, prefix)
        val lastAddress = calculateLastAddress(networkAddress, prefix)
        val totalAddresses = calculateTotalAddresses(prefix)

        return Result.success(
            IPv6Subnet(
                networkAddress = validator.compress(networkAddress),
                prefixLength = prefix,
                cidr = "${validator.compress(networkAddress)}/$prefix",
                firstAddress = validator.compress(firstAddress),
                lastAddress = validator.compress(lastAddress),
                totalAddresses = totalAddresses
            )
        )
    }

    /**
     * 验证 CIDR 格式
     */
    fun validateCIDR(cidr: String): Boolean {
        if (!cidr.contains('/')) return false
        val parts = cidr.split('/')
        if (parts.size != 2) return false

        val prefix = parts[1].toIntOrNull() ?: return false
        if (prefix !in 0..128) return false

        return validator.validate(parts[0])
    }

    /**
     * 计算网络地址
     */
    private fun calculateNetworkAddress(expanded: String, prefix: Int): String {
        val groups = expanded.split(':').map { it.toInt(16) }
        val bitsInGroup = 16

        val networkGroups = groups.mapIndexed { index, group ->
            val groupStartBit = index * bitsInGroup
            val groupEndBit = groupStartBit + bitsInGroup

            when {
                groupEndBit <= prefix -> group
                groupStartBit >= prefix -> 0
                else -> {
                    val bitsToKeep = prefix - groupStartBit
                    val mask = (0xFFFF shl (bitsInGroup - bitsToKeep)) and 0xFFFF
                    group and mask
                }
            }
        }

        return networkGroups.joinToString(":") { it.toString(16).padStart(4, '0') }
    }

    /**
     * 计算第一个地址
     */
    private fun calculateFirstAddress(networkAddress: String, prefix: Int): String {
        return networkAddress
    }

    /**
     * 计算最后一个地址
     */
    private fun calculateLastAddress(networkAddress: String, prefix: Int): String {
        val groups = networkAddress.split(':').map { it.toInt(16) }.toMutableList()
        val bitsInGroup = 16

        for (index in groups.indices) {
            val groupStartBit = index * bitsInGroup
            val groupEndBit = groupStartBit + bitsInGroup

            when {
                groupEndBit <= prefix -> continue
                groupStartBit >= prefix -> groups[index] = 0xFFFF
                else -> {
                    val bitsToSet = groupEndBit - prefix
                    val mask = (1 shl bitsToSet) - 1
                    groups[index] = groups[index] or mask
                }
            }
        }

        return groups.joinToString(":") { it.toString(16).padStart(4, '0') }
    }

    /**
     * 计算地址总数
     */
    private fun calculateTotalAddresses(prefix: Int): String {
        val hostBits = 128 - prefix

        return when {
            hostBits == 0 -> "1"
            hostBits <= 63 -> {
                val count = 2.0.pow(hostBits).toLong()
                formatLargeNumber(count)
            }

            else -> "2^$hostBits (≈ ${formatLargeNumber(2.0.pow(63).toLong())}... x 2^${hostBits - 63})"
        }
    }

    /**
     * 格式化大数字
     */
    private fun formatLargeNumber(num: Long): String {
        return when {
            num < 1_000 -> num.toString()
            num < 1_000_000 -> "${num / 1_000}K"
            num < 1_000_000_000 -> "${num / 1_000_000}M"
            num < 1_000_000_000_000 -> "${num / 1_000_000_000}B"
            else -> "${num / 1_000_000_000_000}T"
        }
    }
}
