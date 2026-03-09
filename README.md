# Hua Rong Dao (华容道)

> Three Kingdoms sliding puzzle · Kotlin Multiplatform · Compose Multiplatform

[**中文文档 →**](README_ZH.md)

A cross-platform implementation of the classic Hua Rong Dao (Klotski) puzzle. Move Cao Cao through the surrounding generals to the exit — with a built-in A\* solver that finds the optimal solution and plays it back frame by frame.

---

## Platform Support

| Platform | Entry Point | Build Command |
|----------|-------------|---------------|
| Android | `androidMain/MainActivity.kt` | `./gradlew :composeApp:installDebug` |
| iOS | `iosMain/MainViewController.kt` | Open `iosApp/iosApp.xcodeproj` in Xcode |
| Desktop (JVM) | `desktopMain/Main.kt` | `./gradlew :composeApp:run` |
| Web (Wasm) | `wasmJsMain/main.kt` | `./gradlew :composeApp:wasmJsBrowserDevelopmentRun` |

---

## Tech Stack

| Category | Library | Version |
|----------|---------|---------|
| Language | Kotlin Multiplatform | 2.0.21 |
| UI | Compose Multiplatform | 1.7.0 |
| DI | Koin | 4.0.0 |
| Database | Room (KMP) | 2.7.0-alpha11 |
| SQLite Driver | SQLite Bundled | 2.5.0-alpha11 |
| Preferences | DataStore Preferences | 1.1.1 |
| Serialization | kotlinx.serialization | 1.7.3 |
| Coroutines | kotlinx.coroutines | 1.9.0 |
| Android Gradle | AGP | 8.5.2 |

---

## Project Structure

```
composeApp/src/
├── commonMain/            # All platforms
│   ├── App.kt             # Koin init + expect AppContent()
│   ├── data/preferences/
│   │   └── SettingsRepository.kt    # expect class (contract only)
│   ├── di/
│   │   ├── PlatformModule.kt        # expect createPlatformModule()
│   │   └── SettingsModules.kt       # settingsViewModelModule
│   ├── domain/
│   │   ├── model/
│   │   │   ├── GameModels.kt        # Piece, GameState, Level, Move
│   │   │   └── LevelData.kt         # 8 built-in levels
│   │   └── solver/
│   │       └── HuaRongDaoSolver.kt  # A* optimal solver
│   └── ui/
│       ├── components/GameBoard.kt  # Board rendering + gestures
│       ├── screens/
│       │   ├── help/
│       │   ├── settings/            # SettingsViewModel
│       │   └── splash/
│       └── theme/Theme.kt
│
├── nonWebMain/            # Android + iOS + Desktop (excludes wasmJs)
│   ├── AppNavigation.kt             # actual AppContent(): full nav tree
│   ├── data/
│   │   ├── database/
│   │   │   ├── HuaRongDatabase.kt   # Room @Database + @Dao + @Entity
│   │   │   └── DatabaseFactory.kt   # expect getDatabaseBuilder()
│   │   ├── preferences/
│   │   │   ├── SettingsRepository.kt  # actual (DataStore persistence)
│   │   │   └── DataStoreFactory.kt    # expect createDataStore()
│   │   └── repository/
│   │       └── GameRepository.kt    # Level loading + progress saving
│   ├── di/
│   │   ├── GameModules.kt           # viewModelModule (all ViewModels)
│   │   └── PlatformModule.kt        # actual createPlatformModule()
│   └── ui/screens/
│       ├── game/
│       │   ├── GameScreen.kt
│       │   └── GameViewModel.kt
│       └── levelselect/
│           ├── LevelSelectScreen.kt
│           └── LevelSelectViewModel.kt
│
├── wasmJsMain/            # Web platform
│   ├── AppNavigation.kt             # actual AppContent(): placeholder
│   ├── data/preferences/
│   │   └── SettingsRepository.kt    # actual (MutableStateFlow, in-memory)
│   └── di/PlatformModule.kt         # actual createPlatformModule()
│
├── androidMain/           # Android-specific implementations
├── iosMain/               # iOS-specific implementations
└── desktopMain/           # Desktop-specific implementations
```

