package top.qiyuey.ipv6.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.qiyuey.ipv6.domain.models.PingResult
import top.qiyuey.ipv6.domain.repositories.NetworkDiagnostics
import top.qiyuey.ipv6.getPlatform

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkDiagnosticsScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ping6", "TCP 测试", "关于")

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("网络诊断") }
        )

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> PingTab()
            1 -> TcpTestTab()
            2 -> AboutTab()
        }
    }
}

@Composable
private fun PingTab() {
    var address by remember { mutableStateOf("2001:4860:4860::8888") }
    var count by remember { mutableStateOf("4") }
    var isRunning by remember { mutableStateOf(false) }
    var results by remember { mutableStateOf<List<PingResult>>(emptyList()) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Ping6 配置",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("目标地址") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isRunning
                )

                OutlinedTextField(
                    value = count,
                    onValueChange = { if (it.all { c -> c.isDigit() }) count = it },
                    label = { Text("数量") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isRunning
                )

                Button(
                    onClick = {
                        scope.launch {
                            isRunning = true
                            results = emptyList()
                            // TODO: 实际调用网络诊断
                            // 这里需要通过依赖注入获取平台实现
                            isRunning = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isRunning && address.isNotBlank()
                ) {
                    Text(if (isRunning) "运行中..." else "开始 Ping")
                }
            }
        }

        if (results.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Ping 结果",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    results.forEach { result ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "seq=${result.sequence}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                if (result.success) {
                                    "✅ ${result.responseTime}ms"
                                } else {
                                    "❌ 超时"
                                },
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        // 平台说明
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "⚠️ 平台限制",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    getPlatformLimitations(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun TcpTestTab() {
    var address by remember { mutableStateOf("2001:4860:4860::8888") }
    var port by remember { mutableStateOf("80") }
    var isRunning by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "TCP 连接测试",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("目标地址") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isRunning
                )

                OutlinedTextField(
                    value = port,
                    onValueChange = { if (it.all { c -> c.isDigit() }) port = it },
                    label = { Text("端口") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isRunning
                )

                Button(
                    onClick = {
                        scope.launch {
                            isRunning = true
                            result = null
                            // TODO: 实际调用 TCP 连接测试
                            isRunning = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isRunning && address.isNotBlank() && port.isNotBlank()
                ) {
                    Text(if (isRunning) "测试中..." else "开始测试")
                }
            }
        }

        result?.let { resultText ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "测试结果",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        resultText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "平台信息",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                InfoRow("平台", getPlatform().name)
                InfoRow("版本", "0.1.0-SNAPSHOT")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "功能支持",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "• Ping6: ${if (supportsIcmpPing()) "✅ ICMP" else "⚠️ TCP 探测"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "• Traceroute6: ${if (supportsTraceroute()) "✅ 支持" else "❌ 不支持"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "• TCP 连接测试: ✅ 支持",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun getPlatformLimitations(): String {
    val platform = getPlatform().name
    return when {
        platform.contains("iOS", ignoreCase = true) ->
            "iOS 平台由于沙箱限制，使用 TCP 连接探测代替 ICMP ping，不支持 traceroute。"

        platform.contains("Android", ignoreCase = true) ->
            "Android 平台完整支持所有网络诊断功能。"

        else ->
            "Desktop 平台完整支持所有网络诊断功能。"
    }
}

private fun supportsIcmpPing(): Boolean {
    val platform = getPlatform().name
    return !platform.contains("iOS", ignoreCase = true)
}

private fun supportsTraceroute(): Boolean {
    val platform = getPlatform().name
    return !platform.contains("iOS", ignoreCase = true)
}
