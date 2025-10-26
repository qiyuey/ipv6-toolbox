package top.qiyuey.ipv6.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import top.qiyuey.ipv6.domain.models.IPv6AddressType
import top.qiyuey.ipv6.domain.usecases.IPv6AddressValidator
import top.qiyuey.ipv6.domain.usecases.IPv6SubnetCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IPv6ToolsScreen() {
    val validator = remember { IPv6AddressValidator() }
    val subnetCalculator = remember { IPv6SubnetCalculator(validator) }

    var addressInput by remember { mutableStateOf("2001:db8::1") }
    var cidrInput by remember { mutableStateOf("2001:db8::/32") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("IPv6 工具") }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // IPv6 地址验证工具
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "IPv6 地址解析",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = addressInput,
                        onValueChange = { addressInput = it },
                        label = { Text("输入 IPv6 地址") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    val parsedAddress = remember(addressInput) {
                        validator.parse(addressInput)
                    }

                    if (parsedAddress.isValid) {
                        HorizontalDivider()

                        InfoRow("验证状态", "✅ 有效")
                        InfoRow("完整格式", parsedAddress.expanded)
                        InfoRow("压缩格式", parsedAddress.compressed)
                        InfoRow("地址类型", getAddressTypeText(parsedAddress.type))
                    } else {
                        Text(
                            "❌ 无效的 IPv6 地址",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // 子网计算器
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "子网计算器",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = cidrInput,
                        onValueChange = { cidrInput = it },
                        label = { Text("输入 CIDR (例: 2001:db8::/32)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    val subnetResult = remember(cidrInput) {
                        subnetCalculator.calculate(cidrInput)
                    }

                    subnetResult.fold(
                        onSuccess = { subnet ->
                            HorizontalDivider()

                            InfoRow("网络地址", subnet.networkAddress)
                            InfoRow("前缀长度", "/${subnet.prefixLength}")
                            InfoRow("CIDR", subnet.cidr)
                            InfoRow("第一个地址", subnet.firstAddress)
                            InfoRow("最后一个地址", subnet.lastAddress)
                            InfoRow("地址总数", subnet.totalAddresses)
                        },
                        onFailure = { error ->
                            Text(
                                "❌ ${error.message}",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    )
                }
            }

            // 使用说明
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
                        "💡 使用提示",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "• 支持完整和压缩的 IPv6 地址格式\n" +
                                "• 支持带前缀的地址 (例: 2001:db8::1/64)\n" +
                                "• 自动识别地址类型\n" +
                                "• 子网计算支持 0-128 位前缀长度",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun getAddressTypeText(type: IPv6AddressType): String {
    return when (type) {
        IPv6AddressType.UNSPECIFIED -> "未指定地址 (::/128)"
        IPv6AddressType.LOOPBACK -> "回环地址 (::1/128)"
        IPv6AddressType.LINK_LOCAL -> "链路本地地址 (fe80::/10)"
        IPv6AddressType.UNIQUE_LOCAL -> "唯一本地地址 (fc00::/7)"
        IPv6AddressType.GLOBAL_UNICAST -> "全局单播地址 (2000::/3)"
        IPv6AddressType.MULTICAST -> "组播地址 (ff00::/8)"
        IPv6AddressType.UNKNOWN -> "未知类型"
    }
}
