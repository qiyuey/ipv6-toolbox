package top.qiyuey.ipv6

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ipv6",
    ) {
        App()
    }
}