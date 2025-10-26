package top.qiyuey.ipv6

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform