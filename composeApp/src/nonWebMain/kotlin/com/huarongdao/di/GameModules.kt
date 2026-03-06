package com.huarongdao.di

import com.huarongdao.ui.screens.game.GameViewModel
import com.huarongdao.ui.screens.levelselect.LevelSelectViewModel
import com.huarongdao.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GameViewModel(get()) }
    viewModel { LevelSelectViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}
