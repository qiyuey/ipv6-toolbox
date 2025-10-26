package top.qiyuey.ipv6.domain.usecases

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IPv6SubnetCalculatorTest {

    private val calculator = IPv6SubnetCalculator()

    @Test
    fun `calculate should return valid subnet for valid CIDR`() {
        val result = calculator.calculate("2001:db8::/32")

        assertTrue(result.isSuccess)
        result.onSuccess { subnet ->
            assertEquals("2001:db8::", subnet.networkAddress)
            assertEquals(32, subnet.prefixLength)
            assertEquals("2001:db8::/32", subnet.cidr)
        }
    }

    @Test
    fun `calculate should fail for invalid CIDR format`() {
        val result1 = calculator.calculate("2001:db8::")  // 缺少前缀
        assertTrue(result1.isFailure)

        val result2 = calculator.calculate("2001:db8::/")  // 缺少前缀值
        assertTrue(result2.isFailure)

        val result3 = calculator.calculate("invalid/32")  // 无效地址
        assertTrue(result3.isFailure)
    }

    @Test
    fun `calculate should fail for invalid prefix length`() {
        val result1 = calculator.calculate("2001:db8::/-1")
        assertTrue(result1.isFailure)

        val result2 = calculator.calculate("2001:db8::/129")
        assertTrue(result2.isFailure)

        val result3 = calculator.calculate("2001:db8::/abc")
        assertTrue(result3.isFailure)
    }

    @Test
    fun `calculate should correctly compute network address`() {
        val result1 = calculator.calculate("2001:db8:1234:5678::/64")
        result1.onSuccess { subnet ->
            assertEquals("2001:db8:1234:5678::", subnet.networkAddress)
        }

        val result2 = calculator.calculate("2001:db8:1234:5678:9abc:def0:1234:5678/64")
        result2.onSuccess { subnet ->
            // 应该掩码到 /64
            assertEquals("2001:db8:1234:5678::", subnet.networkAddress)
        }
    }

    @Test
    fun `calculate should correctly compute first and last address`() {
        val result = calculator.calculate("2001:db8::/32")

        result.onSuccess { subnet ->
            assertEquals("2001:db8::", subnet.firstAddress)
            assertTrue(subnet.lastAddress.startsWith("2001:db8:"))
        }
    }

    @Test
    fun `calculate should handle /128 prefix (single address)`() {
        val result = calculator.calculate("2001:db8::1/128")

        result.onSuccess { subnet ->
            assertEquals("2001:db8::1", subnet.networkAddress)
            assertEquals("2001:db8::1", subnet.firstAddress)
            assertEquals("2001:db8::1", subnet.lastAddress)
            assertEquals("1", subnet.totalAddresses)
        }
    }

    @Test
    fun `calculate should handle /0 prefix (entire IPv6 space)`() {
        val result = calculator.calculate("::/0")

        result.onSuccess { subnet ->
            assertEquals("::", subnet.networkAddress)
            assertEquals(0, subnet.prefixLength)
            assertTrue(subnet.totalAddresses.contains("2^128"))
        }
    }

    @Test
    fun `calculate should handle /64 prefix (standard subnet)`() {
        val result = calculator.calculate("2001:db8:1234:5678::/64")

        result.onSuccess { subnet ->
            assertEquals(64, subnet.prefixLength)
            // /64 有 2^64 个地址
            assertTrue(subnet.totalAddresses.contains("64") || subnet.totalAddresses.length > 10)
        }
    }

    @Test
    fun `validateCIDR should return true for valid CIDR`() {
        assertTrue(calculator.validateCIDR("2001:db8::/32"))
        assertTrue(calculator.validateCIDR("fe80::/10"))
        assertTrue(calculator.validateCIDR("::/0"))
        assertTrue(calculator.validateCIDR("::1/128"))
    }

    @Test
    fun `validateCIDR should return false for invalid CIDR`() {
        assertFalse(calculator.validateCIDR("2001:db8::"))  // 缺少前缀
        assertFalse(calculator.validateCIDR("invalid/32"))  // 无效地址
        assertFalse(calculator.validateCIDR("2001:db8::/129"))  // 前缀超出范围
        assertFalse(calculator.validateCIDR("2001:db8::/-1"))  // 负数前缀
    }

    @Test
    fun `should handle common IPv6 subnets`() {
        val testCases = mapOf(
            "2001:db8::/32" to 32,
            "fe80::/10" to 10,
            "fc00::/7" to 7,
            "ff00::/8" to 8
        )

        testCases.forEach { (cidr, expectedPrefix) ->
            val result = calculator.calculate(cidr)
            assertTrue(result.isSuccess, "Failed to calculate $cidr")
            result.onSuccess { subnet ->
                assertEquals(expectedPrefix, subnet.prefixLength)
            }
        }
    }
}
