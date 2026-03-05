# 华容道 (Hua Rong Dao) — KMP Compose Multiplatform

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android%20|%20iOS%20|%20Desktop%20|%20Web-brightgreen" />
  <img src="https://img.shields.io/badge/Kotlin-2.0.21-blue" />
  <img src="https://img.shields.io/badge/Compose%20Multiplatform-1.7.0-purple" />
  <img src="https://img.shields.io/badge/Architecture-MVI-orange" />
</p>

> 经典三国益智游戏 — The Classic Three Kingdoms Sliding Puzzle

---

## 📖 Overview

华容道 (Hua Rong Dao) is a classic Chinese sliding-block puzzle based on the Three Kingdoms legend. The goal is to move **曹操 (Cao Cao)** — the golden 2×2 block — to the exit at the bottom center of a 4×5 board.

This project is a **full Kotlin Multiplatform (KMP)** implementation targeting:
- 📱 **Android**
- 🍎 **iOS**
- 🖥️ **Desktop** (JVM — macOS, Windows, Linux)
- 🌐 **Web** (Kotlin/Wasm)

---

## 🏗️ Architecture

### MVI (Model-View-Intent)

Each screen follows the **unidirectional data flow** MVI pattern:

```
User Action
     ↓
  Intent
     ↓
ViewModel (processes intent)
     ↓
 UiState (immutable snapshot)
     ↓
  View (Compose UI reacts)
     ↑
  Effect (one-shot side effects: navigation, toasts)
```

**Three core types per screen:**
| Type | Role |
|------|------|
| `*UiState` | Immutable data class. Holds everything the UI needs to render. |
| `*Intent` | Sealed class. Describes user actions (tap, drag, button press). |
| `*Effect` | SharedFlow events. One-shot: navigation, dialogs, sounds. |

**Example — GameScreen:**
```kotlin
// Intent: what the user did
sealed class GameIntent {
    data class LoadLevel(val levelId: Int) : GameIntent()
    data class DragPiece(val pieceId: Int, val dCol: Int, val dRow: Int) : GameIntent()
    object RequestDemo : GameIntent()
    // ...
}

// State: what the UI should show
data class GameUiState(
    val currentState: GameState?,
    val isSolving: Boolean,
    val isDemoMode: Boolean,
    // ...
)

// Effect: one-shot side effects
sealed class GameEffect {
    object LevelCompleted : GameEffect()
    object PieceMoved : GameEffect()
}
```

---

## 🧩 Tech Stack

| Layer | Technology |
|-------|------------|
| **UI** | Jetpack Compose Multiplatform 1.7 |
| **Language** | Kotlin 2.0.21 |
| **Architecture** | MVI + ViewModel |
| **DI** | Koin 4.0 (multiplatform) |
| **Database** | Room (KMP) + SQLite Bundled Driver |
| **Preferences** | DataStore Preferences (KMP) |
| **Async** | Kotlin Coroutines + Flow |
| **Serialization** | kotlinx.serialization JSON |
| **Navigation** | Custom sealed-class nav (no extra dependency) |

---

## 📁 Project Structure

