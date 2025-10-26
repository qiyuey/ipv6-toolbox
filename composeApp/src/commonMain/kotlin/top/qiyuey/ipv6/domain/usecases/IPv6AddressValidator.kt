package top.qiyuey.ipv6.domain.usecases

import top.qiyuey.ipv6.domain.models.IPv6Address
import top.qiyuey.ipv6.domain.models.IPv6AddressType

/**
 * IPv6 地址验证器
 */
class IPv6AddressValidator {

    /**
     * 验证 IPv6 地址格式
     */
    fun validate(address: String): Boolean {
        if (address.isBlank()) return false

        val cleaned = address.trim()

        // 检查是否包含 IPv4 映射格式 (::ffff:192.0.2.1)
        val ipv4MappedRegex = Regex("^::ffff:(?:\\d{1,3}\\.){3}\\d{1,3}$", RegexOption.IGNORE_CASE)
        if (ipv4MappedRegex.matches(cleaned)) {
            return validateIPv4Part(cleaned.substringAfter("::ffff:"))
        }

        // 分割地址和前缀
        val addr = if (cleaned.contains('/')) {
            val parts = cleaned.split('/')
            if (parts.size != 2) return false
            val prefixValue = parts[1].toIntOrNull() ?: return false
            if (prefixValue !in 0..128) return false
            parts[0]
        } else {
            cleaned
        }

        // 检查双冒号数量
        val doubleColonCount = addr.windowed(2).count { it == "::" }
        if (doubleColonCount > 1) return false

        // 分割组
        val groups = if (addr.contains("::")) {
            expandAddress(addr).split(':')
        } else {
            addr.split(':')
        }

        // IPv6 地址应该有 8 个组
        if (groups.size != 8) return false

        // 验证每个组
        return groups.all { group ->
            if (group.isEmpty()) return false
            if (group.length > 4) return false
            group.all { it in '0'..'9' || it in 'a'..'f' || it in 'A'..'F' }
        }
    }

    /**
     * 展开 IPv6 地址为完整格式
     */
    fun expand(address: String): String {
        if (!validate(address)) return address

        val (addr, prefix) = if (address.contains('/')) {
            address.split('/').let { it[0] to "/${it[1]}" }
        } else {
            address to ""
        }

        val expanded = expandAddress(addr)
        return expanded + prefix
    }

    /**
     * 压缩 IPv6 地址
     */
    fun compress(address: String): String {
        if (!validate(address)) return address

        val (addr, prefix) = if (address.contains('/')) {
            address.split('/').let { it[0] to "/${it[1]}" }
        } else {
            address to ""
        }

        val expanded = expandAddress(addr)
        val groups = expanded.split(':')

        // 移除前导零
        val withoutLeadingZeros = groups.map { it.trimStart('0').ifEmpty { "0" } }

        // 查找最长的连续零序列
        var maxZeroStart = -1
        var maxZeroLength = 0
        var currentZeroStart = -1
        var currentZeroLength = 0

        withoutLeadingZeros.forEachIndexed { index, group ->
            if (group == "0") {
                if (currentZeroStart == -1) {
                    currentZeroStart = index
                    currentZeroLength = 1
                } else {
                    currentZeroLength++
                }
            } else {
                if (currentZeroLength > maxZeroLength) {
                    maxZeroStart = currentZeroStart
                    maxZeroLength = currentZeroLength
                }
                currentZeroStart = -1
                currentZeroLength = 0
            }
        }

        // 检查最后一段
        if (currentZeroLength > maxZeroLength) {
            maxZeroStart = currentZeroStart
            maxZeroLength = currentZeroLength
        }

        // 如果有连续的零，用 :: 替换
        val compressed = if (maxZeroLength > 1) {
            val before = withoutLeadingZeros.subList(0, maxZeroStart)
            val after = withoutLeadingZeros.subList(maxZeroStart + maxZeroLength, withoutLeadingZeros.size)

            when {
                before.isEmpty() && after.isEmpty() -> "::"
                before.isEmpty() -> "::" + after.joinToString(":")
                after.isEmpty() -> before.joinToString(":") + "::"
                else -> before.joinToString(":") + "::" + after.joinToString(":")
            }
        } else {
            withoutLeadingZeros.joinToString(":")
        }

        return compressed + prefix
    }

    /**
     * 识别 IPv6 地址类型
     */
    fun getAddressType(address: String): IPv6AddressType {
        if (!validate(address)) return IPv6AddressType.UNKNOWN

        val addr = if (address.contains('/')) {
            address.split('/')[0]
        } else {
            address
        }

        val expanded = expandAddress(addr)
        val groups = expanded.split(':')

        // 未指定地址 ::
        if (groups.all { it == "0000" }) {
            return IPv6AddressType.UNSPECIFIED
        }

        // 回环地址 ::1
        if (groups.dropLast(1).all { it == "0000" } && groups.last() == "0001") {
            return IPv6AddressType.LOOPBACK
        }

        val firstGroup = groups[0].toInt(16)

        // 链路本地地址 fe80::/10
        if (firstGroup in 0xfe80..0xfebf) {
            return IPv6AddressType.LINK_LOCAL
        }

        // 唯一本地地址 fc00::/7
        if (firstGroup in 0xfc00..0xfdff) {
            return IPv6AddressType.UNIQUE_LOCAL
        }

        // 组播地址 ff00::/8
        if (firstGroup in 0xff00..0xffff) {
            return IPv6AddressType.MULTICAST
        }

        // 全局单播地址 2000::/3
        if (firstGroup in 0x2000..0x3fff) {
            return IPv6AddressType.GLOBAL_UNICAST
        }

        return IPv6AddressType.UNKNOWN
    }

    /**
     * 解析 IPv6 地址
     */
    fun parse(address: String): IPv6Address {
        val isValid = validate(address)
        val addr = if (address.contains('/')) address.split('/')[0] else address

        return IPv6Address(
            raw = address,
            expanded = if (isValid) expand(addr) else address,
            compressed = if (isValid) compress(addr) else address,
            type = if (isValid) getAddressType(addr) else IPv6AddressType.UNKNOWN,
            isValid = isValid
        )
    }

    /**
     * 内部方法：展开地址
     */
    private fun expandAddress(addr: String): String {
        if (!addr.contains("::")) {
            return addr.split(':').joinToString(":") {
                it.padStart(4, '0')
            }
        }

        val parts = addr.split("::")
        val left = if (parts[0].isEmpty()) emptyList() else parts[0].split(':')
        val right = if (parts.size > 1 && parts[1].isNotEmpty()) parts[1].split(':') else emptyList()

        val zerosNeeded = 8 - left.size - right.size
        val zeros = List(zerosNeeded) { "0000" }

        val allGroups = left + zeros + right
        return allGroups.joinToString(":") { it.padStart(4, '0') }
    }

    /**
     * 验证 IPv4 部分
     */
    private fun validateIPv4Part(ipv4: String): Boolean {
        val parts = ipv4.split('.')
        if (parts.size != 4) return false
        return parts.all {
            val num = it.toIntOrNull() ?: return false
            num in 0..255
        }
    }
}
