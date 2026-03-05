package com.huarongdao.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

actual class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    actual val settings: Flow<AppSettings> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs ->
            AppSettings(
                isDarkMode = prefs[DARK_MODE] ?: false,
                language = prefs[LANGUAGE] ?: "system",
                soundEnabled = prefs[SOUND_ENABLED] ?: true,
                vibrationEnabled = prefs[VIBRATION_ENABLED] ?: true
            )
        }

    actual suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[DARK_MODE] = enabled }
    }
    actual suspend fun setLanguage(lang: String) {
        dataStore.edit { it[LANGUAGE] = lang }
    }
    actual suspend fun setSoundEnabled(enabled: Boolean) {
        dataStore.edit { it[SOUND_ENABLED] = enabled }
    }
    actual suspend fun setVibrationEnabled(enabled: Boolean) {
        dataStore.edit { it[VIBRATION_ENABLED] = enabled }
    }

    companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
    }
}
