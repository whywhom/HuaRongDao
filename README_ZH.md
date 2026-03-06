# 华容道 (Hua Rong Dao)

> 三国滑块解谜游戏 · Kotlin Multiplatform · Compose Multiplatform

[**English →**](README.md)

经典华容道棋盘游戏的多平台实现。将曹操从重重包围中移出棋盘，内置 A\* 自动求解与逐帧演示通关动画。

---

## 平台支持

| 平台 | 入口 | 构建命令 |
|------|------|---------|
| Android | `androidMain/MainActivity.kt` | `./gradlew :composeApp:installDebug` |
| iOS | `iosMain/MainViewController.kt` | Xcode 打开 `iosApp/iosApp.xcodeproj` |
| Desktop (JVM) | `desktopMain/Main.kt` | `./gradlew :composeApp:run` |
| Web (Wasm) | `wasmJsMain/main.kt` | `./gradlew :composeApp:wasmJsBrowserDevelopmentRun` |

---

## 技术栈

| 类别 | 库 | 版本 |
|------|----|------|
| 语言 | Kotlin Multiplatform | 2.0.21 |
| UI | Compose Multiplatform | 1.7.0 |
| 依赖注入 | Koin | 4.0.0 |
| 数据库 | Room (KMP) | 2.7.0-alpha11 |
| SQLite 驱动 | SQLite Bundled | 2.5.0-alpha11 |
| 偏好设置 | DataStore Preferences | 1.1.1 |
| 序列化 | kotlinx.serialization | 1.7.3 |
| 协程 | kotlinx.coroutines | 1.9.0 |
| Android Gradle | AGP | 8.5.2 |

---

## 项目结构

```
composeApp/src/
├── commonMain/            # 所有平台共用
│   ├── App.kt             # Koin 初始化 + expect AppContent()
│   ├── data/preferences/
│   │   └── SettingsRepository.kt    # expect class（接口声明）
│   ├── di/
│   │   ├── PlatformModule.kt        # expect createPlatformModule()
│   │   └── SettingsModules.kt       # settingsViewModelModule
│   ├── domain/
│   │   ├── model/
│   │   │   ├── GameModels.kt        # Piece, GameState, Level, Move
│   │   │   └── LevelData.kt         # 8 个内置关卡
│   │   └── solver/
│   │       └── HuaRongDaoSolver.kt  # A* 最优路径求解器
│   └── ui/
│       ├── components/GameBoard.kt  # 棋盘渲染 + 手势识别
│       ├── screens/
│       │   ├── help/                # 帮助页
│       │   ├── settings/            # 设置页 + SettingsViewModel
│       │   └── splash/              # 启动页
│       └── theme/Theme.kt
│
├── nonWebMain/            # Android + iOS + Desktop（排除 wasmJs）
│   ├── AppNavigation.kt             # actual AppContent()：完整导航树
│   ├── data/
│   │   ├── database/
│   │   │   ├── HuaRongDatabase.kt   # Room @Database + @Dao + @Entity
│   │   │   └── DatabaseFactory.kt   # expect getDatabaseBuilder()
│   │   ├── preferences/
│   │   │   ├── SettingsRepository.kt  # actual（DataStore 持久化）
│   │   │   └── DataStoreFactory.kt    # expect createDataStore()
│   │   └── repository/
│   │       └── GameRepository.kt    # 关卡读取 + 进度保存
│   ├── di/
│   │   ├── GameModules.kt           # viewModelModule（全部 ViewModel）
│   │   └── PlatformModule.kt        # actual createPlatformModule()
│   └── ui/screens/
│       ├── game/
│       │   ├── GameScreen.kt
│       │   └── GameViewModel.kt
│       └── levelselect/
│           ├── LevelSelectScreen.kt
│           └── LevelSelectViewModel.kt
│
├── wasmJsMain/            # Web 平台
│   ├── AppNavigation.kt             # actual AppContent()：占位页面
│   ├── data/preferences/
│   │   └── SettingsRepository.kt    # actual（MutableStateFlow 内存存储）
│   └── di/PlatformModule.kt         # actual createPlatformModule()
│
├── androidMain/           # Android 平台特定实现
├── iosMain/               # iOS 平台特定实现
└── desktopMain/           # Desktop 平台特定实现
```

