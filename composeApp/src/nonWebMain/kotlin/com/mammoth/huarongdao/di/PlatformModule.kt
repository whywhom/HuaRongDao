package com.mammoth.huarongdao.di

import com.mammoth.huarongdao.data.database.getDatabaseBuilder
import com.mammoth.huarongdao.data.preferences.SettingsRepository
import com.mammoth.huarongdao.data.preferences.createDataStore
import com.mammoth.huarongdao.data.repository.GameRepository
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun createPlatformModule(): Module = module {
    includes(viewModelModule)            // GameViewModel + LevelSelectViewModel + SettingsViewModel
    single { getDatabaseBuilder().build() }
    single { SettingsRepository(createDataStore()) }
    single { GameRepository(get()) }
}
