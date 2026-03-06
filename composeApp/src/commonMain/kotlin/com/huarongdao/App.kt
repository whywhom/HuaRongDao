package com.huarongdao

import androidx.compose.runtime.*
import com.huarongdao.di.createPlatformModule
import org.koin.compose.KoinApplication

sealed class Screen {
    object Splash : Screen()
    object LevelSelect : Screen()
    data class Game(val levelId: Int) : Screen()
    object Help : Screen()
    object Settings : Screen()
}

/** Platform-specific navigation tree (nonWebMain vs wasmJsMain). */
@Composable
expect fun AppContent()

@Composable
fun App() {
    val platformModule = remember { createPlatformModule() }
    KoinApplication(application = { modules(platformModule) }) {
        AppContent()
    }
}
