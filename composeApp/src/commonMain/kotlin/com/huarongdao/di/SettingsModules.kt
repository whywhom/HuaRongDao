package com.huarongdao.di

import com.huarongdao.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// Only SettingsViewModel is safe in commonMain (no Room dependency).
// Game + LevelSelect ViewModels live in nonWebMain (they reference GameRepository).
val settingsViewModelModule = module {
    viewModel { SettingsViewModel(get()) }
}
