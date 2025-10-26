# 变更日志 (Changelog)

所有重要的项目变更都将记录在此文件中。

本项目遵循 [语义化版本](https://semver.org/lang/zh-CN/) 规范。

---

## [未发布]

### 待修复
- [ ] 修复编译错误
- [ ] 集成依赖注入框架 (Koin)
- [ ] 完善网络诊断功能的 UI 集成
- [ ] 添加错误处理和用户反馈

---

## [0.1.0-SNAPSHOT] - 2025-10-26

### 新增 ✨

#### IPv6 地址工具
- ✅ IPv6 地址格式验证（支持完整、压缩、带前缀格式）
- ✅ IPv6 地址自动展开/压缩转换
- ✅ IPv6 地址类型识别（7种类型）
  - 回环地址 (::1/128)
  - 链路本地地址 (fe80::/10)
  - 唯一本地地址 (fc00::/7)
  - 全局单播地址 (2000::/3)
  - 组播地址 (ff00::/8)
  - 未指定地址 (::/128)
  - IPv4 映射地址
- ✅ IPv6 子网计算器
  - CIDR 解析和验证
  - 网络地址计算
  - 地址范围计算（首尾地址）
  - 地址总数计算

#### 网络诊断工具
- ✅ Ping6 功能
  - Android: 完整 ICMP 支持
  - Desktop: 跨平台支持（Windows/Linux/macOS）
  - iOS: TCP 探测降级方案
- ✅ Traceroute6 功能
  - Android: 完整支持
  - Desktop: 跨平台支持
  - iOS: 标注"平台不支持"
- ✅ TCP 连接测试（所有平台）
  - 指定端口连接性检查
  - 连接时间测量
  - 超时检测

#### 架构与框架
- ✅ Clean Architecture 三层架构（Domain/Data/Presentation）
- ✅ expect/actual 跨平台抽象层
- ✅ 平台能力声明机制（PlatformCapabilities）
- ✅ 统一错误处理（NetworkDiagnosticError）
- ✅ Material 3 主题系统
- ✅ 深色/浅色模式支持
- ✅ 底部导航架构

#### UI 页面
- ✅ 首页（欢迎和功能介绍）
- ✅ IPv6 工具页面（地址验证和子网计算）
- ✅ 网络诊断页面（Ping6、TCP 测试、关于）

#### 测试
- ✅ IPv6AddressValidator 单元测试（覆盖率 > 80%）
- ✅ IPv6SubnetCalculator 单元测试（覆盖率 > 85%）

#### 平台支持
- ✅ Android 7.0+ (API 24+)
- ✅ iOS 14.0+（功能受限）
- ✅ Desktop (Windows/Linux/macOS, Java 11+)

#### 文档
- ✅ 用户文档 (README.md)
- ✅ 开发者文档 (AGENTS.md)
- ✅ 产品路线图 (ROADMAP.md)
- ✅ v0.1.0 实现总结 (docs/v0.1.0-implementation-summary.md)
- ✅ MIT & Anti-996 双许可证

### 技术栈 🛠️
- Kotlin 2.2.20
- Compose Multiplatform 1.9.0
- Kotlin Coroutines 1.10.2
- Material 3
- Gradle (Kotlin DSL)

### 已知问题 🐛
- ⚠️ 存在编译错误，需要调试
- ⚠️ 网络诊断功能 UI 层待集成依赖注入
- ⚠️ 缺少集成测试
- ⚠️ iOS 平台功能受限（无 ICMP/Traceroute）

### 平台功能矩阵 📊

| 功能 | Android | iOS | Desktop |
|------|---------|-----|---------|
| IPv6 地址处理 | ✅ 完整 | ✅ 完整 | ✅ 完整 |
| Ping6 (ICMP) | ✅ 完整 | ⚠️ TCP 探测 | ✅ 完整 |
| Traceroute6 | ✅ 完整 | ❌ 不支持 | ✅ 完整 |
| TCP 连接测试 | ✅ 完整 | ✅ 完整 | ✅ 完整 |

### 文件统计 📁
- 新增文件: 19 个
- 修改文件: 1 个 (App.kt)
- 测试文件: 2 个
- 文档文件: 5 个

### 完成度 📈
- 核心功能: **85%**
- 待完成: 编译调试、依赖注入、集成测试

---

## 图例说明

- ✅ 已完成
- ⚠️ 部分完成/有限制
- ❌ 不支持
- 🚧 进行中
- 📝 计划中

---

**注意**: 版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/) 规范:
- 主版本号 (Major): 不兼容的 API 修改
- 次版本号 (Minor): 向下兼容的功能性新增
- 修订号 (Patch): 向下兼容的问题修正

---

[未发布]: https://github.com/qiyuey/ipv6-toolbox/compare/v0.1.0...HEAD
[0.1.0-SNAPSHOT]: https://github.com/qiyuey/ipv6-toolbox/releases/tag/v0.1.0
