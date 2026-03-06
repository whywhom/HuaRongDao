package com.mammoth.huarongdao

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.mammoth.huarongdao.data.preferences.AppSettings
import com.mammoth.huarongdao.data.preferences.SettingsRepository
import com.mammoth.huarongdao.ui.theme.HuaRongDaoTheme
import org.koin.compose.koinInject

@Composable
actual fun AppContent() {
    val settingsRepo: SettingsRepository = koinInject()
    val settings by settingsRepo.settings.collectAsState(initial = AppSettings())

    HuaRongDaoTheme(darkTheme = settings.isDarkMode) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                Text("华容道 — Web version coming soon", fontSize = 20.sp)
            }
        }
    }
}