---

## 架构设计

### MVI 模式

每个屏幕遵循单向数据流：

```
用户操作
   ↓
Intent（密封类）
   ↓
ViewModel.handleIntent()
   ↓
UiState（StateFlow）──→ Compose UI 重组
Effect（SharedFlow）──→ 一次性事件（通关弹窗、导航）
```

Effect 用于只触发一次的事件（如通关弹窗），避免页面旋转时重复弹出。

### Source Set 层级

```
commonMain
├── nonWebMain  ← Room / DataStore 依赖放在这里
│   ├── androidMain
│   ├── iosMain
│   └── desktopMain
└── wasmJsMain  ← Room 无 wasmJs 产物，用内存模拟替代
```

`nonWebMain` 通过 `applyDefaultHierarchyTemplate` 自定义，专门隔离 Room / SQLite 等尚未发布 wasmJs 版本的库，保持 `commonMain` 不含平台特定导入，让 `wasmJsMain` 能干净编译。

### expect / actual 清单

| 声明位置 | 接口 | nonWebMain | wasmJsMain |
|---------|------|------------|------------|
| `commonMain` | `expect fun AppContent()` | 完整游戏导航 | 占位页面 |
| `commonMain` | `expect class SettingsRepository` | DataStore 持久化 | StateFlow 内存存储 |
| `commonMain` | `expect fun createPlatformModule()` | Room + DataStore + ViewModel | 仅 SettingsViewModel |
| `nonWebMain` | `expect fun getDatabaseBuilder()` | 各平台 Room 路径 | — |
| `nonWebMain` | `expect fun createDataStore()` | 各平台文件路径 | — |

---

## 游戏设计

### 棋盘与棋子

棋盘大小为 **4 列 × 5 行**，共 10 个棋子：

| 棋子 | 尺寸 | 头像特征 |
|------|------|---------|
| 曹操 👑 | 2×2 | 金色王冠，奸诈细眼，三撇短须，紫色朝袍 |
| 关羽 🗡️ | 2×1（横） | 红脸，卧蚕眉，丹凤眼，黑色飘逸长髯，绿袍 |
| 张飞 ⚔️ | 1×2（竖） | 黑脸，豹眼怒目，环形络腮胡，黑铁甲 |
| 赵云 🏹 | 1×2（竖） | 白面俊朗，银白头盔蓝羽翎，剑眉星目 |
| 黄忠 🏹 | 1×2（竖） | 老将白眉白鬓，三束白髯，橙铜战袍 |
| 马超 🐴 | 1×2（竖） | 白面英武，紫金头盔，锦甲金钉 |
| 卒 🛡️ | 1×1 | 圆脸小兵，蓝色头盔，憨厚腮红 |

**胜利条件：** 曹操到达 `col=1, row=3`（底部中央出口）。

### 内置关卡（共 8 关）

| # | 中文名 | 英文名 | 难度 |
|---|--------|--------|------|
| 1 | 横刀立马 | Sword Standing | ⭐ |
| 2 | 指挥若定 | Strategic Command | ⭐⭐ |
| 3 | 近在咫尺 | So Close | ⭐⭐ |
| 4 | 过五关 | Five Passes | ⭐⭐⭐ |
| 5 | 兵分三路 | Three Prong Attack | ⭐⭐⭐ |
| 6 | 围而不歼 | Surrounded | ⭐⭐⭐⭐ |
| 7 | 捷足先登 | First to Arrive | ⭐⭐⭐⭐ |
| 8 | 四路进兵 | Four Armies | ⭐⭐⭐⭐⭐ |

### 操作方式

- **点击**棋子选中，再点击方向按钮移动
- **拖拽**棋子：拖动距离超过格子宽度的 40% 时触发移动
- **演示通关**：点击按钮后 A\* 自动求解，600ms/步逐帧播放

---

## A\* 求解器

文件：`commonMain/domain/solver/HuaRongDaoSolver.kt`

### 算法概述

A\* 是带启发函数的最优优先搜索，每个节点的优先级由 `f = g + h` 决定：

