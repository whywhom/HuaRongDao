package com.huarongdao.data.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// wasmJs: no DataStore artifact available — use in-memory StateFlow instead.
// Settings are not persisted across page reloads on the web target.
actual class SettingsRepository {

    private val _settings = MutableStateFlow(AppSettings())
    actual val settings: Flow<AppSettings> = _settings.asStateFlow()

    actual suspend fun setDarkMode(enabled: Boolean) {
        _settings.update { it.copy(isDarkMode = enabled) }
    }
    actual suspend fun setLanguage(lang: String) {
        _settings.update { it.copy(language = lang) }
    }
    actual suspend fun setSoundEnabled(enabled: Boolean) {
        _settings.update { it.copy(soundEnabled = enabled) }
    }
    actual suspend fun setVibrationEnabled(enabled: Boolean) {
        _settings.update { it.copy(vibrationEnabled = enabled) }
    }
}
