package top.qiyuey.ipv6.presentation

/**
 * 导航路由
 */
sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "首页")
    data object IPv6Tools : Screen("ipv6_tools", "IPv6 工具")
    data object NetworkDiagnostics : Screen("network_diagnostics", "网络诊断")
    data object Settings : Screen("settings", "设置")
}

/**
 * 底部导航项
 */
data class BottomNavItem(
    val screen: Screen,
    val icon: String,  // 使用字符串表示图标,实际可以用 ImageVector
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "🏠", "首页"),
    BottomNavItem(Screen.IPv6Tools, "🔧", "IPv6 工具"),
    BottomNavItem(Screen.NetworkDiagnostics, "📡", "网络诊断")
)
