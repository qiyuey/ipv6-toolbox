# IPv6 工具箱 - AI Agent 项目信息

> 本文档为 AI 编码助手提供项目上下文，便于快速理解项目架构和开发规范

---

## 项目概览

### 基本信息
- **项目名称：** IPv6 工具箱 (IPv6 Toolbox)
- **项目定位：** 跨平台 IPv6 网络诊断与分析工具集
- **目标平台：** Android、iOS、Windows、Linux、macOS
- **技术栈：** Kotlin Multiplatform (KMP) + Compose Multiplatform
- **包名：** `top.qiyuey.ipv6`
- **当前版本：** 0.1.0-SNAPSHOT
- **开发状态：** MVP 核心功能已实现（85%），待编译调试和集成

---

## 技术架构

### 核心技术栈
```
Kotlin 2.2.20
├── Kotlin Multiplatform
├── Compose Multiplatform 1.9.0
├── Compose Compiler (KMP)
└── Kotlinx Coroutines 1.10.2
```

### 平台支持
- **Android：** minSdk 24, targetSdk 36, compileSdk 36
- **iOS：** iOS 14+
- **Desktop (JVM)：** Java 11+

### 依赖管理
- **构建工具：** Gradle (Kotlin DSL)
- **版本管理：** `gradle/libs.versions.toml`
- **主要依赖：**
  - `androidx.lifecycle:lifecycle-viewmodel-compose`
  - `androidx.lifecycle:lifecycle-runtime-compose`
  - `androidx.activity:activity-compose`
  - `kotlinx-coroutines-swing` (Desktop)

---

## 项目结构

```
IPv6 Toolbox/
├── composeApp/                 # 共享应用代码
│   ├── src/
│   │   ├── commonMain/        # 跨平台共享代码
│   │   │   └── kotlin/top/qiyuey/ipv6/
│   │   │       ├── App.kt                      # 主应用入口
│   │   │       ├── Platform.kt                 # 平台接口定义
│   │   │       ├── domain/                     # 业务逻辑层 ✅
│   │   │       │   ├── models/                 # 领域模型
│   │   │       │   │   ├── IPv6Address.kt
│   │   │       │   │   └── NetworkDiagnosticResult.kt
│   │   │       │   ├── usecases/               # 用例
│   │   │       │   │   ├── IPv6AddressValidator.kt
│   │   │       │   │   └── IPv6SubnetCalculator.kt
│   │   │       │   └── repositories/           # 仓库接口
│   │   │       │       └── NetworkDiagnostics.kt
│   │   │       └── presentation/               # UI 层 ✅
│   │   │           ├── screens/                # 页面组件
│   │   │           │   ├── HomeScreen.kt
│   │   │           │   ├── IPv6ToolsScreen.kt
│   │   │           │   └── NetworkDiagnosticsScreen.kt
│   │   │           ├── theme/                  # Material 3 主题
│   │   │           │   ├── Theme.kt
│   │   │           │   ├── Color.kt
│   │   │           │   └── Type.kt
│   │   │           └── Navigation.kt           # 导航定义
│   │   ├── androidMain/       # Android 特定代码
│   │   │   └── kotlin/top/qiyuey/ipv6/
│   │   │       ├── MainActivity.kt
│   │   │       ├── Platform.android.kt
│   │   │       └── data/repositories/          # 数据层实现 ✅
│   │   │           └── AndroidNetworkDiagnostics.kt
│   │   ├── iosMain/          # iOS 特定代码
│   │   │   └── kotlin/top/qiyuey/ipv6/
│   │   │       ├── MainViewController.kt
│   │   │       ├── Platform.ios.kt
│   │   │       └── data/repositories/          # 数据层实现 ✅
│   │   │           └── IosNetworkDiagnostics.kt
│   │   ├── jvmMain/          # Desktop (JVM) 特定代码
│   │   │   └── kotlin/top/qiyuey/ipv6/
│   │   │       ├── main.kt
│   │   │       ├── Platform.jvm.kt
│   │   │       └── data/repositories/          # 数据层实现 ✅
│   │   │           └── DesktopNetworkDiagnostics.kt
│   │   └── commonTest/       # 共享测试代码 ✅
│   │       └── kotlin/top/qiyuey/ipv6/domain/usecases/
│   │           ├── IPv6AddressValidatorTest.kt
│   │           └── IPv6SubnetCalculatorTest.kt
│   └── build.gradle.kts
├── iosApp/                    # iOS 原生入口
├── docs/                      # 项目文档
│   ├── v0.1.0-implementation-summary.md  # v0.1.0 实现总结 ✅
│   └── ROADMAP.md             # 产品路线图
├── gradle/
│   └── libs.versions.toml     # 依赖版本管理
├── build.gradle.kts           # 根构建脚本
├── settings.gradle.kts        # 项目设置
├── README.md                  # 用户文档（已更新）✅
├── AGENTS.md                  # 本文件
└── LICENSE                    # MIT & Anti-996 双许可证 ⏳
```

