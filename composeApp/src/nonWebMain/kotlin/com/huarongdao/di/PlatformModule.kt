package com.huarongdao.di

import com.huarongdao.data.database.getDatabaseBuilder
import com.huarongdao.data.preferences.SettingsRepository
import com.huarongdao.data.preferences.createDataStore
import com.huarongdao.data.repository.GameRepository
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun createPlatformModule(): Module = module {
    includes(viewModelModule)            // GameViewModel + LevelSelectViewModel + SettingsViewModel
    single { getDatabaseBuilder().build() }
    single { SettingsRepository(createDataStore()) }
    single { GameRepository(get()) }
}
