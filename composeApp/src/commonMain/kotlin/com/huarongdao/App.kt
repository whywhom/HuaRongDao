package com.huarongdao

import androidx.compose.runtime.*
import com.huarongdao.data.preferences.AppSettings
import com.huarongdao.data.preferences.SettingsRepository
import com.huarongdao.di.createPlatformModule
import com.huarongdao.di.viewModelModule
import com.huarongdao.ui.screens.game.GameScreen
import com.huarongdao.ui.screens.help.HelpScreen
import com.huarongdao.ui.screens.levelselect.LevelSelectScreen
import com.huarongdao.ui.screens.settings.SettingsScreen
import com.huarongdao.ui.screens.splash.SplashScreen
import com.huarongdao.ui.theme.HuaRongDaoTheme
import com.huarongdao.utils.ChineseStrings
import com.huarongdao.utils.EnglishStrings
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

sealed class Screen {
    object Splash : Screen()
    object LevelSelect : Screen()
    data class Game(val levelId: Int) : Screen()
    object Help : Screen()
    object Settings : Screen()
}

@Composable
fun App() {
    val platformModule = remember { createPlatformModule() }

    KoinApplication(
        application = {
            modules(platformModule, viewModelModule)
        }
    ) {
        AppContent()
    }
}

@Composable
private fun AppContent() {
    val settingsRepo: SettingsRepository = koinInject()
    val settings by settingsRepo.settings.collectAsState(initial = AppSettings())

    val isDark = settings.isDarkMode
    val strings = when (settings.language) {
        "zh" -> ChineseStrings
        "en" -> EnglishStrings
        else -> EnglishStrings
    }

    HuaRongDaoTheme(darkTheme = isDark) {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }

        when (val screen = currentScreen) {
            is Screen.Splash -> SplashScreen(
                strings = strings,
                isDark = isDark,
                onNavigateToLevelSelect = { currentScreen = Screen.LevelSelect }
            )
            is Screen.LevelSelect -> LevelSelectScreen(
                strings = strings,
                onSelectLevel = { levelId -> currentScreen = Screen.Game(levelId) },
                onNavigateSettings = { currentScreen = Screen.Settings },
                onNavigateHelp = { currentScreen = Screen.Help }
            )
            is Screen.Game -> GameScreen(
                levelId = screen.levelId,
                strings = strings,
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
