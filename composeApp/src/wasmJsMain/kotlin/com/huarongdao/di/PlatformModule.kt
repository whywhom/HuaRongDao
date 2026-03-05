package com.huarongdao.di

import com.huarongdao.data.preferences.SettingsRepository
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun createPlatformModule(): Module = module {
    includes(settingsViewModelModule)    // only SettingsViewModel (no Room needed)
    single { SettingsRepository() }
}
