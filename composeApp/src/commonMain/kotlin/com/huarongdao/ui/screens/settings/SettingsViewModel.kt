package com.huarongdao.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huarongdao.data.preferences.AppSettings
import com.huarongdao.data.preferences.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val isLoading: Boolean = true
)

sealed class SettingsIntent {
    data class SetDarkMode(val enabled: Boolean) : SettingsIntent()
    data class SetLanguage(val lang: String) : SettingsIntent()
    data class SetSound(val enabled: Boolean) : SettingsIntent()
    data class SetVibration(val enabled: Boolean) : SettingsIntent()
}

class SettingsViewModel(
    private val repo: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.settings.collect { settings ->
                _uiState.update { it.copy(settings = settings, isLoading = false) }
            }
        }
    }

    fun handleIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is SettingsIntent.SetDarkMode -> repo.setDarkMode(intent.enabled)
                is SettingsIntent.SetLanguage -> repo.setLanguage(intent.lang)
                is SettingsIntent.SetSound -> repo.setSoundEnabled(intent.enabled)
                is SettingsIntent.SetVibration -> repo.setVibrationEnabled(intent.enabled)
            }
        }
    }
}