---

## Architecture

### MVI Pattern

Each screen follows a strict unidirectional data flow:

```
User action
    ↓
Intent (sealed class)
    ↓
ViewModel.handleIntent()
    ↓
UiState (StateFlow) ──→ Compose recomposition
Effect  (SharedFlow) ──→ One-shot events (dialogs, navigation)
```

Effects are used for events that must fire exactly once (e.g. level-complete dialog), preventing them from re-triggering on recomposition or screen rotation.

### Source Set Hierarchy

```
commonMain
├── nonWebMain  ← Room / DataStore dependencies live here
│   ├── androidMain
│   ├── iosMain
│   └── desktopMain
└── wasmJsMain  ← No Room artifact for Wasm; replaced with in-memory stubs
```

`nonWebMain` is a custom intermediate source set created via `applyDefaultHierarchyTemplate`. It isolates libraries (Room, SQLite, DataStore) that have not yet published a `wasmJs` artifact, keeping `commonMain` free of platform-specific imports and allowing `wasmJsMain` to compile cleanly.

### expect / actual Contract

| Declared in | Interface | nonWebMain | wasmJsMain |
|-------------|-----------|------------|------------|
| `commonMain` | `expect fun AppContent()` | Full game navigation | Placeholder screen |
| `commonMain` | `expect class SettingsRepository` | DataStore (persistent) | StateFlow (in-memory) |
| `commonMain` | `expect fun createPlatformModule()` | Room + DataStore + ViewModels | SettingsViewModel only |
| `nonWebMain` | `expect fun getDatabaseBuilder()` | Platform Room path | — |
| `nonWebMain` | `expect fun createDataStore()` | Platform file path | — |

---

## Game Design

### Board & Pieces

The board is **4 columns × 5 rows**, holding exactly 10 pieces:

| Piece | Size | Character |
|-------|------|-----------|
| Cao Cao 👑 | 2×2 | The target piece — must reach the exit |
| Guan Yu 🗡️ | 2×1 (H) | Horizontal general |
| Zhang Fei ⚔️ | 1×2 (V) | Vertical general |
| Zhao Yun 🏹 | 1×2 (V) | Vertical general |
| Huang Zhong 🏹 | 1×2 (V) | Vertical general |
| Ma Chao 🐴 | 1×2 (V) | Vertical general |
| Soldier 🛡️ × 4 | 1×1 | Small pieces |

**Win condition:** Cao Cao reaches `col=1, row=3` (the exit at the bottom centre).

### Built-in Levels (8 total)

| # | Chinese Name | English Name | Difficulty |
|---|--------------|--------------|------------|
| 1 | 横刀立马 | Sword Standing | ⭐ |
| 2 | 指挥若定 | Strategic Command | ⭐⭐ |
| 3 | 近在咫尺 | So Close | ⭐⭐ |
| 4 | 过五关 | Five Passes | ⭐⭐⭐ |
| 5 | 兵分三路 | Three Prong Attack | ⭐⭐⭐ |
| 6 | 围而不歼 | Surrounded | ⭐⭐⭐⭐ |
| 7 | 捷足先登 | First to Arrive | ⭐⭐⭐⭐ |
| 8 | 四路进兵 | Four Armies | ⭐⭐⭐⭐⭐ |

### Controls

- **Tap** a piece to select it, then tap a direction button to move
- **Drag** a piece: triggers a move once the drag exceeds 40% of a cell width
- **Demo**: tap the button to run the A\* solver and watch the solution play back at 600 ms per step

---

## A\* Solver

File: `commonMain/domain/solver/HuaRongDaoSolver.kt`

### Overview

A\* is an informed best-first search. Each node is prioritised by:

```
f(n) = g(n) + h(n)

g(n)  actual cost  — steps taken from the initial state to reach n
h(n)  heuristic    — estimated steps remaining from n to the goal
```