---

## 代码架构指南

### 模块组织原则
1. **commonMain：** 业务逻辑、UI 组件、数据模型、跨平台 API
2. **平台特定目录：** 仅包含平台相关的实现（网络、文件系统、权限等）
3. **expect/actual 模式：** 用于定义跨平台接口和平台实现

### 推荐架构模式
```
Clean Architecture + MVVM + MVI

├── presentation/          # UI 层
│   ├── screens/          # 页面组件
│   ├── components/       # 可复用 UI 组件
│   └── viewmodels/       # ViewModel
├── domain/               # 业务逻辑层
│   ├── models/          # 领域模型
│   ├── usecases/        # 用例
│   └── repositories/    # 仓库接口
└── data/                # 数据层
    ├── repositories/    # 仓库实现
    ├── sources/         # 数据源（local/remote）
    └── mappers/         # 数据映射
```

---

## 开发规范

### 命名规范
- **包名：** 小写，使用点分隔 (`top.qiyuey.ipv6.feature`)
- **类名：** 大驼峰 (`IPv6Validator`, `DnsQueryViewModel`)
- **函数/变量：** 小驼峰 (`validateAddress`, `dnsServer`)
- **常量：** 大写下划线 (`MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT`)
- **Composable 函数：** 大驼峰 (`IPv6AddressInput`, `PingResultCard`)

### 代码风格
- **缩进：** 4 空格
- **行长度：** 最大 120 字符
- **导入：** 按字母顺序，分组（标准库 → 第三方 → 项目内）
- **注释：** 使用 KDoc 格式，重要逻辑必须注释

### Compose 规范
```kotlin
@Composable
fun FeatureScreen(
    state: FeatureState,              // 状态参数
    onAction: (Action) -> Unit,       // 事件回调
    modifier: Modifier = Modifier     // Modifier 总是最后
) {
    // Composable 实现
}
```

### ViewModel 规范
```kotlin
class FeatureViewModel : ViewModel() {
    private val _state = MutableStateFlow(FeatureState())
    val state: StateFlow<FeatureState> = _state.asStateFlow()
    
    fun onAction(action: Action) {
        when (action) {
            // 处理用户操作
        }
    }
}
```

---

## 平台特定实现

### expect/actual 示例
```kotlin
// commonMain/Platform.kt
expect class Platform {
    fun getName(): String
}

// androidMain/Platform.android.kt
actual class Platform {
    actual fun getName(): String = "Android ${Build.VERSION.SDK_INT}"
}

// iosMain/Platform.ios.kt
actual class Platform {
    actual fun getName(): String = UIDevice.currentDevice.systemName()
}

// jvmMain/Platform.jvm.kt
actual class Platform {
    actual fun getName(): String = System.getProperty("os.name")
}
```