```
g(n) = 从初始状态到当前状态已走的步数（实际代价）
h(n) = 从当前状态到终态的估计剩余步数（启发代价）
f(n) = g(n) + h(n)
```

每次从优先队列中取出 `f` 最小的节点展开。只要 `h(n)` 满足**可采纳性**（永不高估真实剩余步数），A\* 保证找到最优解。

### 启发函数 h(n)

```
h(n) = 曹操到出口的曼哈顿距离
     + 垂直路径上的障碍棋子数 × 2
```

**可采纳性证明：**
- 曼哈顿距离是曹操自身移动步数的下界，不可能以更少步数到达出口。
- 每个障碍棋子至少需要 2 步才能让出路（移走障碍 + 曹操通过），因此 `障碍数 × 2` 也是下界。
- 两个下界之和仍是下界 → 可采纳 → A\* 解最优。

### 状态去重：双 Long 编码

原 BFS 使用字符串拼接作为状态键，每次查找都有堆分配和字符串哈希开销。新方案将棋盘编码为两个 `Long`：

```
棋盘共 20 格（4×5），每格用 4 位表示棋子类型（0=空，1=曹操，…，7=卒）
20 格 × 4 位 = 80 位 = 恰好 2 个 Long

lo：前 16 格（位 0–63）
hi：后  4 格（位 0–15）

状态键 = StateKey2(hi, lo)   // data class，自动生成 equals / hashCode
```

去重查找变为纯整数比较，比字符串哈希快约 3–5 倍，且每次查找零额外内存分配。

### 父指针回溯，消除路径拷贝

原 BFS 每步执行 `moves + move`，将完整历史列表复制一遍，总内存消耗 O(n²)。新方案每个节点只存一个父指针：

```
AStarNode(state, key, g, h, parent, lastMove)
                               ↑         ↑
                           指向父节点   到达本节点的那一步
```

找到终态后，沿父指针链倒序回溯一次即可得到完整路径，总内存 O(n)。

### 内置最小堆

A\* 需要优先队列。这里用 `ArrayDeque` 手动维护二叉堆，无需引入额外依赖：

```
heapPush：末尾插入 → 上浮    O(log n)
heapPop ：取堆顶   → 末尾补位 → 下沉    O(log n)
```

每次都展开当前 `f` 最小的节点，而不是像 FIFO 队列那样盲目按插入顺序处理。

### 与原始 BFS 的对比

| 维度 | 原 BFS | A\* |
|------|--------|-----|
| 搜索策略 | 层序展开，无优先级 | 按 f=g+h 优先展开，导向目标 |
| 路径存储 | 每步复制完整历史列表 O(n²) | 父指针链，回溯一次 O(n) |
| 状态键 | 字符串拼接，堆分配 + 哈希 | 两个 Long，纯整数比较 |
| 优先队列 | ArrayDeque FIFO | 二叉堆 O(log n) |
| 展开节点数（困难关卡） | 数万 | 数百～数千 |
| 解的最优性 | 最优（BFS 天然保证） | 最优（h 可采纳保证） |

---

## 数据层

### Room 数据库

每次移动棋子后自动保存当前局面，下次进入关卡时自动恢复：

```kotlin
@Entity
data class LevelProgressEntity(
    @PrimaryKey val levelId: Int,
    val savedStateJson: String,  // 序列化的 GameState
    val bestMoves: Int,
    val isCompleted: Boolean
)
```

### DataStore 设置项

| Key | 类型 | 默认值 |
|-----|------|--------|
| `dark_mode` | Boolean | false |
| `language` | String | "system"（system / zh / en）|
| `sound_enabled` | Boolean | true |
| `vibration_enabled` | Boolean | true |

---

## 本地运行

**前置要求：** JDK 17+、Android Studio Hedgehog+、Xcode 15+（iOS）

```bash
git clone <repo-url>
cd HuaRongDao

# Android
./gradlew :composeApp:installDebug

# Desktop
./gradlew :composeApp:run

# Web
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# iOS（先生成 framework，再用 Xcode 打开）
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
open iosApp/iosApp.xcodeproj
```

---

## License

MIT License
