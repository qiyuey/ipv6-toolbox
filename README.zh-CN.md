# IPv6 工具箱 (IPv6 Toolbox)

<div align="center">

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-1.9.0-4285F4?style=flat&logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![License: Anti-996](https://img.shields.io/badge/License-Anti_996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

一个现代化的跨平台 IPv6 网络诊断与分析工具集

[功能特性](#-功能特性) • [平台支持](#-平台支持) • [快速开始](#-快速开始) • [使用指南](#-使用指南) • [开发](#-开发) • [许可证](#-许可证)

</div>

---

## 📖 简介

IPv6 工具箱是一个基于 **Kotlin Multiplatform** 和 **Compose Multiplatform** 构建的跨平台应用，为网络工程师、开发者和 IPv6 用户提供完整的 IPv6 网络工具集。

### 为什么选择 IPv6 工具箱？

- 🌐 **真正的跨平台** - 一套代码，支持 Android、iOS、Windows、Linux、macOS
- 🎨 **现代化设计** - 采用 Material 3 设计语言，支持深色/浅色模式
- 🔧 **专业工具** - IPv6 地址解析、子网计算、网络诊断一应俱全
- 🚀 **高性能** - 基于 Kotlin Coroutines，流畅响应
- 🔓 **完全开源** - MIT & Anti-996 双许可证

---

## ✨ 功能特性

### IPv6 地址工具

- ✅ **地址验证** - 支持完整格式、压缩格式、带前缀格式
- 🔄 **格式转换** - 自动展开/压缩 IPv6 地址
- 🏷️ **类型识别** - 识别 7 种 IPv6 地址类型
  - 回环地址 (::1)
  - 链路本地地址 (fe80::/10)
  - 唯一本地地址 (fc00::/7)
  - 全局单播地址 (2000::/3)
  - 组播地址 (ff00::/8)
  - 未指定地址 (::)
  - IPv4 映射地址 (::ffff:0:0/96)
- 🧮 **子网计算器** - CIDR 解析、网络地址计算、地址范围查询

### 网络诊断工具

- 🏓 **Ping6** - ICMPv6 Echo Request (Android/Desktop)，TCP 探测 (iOS)
- 🛤️ **Traceroute6** - IPv6 路径追踪 (Android/Desktop)
- 🔌 **TCP 连接测试** - 指定端口连接性检查（所有平台）
- 📊 **实时结果** - 流式显示诊断结果
- ⏱️ **性能统计** - RTT、丢包率、连接时间

---

## 🎯 平台支持

| 平台 | 状态 | IPv6 地址工具 | Ping6 | Traceroute6 | TCP 测试 |
|------|------|--------------|-------|-------------|---------|
| **Android 7.0+** | ✅ 完整支持 | ✅ | ✅ ICMP | ✅ | ✅ |
| **Windows 10+** | ✅ 完整支持 | ✅ | ✅ ICMP | ✅ | ✅ |
| **macOS 12+** | ✅ 完整支持 | ✅ | ✅ ICMP | ✅ | ✅ |
| **Linux** | ✅ 完整支持 | ✅ | ✅ ICMP | ✅ | ✅ |
| **iOS 14+** | ⚠️ 功能受限 | ✅ | ⚠️ TCP 探测* | ❌ | ✅ |

> \* iOS 由于沙箱限制，使用 TCP 连接探测代替 ICMP ping

### 系统要求

- **Android**: API Level 24+ (Android 7.0)
- **iOS**: iOS 14.0+
- **Desktop**: 
  - Windows 10/11
  - macOS 12.0+ (Monterey)
  - Linux (Ubuntu 20.04+, Fedora 35+, 或其他现代发行版)
  - Java 11+ (JVM 运行时)

---

## 🚀 快速开始

### 下载

#### 预编译版本

> ⚠️ 当前版本处于开发阶段 (v0.1.0-SNAPSHOT)，暂未提供预编译版本

- [ ] Android APK - [GitHub Releases](https://github.com/yuchuanbj/ipv6-toolbox/releases)
- [ ] Windows MSI - [GitHub Releases](https://github.com/yuchuanbj/ipv6-toolbox/releases)
- [ ] macOS DMG - [GitHub Releases](https://github.com/yuchuanbj/ipv6-toolbox/releases)
- [ ] Linux DEB/RPM - [GitHub Releases](https://github.com/yuchuanbj/ipv6-toolbox/releases)
- [ ] iOS IPA - 通过 TestFlight

#### 从源码构建

```bash
# 克隆仓库
git clone https://github.com/yuchuanbj/ipv6-toolbox.git
cd ipv6-toolbox

# 使用 Make（推荐）
make run              # 运行桌面应用
make build            # 构建所有平台
make test             # 运行测试
make android          # 构建 Android APK

# 或直接使用 Gradle
./gradlew :composeApp:run              # 桌面应用
./gradlew :composeApp:assembleDebug    # Android APK
./gradlew test                         # 测试

# iOS (需要在 macOS 上)
open iosApp/iosApp.xcodeproj
# 在 Xcode 中构建和运行
```

---

## 📱 使用指南

### IPv6 地址工具

1. **地址验证与解析**
   - 在"IPv6 工具"页面输入 IPv6 地址
   - 支持格式：`2001:db8::1`、`2001:0db8::1`、`2001:db8::1/64`
   - 自动显示展开/压缩格式和地址类型

2. **子网计算**
   - 输入 CIDR 格式：`2001:db8::/32`
   - 查看网络地址、地址范围、可用地址数量
   - 支持 /0 到 /128 的所有前缀长度

### 网络诊断

1. **Ping6 测试**
   - 进入"网络诊断" > "Ping6"
   - 输入目标 IPv6 地址（如 `2001:4860:4860::8888`）
   - 设置发送数量
   - 点击"开始 Ping"查看实时结果

2. **TCP 连接测试**
   - 进入"网络诊断" > "TCP 测试"
   - 输入 IPv6 地址和端口
   - 测试指定服务的连接性

### 平台特定说明

#### iOS 用户
- Ping 功能使用 TCP 探测（端口 80/443），非标准 ICMP
- 不支持 Traceroute 功能
- 所有其他功能完整可用

#### Desktop 用户
- 某些网络诊断功能可能需要管理员/root 权限
- Windows: 以管理员身份运行
- Linux/macOS: 使用 `sudo` 运行或配置 capabilities

---

## 🛠️ 开发

### 技术栈

- **语言**: Kotlin 2.2.20
- **UI 框架**: Compose Multiplatform 1.9.0
- **架构**: Clean Architecture (Domain/Data/Presentation)
- **异步**: Kotlin Coroutines 1.10.2
- **测试**: kotlin-test, MockK

### 项目结构

```
composeApp/src/
├── commonMain/          # 跨平台共享代码
│   ├── domain/         # 业务逻辑层
│   ├── data/           # 数据层
│   └── presentation/   # UI 层
├── androidMain/        # Android 特定实现
├── iosMain/            # iOS 特定实现
└── jvmMain/            # Desktop 特定实现
```

### 本地开发

```bash
# 运行测试
./gradlew test

# 代码检查
./gradlew detekt

# 运行 Desktop 版本
./gradlew :composeApp:run

# 构建所有平台
./gradlew build
```

### 贡献指南

欢迎贡献！请查看 [CONTRIBUTING.md](CONTRIBUTING.md) 了解详情。

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📋 路线图

- [x] **v0.1.0** (当前) - MVP 核心功能
  - IPv6 地址工具
  - 基础网络诊断
  - Material 3 UI
  
- [ ] **v0.2.0** - 增强工具集
  - DNS over HTTPS 查询
  - 网络信息查看
  - 历史记录与收藏
  
- [ ] **v0.3.0** - 高级诊断
  - 端口扫描
  - Whois 查询
  - MTU 探测
  
- [ ] **v1.0.0** - 正式版
  - 多语言支持
  - 性能优化
  - 应用商店发布

详见 [ROADMAP.md](ROADMAP.md)

---

## 🤝 致谢

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) - 强大的跨平台框架
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) - 声明式 UI 框架
- [Material Design 3](https://m3.material.io/) - 设计规范
- 所有贡献者和使用者

---

## 📄 许可证

本项目采用双许可证:

- **MIT License** - 允许商业和非商业使用
- **Anti-996 License** - 促进健康的工作生活平衡

详见 [LICENSE](LICENSE) 和 [LICENSE.Anti-996](LICENSE.Anti-996)

```
Copyright (c) 2025 qiyuey

本软件根据 MIT 许可证和反 996 许可证双重许可。
使用本软件即表示您同意遵守两个许可证的条款。
```

---

## 📞 联系方式

- **问题反馈**: [GitHub Issues](https://github.com/yuchuanbj/ipv6-toolbox/issues)
- **功能建议**: [GitHub Discussions](https://github.com/yuchuanbj/ipv6-toolbox/discussions)

---

## 🌟 支持项目

如果这个项目对你有帮助，请考虑：

- ⭐ 给项目点个 Star
- 🐛 报告 Bug 和提出建议
- 🔀 贡献代码
- 📢 分享给更多人

---

<div align="center">

**用 ❤️ 构建，为了更好的 IPv6 网络体验**

[主页](https://github.com/yuchuanbj/ipv6-toolbox) • [文档](docs/) • [更新日志](CHANGELOG.md)

</div>