### 网络请求平台实现
- **Android/JVM：** 使用 `OkHttp` 或 `Ktor Client`
- **iOS：** 使用 `NSURLSession` 封装或 `Ktor Client (Darwin)`

### 权限处理
- **Android：** `AndroidManifest.xml` + Runtime Permissions
- **iOS：** `Info.plist` + Permission Request APIs
- **Desktop：** 通常无需特殊权限

---

## IPv6 功能实现指南

### 核心功能模块

#### 1. IPv6 地址处理
```kotlin
// 推荐库：java.net.Inet6Address (JVM)
// iOS: Darwin Network Framework
// 功能：验证、解析、转换、格式化

interface IPv6AddressValidator {
    fun validate(address: String): Boolean
    fun expand(address: String): String
    fun compress(address: String): String
    fun getAddressType(address: String): AddressType
}
```

#### 2. 网络诊断工具
```kotlin
// Ping6
interface PingService {
    suspend fun ping(
        address: String,
        count: Int = 4,
        timeout: Duration = 5.seconds
    ): Flow<PingResult>
}

// Traceroute
interface TracerouteService {
    suspend fun traceroute(
        address: String,
        maxHops: Int = 30
    ): Flow<HopResult>
}

// DNS Query
interface DnsService {
    suspend fun queryAAAA(domain: String): List<String>
    suspend fun queryPTR(address: String): String?
}
```

#### 3. 数据持久化
```kotlin
// 推荐使用 SQLDelight 或 Room (Android) + Core Data wrapper (iOS)
// 或使用 KMP 存储库：Multiplatform Settings

interface StorageRepository {
    suspend fun saveHistory(entry: HistoryEntry)
    suspend fun getHistory(limit: Int): List<HistoryEntry>
    suspend fun clearHistory()
}
```

---

## 测试策略

### 测试层级
1. **单元测试：** commonTest（跨平台共享逻辑）
2. **平台测试：** androidTest / iosTest / jvmTest
3. **UI 测试：** Compose UI Testing

### 测试工具
- `kotlin-test` - 通用断言
- `kotlinx-coroutines-test` - 协程测试
- `turbine` - Flow 测试
- `mockk` - Mock 框架

### 测试示例
```kotlin
class IPv6ValidatorTest {
    @Test
    fun `valid IPv6 address should pass validation`() {
        val validator = IPv6Validator()
        assertTrue(validator.validate("2001:db8::1"))
    }
}
```

---

## 构建与运行

### 构建命令
```bash
# Android
./gradlew :composeApp:assembleDebug

# Desktop (JVM)
./gradlew :composeApp:run

# iOS (需要在 macOS 上)
# 使用 Xcode 打开 iosApp/ 目录
```

### 常用 Gradle 任务
- `./gradlew clean` - 清理构建产物
- `./gradlew build` - 完整构建
- `./gradlew test` - 运行测试
- `./gradlew assembleDebug` - 构建 Debug 版本

---

## 性能优化建议

### Compose 性能
1. **状态提升：** 避免不必要的重组
2. **remember：** 缓存计算结果
3. **derivedStateOf：** 派生状态避免重复计算
4. **LazyColumn：** 长列表虚拟化
5. **key：** 为列表项提供稳定的 key

### 网络性能
1. **协程并发：** 使用 `async` 并行请求
2. **连接池：** 复用网络连接
3. **超时控制：** 设置合理的超时时间
4. **错误重试：** 实现指数退避

### 内存优化
1. **及时取消协程：** 使用 `viewModelScope`
2. **避免内存泄漏：** 注意 listener 生命周期
3. **图片加载：** 使用 Coil 或 Kamel (KMP)

---

## 常见问题与解决方案

### Q1: 如何添加新的平台特定功能？
```kotlin
// 1. 在 commonMain 定义 expect
expect fun platformSpecificFeature(): String

// 2. 在各平台实现 actual
// androidMain/iosMain/jvmMain
actual fun platformSpecificFeature(): String = "Implementation"
```

