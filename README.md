# IPv6 Toolbox

<div align="center">

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-1.9.0-4285F4?style=flat&logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![License: Anti-996](https://img.shields.io/badge/License-Anti_996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

A modern cross-platform IPv6 network diagnostic and analysis toolkit

[Features](#-features) • [Platform Support](#-platform-support) • [Quick Start](#-quick-start) • [Usage Guide](#-usage-guide) • [Development](#-development) • [License](#-license)

**[简体中文](README.zh-CN.md)** | **English**

</div>

---

## 📖 Introduction

IPv6 Toolbox is a cross-platform application built on **Kotlin Multiplatform** and **Compose Multiplatform**, providing a comprehensive set of IPv6 network tools for network engineers, developers, and IPv6 users.

### Why IPv6 Toolbox?

- 🌐 **True Cross-Platform** - One codebase, supports Android, iOS, Windows, Linux, macOS
- 🎨 **Modern Design** - Material 3 design language with dark/light theme support
- 🔧 **Professional Tools** - Complete IPv6 address parsing, subnet calculation, and network diagnostics
- 🚀 **High Performance** - Built on Kotlin Coroutines for smooth responsiveness
- 🔓 **Fully Open Source** - Dual-licensed under MIT & Anti-996

---

## ✨ Features

### IPv6 Address Tools

- ✅ **Address Validation** - Supports full format, compressed format, and prefix notation
- 🔄 **Format Conversion** - Auto expand/compress IPv6 addresses
- 🏷️ **Type Recognition** - Identifies 7 types of IPv6 addresses
  - Loopback (::1)
  - Link-Local (fe80::/10)
  - Unique Local (fc00::/7)
  - Global Unicast (2000::/3)
  - Multicast (ff00::/8)
  - Unspecified (::)
  - IPv4-mapped (::ffff:0:0/96)
- 🧮 **Subnet Calculator** - CIDR parsing, network address calculation, address range queries

### Network Diagnostic Tools

- 🏓 **Ping6** - ICMPv6 Echo Request (Android/Desktop), TCP probe (iOS)
- 🛤️ **Traceroute6** - IPv6 path tracing (Android/Desktop)
- 🔌 **TCP Connection Test** - Port connectivity check (all platforms)
- 📊 **Real-time Results** - Streaming diagnostic results
- ⏱️ **Performance Stats** - RTT, packet loss, connection time

---

## 🎯 Platform Support

| Platform | Status | IPv6 Tools | Ping6 | Traceroute6 | TCP Test |
|----------|--------|------------|-------|-------------|----------|
| **Android 7.0+** | ✅ Full Support | ✅ | ✅ ICMP | ✅ | ✅ |
| **Windows 10+** | ✅ Full Support | ✅ | ✅ ICMP | ✅ | ✅ |
| **macOS 12+** | ✅ Full Support | ✅ | ✅ ICMP | ✅ | ✅ |
| **Linux** | ✅ Full Support | ✅ | ✅ ICMP | ✅ | ✅ |
| **iOS 14+** | ⚠️ Limited | ✅ | ⚠️ TCP probe* | ❌ | ✅ |

> \* iOS uses TCP connection probing instead of ICMP ping due to sandbox restrictions

### System Requirements

- **Android**: API Level 24+ (Android 7.0)
- **iOS**: iOS 14.0+
- **Desktop**: 
  - Windows 10/11
  - macOS 12.0+ (Monterey)
  - Linux (Ubuntu 20.04+, Fedora 35+, or other modern distributions)
  - Java 11+ (JVM runtime)

---

## 🚀 Quick Start

### Download

#### Pre-built Binaries

> ⚠️ Current version is in development (v0.1.0-SNAPSHOT), pre-built binaries not yet available

- [ ] Android APK - [GitHub Releases](https://github.com/qiyuey/ipv6-toolbox/releases)
- [ ] Windows MSI - [GitHub Releases](https://github.com/qiyuey/ipv6-toolbox/releases)
- [ ] macOS DMG - [GitHub Releases](https://github.com/qiyuey/ipv6-toolbox/releases)
- [ ] Linux DEB/RPM - [GitHub Releases](https://github.com/qiyuey/ipv6-toolbox/releases)
- [ ] iOS IPA - via TestFlight

#### Build from Source

```bash
# Clone repository
git clone https://github.com/qiyuey/ipv6-toolbox.git
cd ipv6-toolbox

# Using Make (recommended)
make desktop          # Run desktop app
make android          # Build Android APK
make ios              # Build iOS app (macOS only)
make build            # Build all platforms
make test             # Run tests

# Or using Gradle directly
./gradlew :composeApp:run              # Desktop
./gradlew :composeApp:assembleDebug    # Android APK
./gradlew test                         # Tests

# iOS (requires macOS)
open iosApp/iosApp.xcodeproj
# Build and run in Xcode
```

---

## 📱 Usage Guide

### IPv6 Address Tools

1. **Address Validation & Parsing**
   - Enter IPv6 address in "IPv6 Tools" page
   - Supported formats: `2001:db8::1`, `2001:0db8::1`, `2001:db8::1/64`
   - Auto-display expanded/compressed format and address type

2. **Subnet Calculation**
   - Enter CIDR format: `2001:db8::/32`
   - View network address, address range, available address count
   - Supports all prefix lengths from /0 to /128

### Network Diagnostics

1. **Ping6 Test**
   - Go to "Network Diagnostics" > "Ping6"
   - Enter target IPv6 address (e.g., `2001:4860:4860::8888`)
   - Set packet count
   - Click "Start Ping" to view real-time results

2. **TCP Connection Test**
   - Go to "Network Diagnostics" > "TCP Test"
   - Enter IPv6 address and port
   - Test connectivity to specified service

### Platform-Specific Notes

#### iOS Users
- Ping uses TCP probing (ports 80/443), not standard ICMP
- Traceroute not supported
- All other features fully available

#### Desktop Users
- Some diagnostic features may require administrator/root privileges
- Windows: Run as administrator
- Linux/macOS: Use `sudo` or configure capabilities

---

## 🛠️ Development

### Tech Stack

- **Language**: Kotlin 2.2.20
- **UI Framework**: Compose Multiplatform 1.9.0
- **Architecture**: Clean Architecture (Domain/Data/Presentation)
- **Async**: Kotlin Coroutines 1.10.2
- **Testing**: kotlin-test, MockK

### Project Structure

```
composeApp/src/
├── commonMain/          # Cross-platform shared code
│   ├── domain/         # Business logic layer
│   ├── data/           # Data layer
│   └── presentation/   # UI layer
├── androidMain/        # Android-specific implementation
├── iosMain/            # iOS-specific implementation
└── jvmMain/            # Desktop-specific implementation
```

### Local Development

```bash
# Run tests
./gradlew test

# Code check
./gradlew detekt

# Run Desktop version
./gradlew :composeApp:run

# Build all platforms
./gradlew build
```

### Contributing

Contributions welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details.

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

---

## 📋 Roadmap

- [x] **v0.1.0** (Current) - MVP Core Features
  - IPv6 address tools
  - Basic network diagnostics
  - Material 3 UI
  
- [ ] **v0.2.0** - Enhanced Toolset
  - DNS over HTTPS queries
  - Network information viewer
  - History & favorites
  
- [ ] **v0.3.0** - Advanced Diagnostics
  - Port scanner
  - Whois lookup
  - MTU detection
  
- [ ] **v1.0.0** - Official Release
  - Multi-language support
  - Performance optimization
  - App store release

See [ROADMAP.md](ROADMAP.md) for details

---

## 🤝 Acknowledgments

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) - Powerful cross-platform framework
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) - Declarative UI framework
- [Material Design 3](https://m3.material.io/) - Design system
- All contributors and users

---

## 📄 License

This project is dual-licensed under:

- **MIT License** - Allows commercial and non-commercial use
- **Anti-996 License** - Promotes healthy work-life balance

See [LICENSE](LICENSE) and [LICENSE.Anti-996](LICENSE.Anti-996) for details

```
Copyright (c) 2025 qiyuey

This software is dual-licensed under the MIT License and Anti-996 License.
By using this software, you agree to comply with both licenses.
```

---

## 📞 Contact

- **Bug Reports**: [GitHub Issues](https://github.com/qiyuey/ipv6-toolbox/issues)
- **Feature Requests**: [GitHub Discussions](https://github.com/qiyuey/ipv6-toolbox/discussions)

---

## 🌟 Support the Project

If this project helps you, please consider:

- ⭐ Star the project
- 🐛 Report bugs and suggest features
- 🔀 Contribute code
- 📢 Share with others

---

<div align="center">

**Built with ❤️ for a better IPv6 networking experience**

[Homepage](https://github.com/qiyuey/ipv6-toolbox) • [Documentation](docs/) • [Changelog](CHANGELOG.md)

</div>