The node with the lowest `f` is always expanded next. Provided `h(n)` is **admissible** (never overestimates the true remaining cost), A\* is guaranteed to find the optimal solution.

### Heuristic h(n)

```
h(n) = Manhattan distance of Cao Cao to the exit
     + number of pieces blocking Cao Cao's vertical path × 2
```

**Admissibility proof:**
- The Manhattan distance is a lower bound on Cao Cao's own moves — it cannot move there in fewer steps.
- Each blocker requires at least 2 moves to clear (move the blocker out, then advance Cao Cao). So `blockers × 2` is also a lower bound.
- The sum of two lower bounds is still a lower bound → admissible → solution is optimal.

### State Deduplication: Dual-Long Encoding

The original BFS used string concatenation as state keys, incurring heap allocation and string hashing on every node. The new scheme encodes the entire board into two `Long` values:

```
Board: 20 cells (4×5)
Each cell: 4 bits encoding the piece type (0 = empty, 1 = CaoCao … 7 = Soldier)
Total: 20 × 4 = 80 bits = exactly 2 × Long

lo  →  cells 0–15   (bits 0–63)
hi  →  cells 16–19  (bits 0–15)

StateKey2(hi, lo)   // data class → equals/hashCode generated automatically
```

Lookup is a pure integer comparison — roughly 3–5× faster than string hashing with zero heap allocation per lookup.

### Parent-Pointer Graph — Eliminating Path Copies

The original BFS stored the full move history in every queue entry (`moves + move`), copying the entire list at each step — O(n²) memory overall. The new approach stores only a parent pointer in each node:

```
AStarNode(state, key, g, h, parent, lastMove)
                              ↑        ↑
                        points to    the move that
                        parent node  led to this node
```

When the goal is reached, a single reverse walk up the parent chain reconstructs the full path — O(n) total memory.

### Built-in Min-Heap

A\* requires a priority queue. Rather than pulling in a dependency, the solver maintains a binary heap over `ArrayDeque`:

```
heapPush  append to tail → sift up    O(log n)
heapPop   remove root → move tail to root → sift down    O(log n)
```

This ensures the node with the lowest `f = g + h` is always expanded next, which is what drives A\* toward the goal instead of exploring blindly.

### A\* vs Original BFS

| Dimension | Original BFS | A\* |
|-----------|-------------|-----|
| Search strategy | Expand layer by layer, no priority | Expand lowest f=g+h first, guided toward goal |
| Path storage | Full history copied every step — O(n²) | Parent-pointer chain, reconstructed once — O(n) |
| State key | String concatenation, heap alloc + hash | Two Longs, integer comparison |
| Priority queue | ArrayDeque FIFO | Binary min-heap O(log n) |
| Nodes expanded (hard levels) | Tens of thousands | Hundreds to low thousands |
| Solution optimality | Optimal (BFS guarantees by layer) | Optimal (admissible h guarantees) |

---

## Data Layer

### Room Database

The current game state is auto-saved after every move and restored when re-entering a level:

```kotlin
@Entity
data class LevelProgressEntity(
    @PrimaryKey val levelId: Int,
    val savedStateJson: String,   // serialised GameState
    val bestMoves: Int,
    val isCompleted: Boolean
)
```

### DataStore Settings

| Key | Type | Default |
|-----|------|---------|
| `dark_mode` | Boolean | false |
| `language` | String | "system" (system / zh / en) |
| `sound_enabled` | Boolean | true |
| `vibration_enabled` | Boolean | true |

---

## Getting Started

**Prerequisites:** JDK 17+, Android Studio Hedgehog or later, Xcode 15+ (iOS only)

```bash
git clone <repo-url>
cd HuaRongDao

# Android
./gradlew :composeApp:installDebug
./gradlew assembleRelease
./gradlew :composeApp:bundleRelease



# Desktop
./gradlew :composeApp:run

# Web
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# iOS — generate the framework first, then open in Xcode
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
open iosApp/iosApp.xcodeproj
```

---

## License

MIT License

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
