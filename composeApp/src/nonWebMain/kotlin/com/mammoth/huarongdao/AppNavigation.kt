package com.mammoth.huarongdao

import androidx.compose.runtime.*
import com.mammoth.huarongdao.data.preferences.AppSettings
import com.mammoth.huarongdao.data.preferences.SettingsRepository
import com.mammoth.huarongdao.ui.screens.game.GameScreen
import com.mammoth.huarongdao.ui.screens.help.HelpScreen
import com.mammoth.huarongdao.ui.screens.levelselect.LevelSelectScreen
import com.mammoth.huarongdao.ui.screens.settings.SettingsScreen
import com.mammoth.huarongdao.ui.screens.splash.SplashScreen
import com.mammoth.huarongdao.ui.theme.HuaRongDaoTheme
import com.mammoth.huarongdao.utils.ChineseStrings
import com.mammoth.huarongdao.utils.EnglishStrings
import org.koin.compose.koinInject

@Composable
actual fun AppContent() {
    val settingsRepo: SettingsRepository = koinInject()
    val settings by settingsRepo.settings.collectAsState(initial = AppSettings())

    val isDark = settings.isDarkMode
    val strings = when (settings.language) {
        "zh" -> ChineseStrings
        else -> EnglishStrings
    }

    HuaRongDaoTheme(darkTheme = isDark) {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
        when (val screen = currentScreen) {
            is Screen.Splash -> SplashScreen(
                strings = strings, isDark = isDark,
                onNavigateToLevelSelect = { currentScreen = Screen.LevelSelect }
            )
            is Screen.LevelSelect -> LevelSelectScreen(
                lan = settings.language,
                strings = strings,
                onSelectLevel = { levelId -> currentScreen = Screen.Game(levelId) },
                onNavigateSettings = { currentScreen = Screen.Settings },
                onNavigateHelp = { currentScreen = Screen.Help }
            )
            is Screen.Game -> GameScreen(
                lan = settings.language,
                levelId = screen.levelId, strings = strings,
                onNavigateBack = { currentScreen = Screen.LevelSelect }
            )
            is Screen.Help -> HelpScreen(
                strings = strings,
                onNavigateBack = { currentScreen = Screen.LevelSelect }
            )
            is Screen.Settings -> SettingsScreen(
                strings = strings,
                onNavigateBack = { currentScreen = Screen.LevelSelect }
            )
        }
    }
}
