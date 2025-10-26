package top.qiyuey.ipv6.domain.usecases

import top.qiyuey.ipv6.domain.models.IPv6AddressType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IPv6AddressValidatorTest {

    private val validator = IPv6AddressValidator()

    @Test
    fun `valid IPv6 addresses should pass validation`() {
        val validAddresses = listOf(
            "2001:db8::1",
            "2001:0db8:0000:0000:0000:0000:0000:0001",
            "::1",
            "::",
            "fe80::1",
            "ff02::1",
            "2001:db8:85a3::8a2e:370:7334",
            "2001:db8:85a3:0:0:8a2e:370:7334"
        )

        validAddresses.forEach { address ->
            assertTrue(
                validator.validate(address),
                "Address $address should be valid"
            )
        }
    }

    @Test
    fun `invalid IPv6 addresses should fail validation`() {
        val invalidAddresses = listOf(
            "",
            "not-an-ip",
            "2001:db8::1::2",  // 双 :: 出现两次
            "2001:db8:gggg::1",  // 非法字符
            "2001:db8::1:2:3:4:5:6:7:8",  // 超过 8 组
            "2001:db8",  // 组数不足
            "12345::1"  // 组值超过 4 位
        )

        invalidAddresses.forEach { address ->
            assertFalse(
                validator.validate(address),
                "Address $address should be invalid"
            )
        }
    }

    @Test
    fun `expand should convert compressed addresses to full format`() {
        assertEquals(
            "2001:0db8:0000:0000:0000:0000:0000:0001",
            validator.expand("2001:db8::1")
        )

        assertEquals(
            "0000:0000:0000:0000:0000:0000:0000:0001",
            validator.expand("::1")
        )

        assertEquals(
            "0000:0000:0000:0000:0000:0000:0000:0000",
            validator.expand("::")
        )

        assertEquals(
            "fe80:0000:0000:0000:0000:0000:0000:0001",
            validator.expand("fe80::1")
        )
    }

    @Test
    fun `compress should convert full addresses to compressed format`() {
        assertEquals(
            "2001:db8::1",
            validator.compress("2001:0db8:0000:0000:0000:0000:0000:0001")
        )

        assertEquals(
            "::1",
            validator.compress("0000:0000:0000:0000:0000:0000:0000:0001")
        )

        assertEquals(
            "::",
            validator.compress("0000:0000:0000:0000:0000:0000:0000:0000")
        )

        assertEquals(
            "fe80::1",
            validator.compress("fe80:0000:0000:0000:0000:0000:0000:0001")
        )
    }

    @Test
    fun `getAddressType should correctly identify address types`() {
        assertEquals(
            IPv6AddressType.UNSPECIFIED,
            validator.getAddressType("::")
        )

        assertEquals(
            IPv6AddressType.LOOPBACK,
            validator.getAddressType("::1")
        )

        assertEquals(
            IPv6AddressType.LINK_LOCAL,
            validator.getAddressType("fe80::1")
        )

        assertEquals(
            IPv6AddressType.UNIQUE_LOCAL,
            validator.getAddressType("fc00::1")
        )

        assertEquals(
            IPv6AddressType.UNIQUE_LOCAL,
            validator.getAddressType("fd00::1")
        )

        assertEquals(
            IPv6AddressType.GLOBAL_UNICAST,
            validator.getAddressType("2001:db8::1")
        )

        assertEquals(
            IPv6AddressType.MULTICAST,
            validator.getAddressType("ff02::1")
        )
    }

    @Test
    fun `parse should return valid IPv6Address for valid addresses`() {
        val result = validator.parse("2001:db8::1")

        assertTrue(result.isValid)
        assertEquals("2001:db8::1", result.raw)
        assertEquals("2001:0db8:0000:0000:0000:0000:0000:0001", result.expanded)
        assertEquals("2001:db8::1", result.compressed)
        assertEquals(IPv6AddressType.GLOBAL_UNICAST, result.type)
    }

    @Test
    fun `parse should return invalid IPv6Address for invalid addresses`() {
        val result = validator.parse("invalid")

        assertFalse(result.isValid)
        assertEquals("invalid", result.raw)
        assertEquals(IPv6AddressType.UNKNOWN, result.type)
    }

    @Test
    fun `should handle addresses with prefix length`() {
        assertTrue(validator.validate("2001:db8::1/64"))
        assertTrue(validator.validate("fe80::1/10"))
        assertTrue(validator.validate("::/0"))
        assertTrue(validator.validate("::1/128"))

        assertFalse(validator.validate("2001:db8::1/129"))  // 前缀长度超过 128
        assertFalse(validator.validate("2001:db8::1/-1"))   // 负数前缀
    }

    @Test
    fun `expand and compress should preserve prefix length`() {
        assertEquals(
            "2001:0db8:0000:0000:0000:0000:0000:0001/64",
            validator.expand("2001:db8::1/64")
        )

        assertEquals(
            "2001:db8::1/64",
            validator.compress("2001:0db8:0000:0000:0000:0000:0000:0001/64")
        )
    }

    @Test
    fun `should handle IPv4-mapped IPv6 addresses`() {
        assertTrue(validator.validate("::ffff:192.0.2.1"))
        assertTrue(validator.validate("::FFFF:192.0.2.1"))
    }
}
