package com.huarongdao.data.preferences

import kotlinx.coroutines.flow.Flow

data class AppSettings(
    val isDarkMode: Boolean = false,
    val language: String = "system",  // "system", "zh", "en"
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true
)

// expect/actual lets each platform provide its own storage backend:
//   nonWebMain  → DataStore (persistent)
//   wasmJsMain  → MutableStateFlow (in-memory, no DataStore dependency)
expect class SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setLanguage(lang: String)
    suspend fun setSoundEnabled(enabled: Boolean)
    suspend fun setVibrationEnabled(enabled: Boolean)
}
