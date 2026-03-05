package com.huarongdao.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.huarongdao.utils.Strings
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    strings: Strings,
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.settingsTitle, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = strings.back)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Dark mode
            SettingsSwitchRow(
                emoji = "🌙",
                label = strings.darkMode,
                checked = uiState.settings.isDarkMode,
                onCheckedChange = {
                    viewModel.handleIntent(SettingsIntent.SetDarkMode(it))
                }
            )

            HorizontalDivider()

            // Language selector
            Text(
                text = strings.language,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )

            val langOptions = listOf(
                "system" to strings.langSystem,
                "zh" to strings.langChinese,
                "en" to strings.langEnglish
            )

            langOptions.forEach { (code, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(label)
                    RadioButton(
                        selected = uiState.settings.language == code,
                        onClick = { viewModel.handleIntent(SettingsIntent.SetLanguage(code)) }
                    )
                }
            }

            HorizontalDivider()

            // Sound
            SettingsSwitchRow(
                emoji = "🔊",
                label = strings.sound,
                checked = uiState.settings.soundEnabled,
                onCheckedChange = {
                    viewModel.handleIntent(SettingsIntent.SetSound(it))
                }
            )

            // Vibration
            SettingsSwitchRow(
                emoji = "📳",
                label = strings.vibration,
                checked = uiState.settings.vibrationEnabled,
                onCheckedChange = {
                    viewModel.handleIntent(SettingsIntent.SetVibration(it))
                }
            )

            Spacer(Modifier.height(32.dp))

            // App info
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("华容道 / Hua Rong Dao", fontWeight = FontWeight.Bold)
                    Text("v1.0.0", style = MaterialTheme.typography.bodySmall)
                    Text("KMP + Compose Multiplatform", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    emoji: String,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(emoji, fontSize = 20.sp)
            Text(label, style = MaterialTheme.typography.bodyLarge)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
