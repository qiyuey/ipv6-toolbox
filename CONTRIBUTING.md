# 贡献指南 (Contributing Guide)

感谢你考虑为 IPv6 工具箱做出贡献！🎉

我们欢迎任何形式的贡献，包括但不限于：
- 🐛 报告 Bug
- 💡 提出新功能建议
- 📝 改进文档
- 🔧 提交代码
- 🌍 翻译

---

## 行为准则

参与本项目即表示你同意遵守我们的 [行为准则](CODE_OF_CONDUCT.md)。请保持友善、尊重和包容。

---

## 如何贡献

### 报告 Bug 🐛

在提交 Bug 报告前，请：
1. 检查 [已有 Issues](https://github.com/qiyuey/ipv6-toolbox/issues) 避免重复
2. 使用最新版本测试问题是否仍然存在

**好的 Bug 报告应包含：**
- 清晰的标题和描述
- 复现步骤（越详细越好）
- 预期行为和实际行为
- 截图或录屏（如果适用）
- 环境信息：
  - 操作系统和版本
  - 应用版本
  - 设备型号（移动端）

**Bug 报告模板：**
```markdown
### 描述
简要描述遇到的问题

### 复现步骤
1. 打开应用
2. 点击 "IPv6 工具"
3. 输入 "2001:db8::1"
4. ...

### 预期行为
应该显示地址信息

### 实际行为
应用崩溃

### 环境信息
- OS: macOS 14.0
- 应用版本: 0.1.0
- 设备: MacBook Pro 2023
```

### 建议新功能 💡

在提交功能建议前，请：
1. 查看 [ROADMAP.md](ROADMAP.md) 确认功能是否已规划
2. 搜索已有 Issues 避免重复

**好的功能建议应包含：**
- 功能的使用场景
- 解决的问题
- 建议的实现方式（可选）
- 参考案例或竞品（可选）

### 改进文档 📝

文档同样重要！你可以：
- 修正拼写或语法错误
- 改进措辞和表达
- 添加示例和说明
- 翻译文档

### 提交代码 🔧

#### 开发环境设置

1. **Fork 仓库并克隆**
   ```bash
   git clone https://github.com/YOUR_USERNAME/ipv6-toolbox.git
   cd ipv6-toolbox
   ```

2. **设置上游仓库**
   ```bash
   git remote add upstream https://github.com/qiyuey/ipv6-toolbox.git
   ```

3. **安装依赖**
   - JDK 11 或更高版本
   - Android Studio 或 IntelliJ IDEA
   - Xcode（仅 macOS，用于 iOS 开发）

4. **运行项目**
   ```bash
   # Desktop
   ./gradlew :composeApp:run
   
   # Android
   ./gradlew :composeApp:assembleDebug
   ```

#### 开发流程

1. **创建分支**
   ```bash
   git checkout -b feature/amazing-feature
   # 或
   git checkout -b fix/bug-description
   ```

2. **遵循代码规范**
   - 使用 4 空格缩进
   - 行长度不超过 120 字符
   - 遵循 [Kotlin 编码规范](https://kotlinlang.org/docs/coding-conventions.html)
   - 为公共 API 添加 KDoc 注释
   - Composable 函数使用大驼峰命名

3. **编写测试**
   - 为新功能编写单元测试
   - 确保所有测试通过
   ```bash
   ./gradlew test
   ```

4. **提交代码**
   - 使用清晰的提交信息
   - 一个提交只做一件事
   ```bash
   git add .
   git commit -m "feat: add IPv6 address history feature"
   ```

   **提交信息规范：**
   - `feat:` 新功能
   - `fix:` Bug 修复
   - `docs:` 文档更新
   - `style:` 代码格式调整（不影响功能）
   - `refactor:` 代码重构
   - `test:` 测试相关
   - `chore:` 构建工具或辅助工具的变动

5. **推送到 Fork**
   ```bash
   git push origin feature/amazing-feature
   ```

6. **创建 Pull Request**
   - 在 GitHub 上创建 PR
   - 填写 PR 模板
   - 关联相关 Issue

#### Pull Request 检查清单

- [ ] 代码遵循项目编码规范
- [ ] 添加了必要的测试
- [ ] 所有测试通过
- [ ] 更新了相关文档
- [ ] 提交信息清晰明确
- [ ] 没有合并冲突

---

## 开发指南

### 项目架构

```
Clean Architecture + MVVM

├── domain/           # 业务逻辑层（不依赖平台）
│   ├── models/      # 领域模型
│   ├── usecases/    # 用例
│   └── repositories/# 仓库接口
├── data/            # 数据层（平台实现）
│   └── repositories/# 仓库实现
└── presentation/    # UI 层
    ├── screens/     # 页面
    ├── components/  # 组件
    └── theme/       # 主题
```

### 跨平台开发

使用 `expect/actual` 模式实现平台特定功能：

```kotlin
// commonMain - 定义接口
expect class PlatformFeature {
    fun doSomething(): String
}

// androidMain - Android 实现
actual class PlatformFeature {
    actual fun doSomething(): String = "Android"
}

// iosMain - iOS 实现
actual class PlatformFeature {
    actual fun doSomething(): String = "iOS"
}

// jvmMain - Desktop 实现
actual class PlatformFeature {
    actual fun doSomething(): String = "Desktop"
}
```

### 测试

```kotlin
// commonTest - 跨平台测试
class FeatureTest {
    @Test
    fun `test feature`() {
        val feature = Feature()
        assertTrue(feature.isValid())
    }
}
```

### 常见任务

```bash
# 清理构建
./gradlew clean

# 运行测试
./gradlew test

# 构建所有平台
./gradlew build

# 代码检查（待配置）
./gradlew detekt

# 格式化代码（待配置）
./gradlew ktlintFormat
```

---

## 代码审查流程

1. 自动检查（CI/CD，待配置）
   - 编译检查
   - 单元测试
   - 代码质量检查

2. 人工审查
   - 代码质量
   - 架构一致性
   - 功能完整性
   - 文档完善度

3. 合并条件
   - 至少一位维护者批准
   - 所有检查通过
   - 无合并冲突

---

## 发布流程

1. 更新版本号（`gradle/libs.versions.toml`）
2. 更新 [CHANGELOG.md](CHANGELOG.md)
3. 创建 Git Tag
4. 构建发布版本
5. 发布到 GitHub Releases

---

## 社区

- **GitHub Issues**: [问题跟踪](https://github.com/qiyuey/ipv6-toolbox/issues)
- **GitHub Discussions**: [讨论区](https://github.com/qiyuey/ipv6-toolbox/discussions)

---

## 获取帮助

如果你在贡献过程中遇到问题：

1. 查看 [文档](docs/)
2. 搜索 [已有 Issues](https://github.com/qiyuey/ipv6-toolbox/issues)
3. 在 [Discussions](https://github.com/qiyuey/ipv6-toolbox/discussions) 提问

---

## 许可证

通过贡献代码，你同意你的贡献将在 [MIT License](LICENSE) 和 [Anti-996 License](LICENSE.Anti-996) 下发布。

---

## 致谢

感谢所有贡献者！你们的努力让这个项目变得更好。

[![Contributors](https://contrib.rocks/image?repo=qiyuey/ipv6-toolbox)](https://github.com/qiyuey/ipv6-toolbox/graphs/contributors)

---

**让我们一起构建更好的 IPv6 工具！** 🚀