```
HuaRongDao/
├── composeApp/
│   └── src/
│       ├── commonMain/kotlin/com/huarongdao/
│       │   ├── App.kt                          # Root composable, KoinApplication, navigation
│       │   ├── domain/
│       │   │   ├── model/
│       │   │   │   ├── GameModels.kt           # Piece, GameState, Level, Move, LevelProgress
│       │   │   │   └── LevelData.kt            # 8 built-in classic levels
│       │   │   └── solver/
│       │   │       └── HuaRongDaoSolver.kt     # BFS solver + move generator
│       │   ├── data/
│       │   │   ├── database/
│       │   │   │   ├── HuaRongDatabase.kt      # Room DB, DAO, Entity
│       │   │   │   └── DatabaseFactory.kt      # expect fun getDatabaseBuilder()
│       │   │   ├── preferences/
│       │   │   │   ├── SettingsRepository.kt   # DataStore wrapper for AppSettings
│       │   │   │   └── DataStoreFactory.kt     # expect fun createDataStore()
│       │   │   └── repository/
│       │   │       └── GameRepository.kt       # Combines DB + LevelData
│       │   ├── ui/
│       │   │   ├── theme/
│       │   │   │   └── Theme.kt                # Material3 light/dark color schemes
│       │   │   ├── components/
│       │   │   │   └── GameBoard.kt            # Board + PieceItem + cartoon drawing
│       │   │   └── screens/
│       │   │       ├── splash/SplashScreen.kt
│       │   │       ├── levelselect/
│       │   │       │   ├── LevelSelectScreen.kt
│       │   │       │   └── LevelSelectViewModel.kt
│       │   │       ├── game/
│       │   │       │   ├── GameScreen.kt
│       │   │       │   └── GameViewModel.kt
│       │   │       ├── help/HelpScreen.kt
│       │   │       └── settings/
│       │   │           ├── SettingsScreen.kt
│       │   │           └── SettingsViewModel.kt
│       │   ├── di/
│       │   │   └── AppModules.kt               # Koin modules: viewModel, repository
│       │   └── utils/
│       │       └── Strings.kt                  # i18n: EnglishStrings + ChineseStrings
│       │
│       ├── androidMain/
│       │   ├── kotlin/com/huarongdao/
│       │   │   ├── MainActivity.kt
│       │   │   ├── data/database/DatabaseFactory.android.kt
│       │   │   └── data/preferences/DataStoreFactory.android.kt
│       │   ├── AndroidManifest.xml
│       │   └── res/values/strings.xml
│       │
│       ├── iosMain/
│       │   └── kotlin/com/huarongdao/
│       │       ├── MainViewController.kt
│       │       ├── data/database/DatabaseFactory.ios.kt
│       │       └── data/preferences/DataStoreFactory.ios.kt
│       │
│       ├── desktopMain/
│       │   └── kotlin/com/huarongdao/
│       │       ├── Main.kt
│       │       ├── data/database/DatabaseFactory.desktop.kt
│       │       └── data/preferences/DataStoreFactory.desktop.kt
│       │
│       └── wasmJsMain/
│           └── kotlin/com/huarongdao/
│               ├── main.kt
│               ├── data/database/DatabaseFactory.wasmJs.kt
│               └── data/preferences/DataStoreFactory.wasmJs.kt
│
├── iosApp/                                     # iOS Xcode project wrapper
├── gradle/libs.versions.toml                   # Version catalog
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🎮 Game Features

### Screens

| Screen | Description |
|--------|-------------|
| **Splash** | Animated intro with dragon decoration, Chinese calligraphy title, hero emojis |
| **Level Select** | Grid of 8 levels with star difficulty, completion badge, best-moves record, continue state |
| **Game** | Tap/drag pieces, move counter, direction pad, reset, demo button |
| **Help** | Rules explanation, piece guide with cartoon avatars |
| **Settings** | Dark mode toggle, language selector (中/EN/system), sound/vibration |

### Pieces (Characters)

| Piece | Type | Size | Color | Emoji |
|-------|------|------|-------|-------|
| 曹操 Cao Cao | Boss | 2×2 | Gold | 👑 |
| 关羽 Guan Yu | Hero | 2×1 (H) | Green | 🗡️ |
| 张飞 Zhang Fei | Hero | 1×2 (V) | Dark grey | ⚔️ |
| 赵云 Zhao Yun | Hero | 1×2 (V) | White/silver | 🏹 |
| 黄忠 Huang Zhong | Hero | 1×2 (V) | Orange | 🏹 |
| 马超 Ma Chao | Hero | 1×2 (V) | Purple | 🐴 |
| 卒 Soldier ×4 | Pawn | 1×1 | Blue | 🛡️ |

### Levels (8 built-in)

| # | Name (ZH) | Name (EN) | Difficulty |
|---|-----------|-----------|------------|
| 1 | 横刀立马 | Sword Standing | ⭐ |
| 2 | 指挥若定 | Strategic Command | ⭐⭐ |
| 3 | 近在咫尺 | So Close Yet So Far | ⭐⭐ |
| 4 | 过五关 | Five Passes | ⭐⭐⭐ |
| 5 | 兵分三路 | Three Prong Attack | ⭐⭐⭐ |
| 6 | 围而不歼 | Surrounded | ⭐⭐⭐⭐ |
| 7 | 捷足先登 | First to Arrive | ⭐⭐⭐⭐ |
| 8 | 四路进兵 | Four Armies | ⭐⭐⭐⭐⭐ |

---

## 🤖 BFS Auto-Solver

The "Show Solution / 演示通关" feature uses **Breadth-First Search (BFS)** to find the **optimal (minimum moves) solution**.

### Algorithm

```kotlin
// HuaRongDaoSolver.kt
fun solve(initialState: GameState): SolverResult? {
    val visited = HashSet<String>()
    val queue = ArrayDeque<Pair<GameState, List<Move>>>()
    
    queue.add(initialState to emptyList())
    visited.add(initialState.encodeState())
    
    while (queue.isNotEmpty()) {
        val (state, moves) = queue.removeFirst()
        
        for (move in getPossibleMoves(state)) {
            val newState = applyMove(state, move)
            val key = newState.encodeState()
            
            if (key !in visited) {
                if (newState.isSolved()) return SolverResult(moves + move, ...)
                visited.add(key)
                queue.add(newState to (moves + move))
            }
        }
    }
    return null  // No solution
}
```

**State encoding:** Piece positions sorted by ID, joined as `"col:row,col:row,..."`  
**Complexity:** O(b^d) where b ≈ branching factor ~4–10 moves/state, d = solution depth  
**Performance:** Runs on `Dispatchers.Default` (background thread) to avoid blocking UI

**Win condition:**
```kotlin
fun isSolved(): Boolean {
    val caoCao = pieces.find { it.type == PieceType.CAO_CAO }
    return caoCao?.col == 1 && caoCao.row == 3  // Exit at bottom center
}
```

---

## 💾 Data Persistence

### Room Database (Level Progress)

Table: `level_progress`

| Column | Type | Description |
|--------|------|-------------|
| `levelId` | INT (PK) | Level identifier |
| `savedStateJson` | TEXT? | JSON-serialized `GameState` (pieces + moveCount) |
| `bestMoves` | INT? | Best completion score |
| `isCompleted` | BOOL | Whether ever solved |

**Game state is auto-saved** on every move via `GameRepository.saveGameState()`.  
When a player re-enters a level, the saved state is restored automatically.

### DataStore Preferences (Settings)

| Key | Type | Default |
|-----|------|---------|
| `dark_mode` | Boolean | false |
| `language` | String | "system" |
| `sound_enabled` | Boolean | true |
| `vibration_enabled` | Boolean | true |

---

## 🌐 Internationalization

All UI strings are defined as data classes in `Strings.kt`:

```kotlin
val strings = when (settings.language) {
    "zh" -> ChineseStrings
    "en" -> EnglishStrings
    else -> EnglishStrings // + system locale detection
}
```

Both `EnglishStrings` and `ChineseStrings` implement the same `Strings` data class, making it trivial to add more languages.

---

## 🌙 Dark Mode

Dark mode is a toggle in Settings, persisted in DataStore. The theme uses Material3 `lightColorScheme` / `darkColorScheme`:

```kotlin
HuaRongDaoTheme(darkTheme = settings.isDarkMode) { ... }
```

Board and piece colors adapt automatically through the composable color system.

---

## 🔧 DI with Koin

```kotlin
// AppModules.kt
val viewModelModule = module {
    viewModel { GameViewModel(get()) }
    viewModel { LevelSelectViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}

val repositoryModule = module {
    single { GameRepository(get()) }
}

// Platform module (injected at runtime in App.kt)
val platformModule = module {
    single { getDatabaseBuilder().build() }  // expect/actual per platform
    single { settingsRepo }
}
```

ViewModels are retrieved via `koinViewModel()` in composables — no manual factory needed.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Xcode 15+ (for iOS)
- JDK 17+

### Run Android
```bash
./gradlew :composeApp:installDebug
```

### Run Desktop
```bash
./gradlew :composeApp:run
```

### Run Web
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### Run iOS
Open `iosApp/iosApp.xcodeproj` in Xcode and run on simulator or device.

---

## 📐 Design Decisions

### Why custom sealed-class navigation?
Avoids adding Jetpack Navigation or Voyager as a dependency for a single-stack app. The `Screen` sealed class with a `var currentScreen` state is simple, type-safe, and fully multiplatform.

### Why BFS instead of A*?
BFS guarantees the **shortest solution** (minimum moves). For Klotski puzzles with typical boards (up to ~100k reachable states), BFS runs in under a second on modern hardware. A* would be faster for deep solutions but adds heuristic complexity without meaningful benefit here.

### Why Room + DataStore instead of SQLDelight + Settings?
Room 2.7+ has official KMP support and follows the familiar Android Room API. DataStore Preferences KMP is equally well-supported. Both use the bundled SQLite driver, ensuring identical behavior across platforms.

---

## 📄 License

MIT License — see [LICENSE](LICENSE)