### Q2: 如何处理平台差异的 UI？
```kotlin
@Composable
fun AdaptiveLayout() {
    if (Platform.isAndroid) {
        // Android 特定 UI
    } else if (Platform.isIOS) {
        // iOS 特定 UI
    } else {
        // Desktop 特定 UI
    }
}
```

### Q3: 如何管理应用状态？
推荐使用 `StateFlow` + `ViewModel`：
```kotlin
class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
}
```

---

## 依赖添加指南

### 添加跨平台依赖
在 `composeApp/build.gradle.kts` 的 `commonMain` 中：
```kotlin
sourceSets {
    commonMain.dependencies {
        implementation("io.ktor:ktor-client-core:2.3.5")
    }
}
```

### 添加平台特定依赖
```kotlin
sourceSets {
    androidMain.dependencies {
        implementation("androidx.core:core-ktx:1.12.0")
    }
    iosMain.dependencies {
        // iOS 特定依赖
    }
}
```

---

## 资源文件管理

### 资源位置
```
composeApp/src/commonMain/composeResources/
├── drawable/      # 图片资源
├── values/        # 字符串、颜色等
└── font/          # 字体文件
```

### 使用资源
```kotlin
import ipv6.composeapp.generated.resources.Res
import ipv6.composeapp.generated.resources.app_name

Text(stringResource(Res.string.app_name))
Image(painterResource(Res.drawable.icon))
```

---

## 国际化 (i18n)

### 添加多语言支持
1. 在 `composeResources/values/` 创建语言目录
2. `values/strings.xml` - 默认（英语）
3. `values-zh/strings.xml` - 简体中文
4. `values-zh-rTW/strings.xml` - 繁体中文

---

## 发布清单

### 发布前检查
- [ ] 所有单元测试通过
- [ ] UI 测试覆盖主要流程
- [ ] 版本号更新
- [ ] 更新日志编写
- [ ] 混淆配置（Android）
- [ ] 签名配置
- [ ] 应用图标和启动页
- [ ] 隐私政策链接

---

## 有用的链接

- **Kotlin Multiplatform 文档：** https://kotlinlang.org/docs/multiplatform.html
- **Compose Multiplatform：** https://www.jetbrains.com/lp/compose-multiplatform/
- **Ktor 文档：** https://ktor.io/docs/
- **SQLDelight：** https://cashapp.github.io/sqldelight/
- **产品路线图：** [docs/prd/ROADMAP.md](ROADMAP.md)

---

## AI Agent 协作建议

### 开发新功能时
1. 先查看 `ROADMAP.md` 确认功能优先级
2. 在 `commonMain` 实现共享逻辑
3. 平台特定部分使用 `expect/actual`
4. 编写对应的单元测试
5. 更新相关文档

### 修复 Bug 时
1. 先写一个失败的测试用例
2. 修复代码使测试通过
3. 确保不影响其他功能
4. 添加必要的注释说明

### 代码审查重点
- 是否遵循 Clean Architecture
- 是否有平台耦合代码在 commonMain
- 是否有内存泄漏风险
- 是否有性能问题
- 代码可读性和可维护性

---

**文档版本：** 1.0  
**最后更新：** 2025-10-26  
**维护者：** IPv6 Toolbox Development Team

---

## 附录：快速参考

### Compose Multiplatform 平台判断
```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

@Composable
expect fun getPlatformName(): String

// 使用
val platform = getPlatformName()
```

### 协程最佳实践
```kotlin
viewModelScope.launch {
    try {
        val result = withContext(Dispatchers.IO) {
            // 耗时操作
        }
        _state.update { it.copy(result = result) }
    } catch (e: Exception) {
        _state.update { it.copy(error = e.message) }
    }
}
```

### 导航框架建议
- Compose 原生 Navigation（推荐）
- Decompose（更强大的 KMP 导航）
- Voyager（轻量级 KMP 导航）

---

**注意：** 本文档会随项目演进持续更新，请开发前确认文档版本。
