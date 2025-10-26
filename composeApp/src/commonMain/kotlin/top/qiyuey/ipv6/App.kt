package top.qiyuey.ipv6

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import top.qiyuey.ipv6.presentation.Screen
import top.qiyuey.ipv6.presentation.bottomNavItems
import top.qiyuey.ipv6.presentation.screens.HomeScreen
import top.qiyuey.ipv6.presentation.screens.IPv6ToolsScreen
import top.qiyuey.ipv6.presentation.screens.NetworkDiagnosticsScreen
import top.qiyuey.ipv6.presentation.theme.IPv6ToolboxTheme

@Composable
@Preview
fun App() {
    IPv6ToolboxTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Text(item.icon, style = MaterialTheme.typography.titleLarge) },
                            label = { Text(item.label) },
                            selected = currentScreen == item.screen,
                            onClick = { currentScreen = item.screen }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (currentScreen) {
                    Screen.Home -> HomeScreen()
                    Screen.IPv6Tools -> IPv6ToolsScreen()
                    Screen.NetworkDiagnostics -> NetworkDiagnosticsScreen()
                    else -> HomeScreen()
                }
            }
        }
    }
}
