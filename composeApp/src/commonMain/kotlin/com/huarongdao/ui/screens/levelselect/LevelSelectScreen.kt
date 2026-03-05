package com.huarongdao.ui.screens.levelselect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.huarongdao.domain.model.Level
import com.huarongdao.domain.model.LevelProgress
import com.huarongdao.ui.theme.HuaRongColors
import com.huarongdao.utils.Strings
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectScreen(
    strings: Strings,
    onSelectLevel: (Int) -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateHelp: () -> Unit,
    viewModel: LevelSelectViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateToLevel.collect { levelId ->
            onSelectLevel(levelId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = strings.levelSelectTitle,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateHelp) {
                        Text("❓", fontSize = 20.sp)
                    }
                    IconButton(onClick = onNavigateSettings) {
                        Icon(Icons.Default.Settings, contentDescription = strings.settingsTitle)
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = padding.calculateTopPadding() + 8.dp,
                bottom = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.levels) { level ->
                val progress = uiState.progressMap[level.id]
                LevelCard(
                    level = level,
                    progress = progress,
                    strings = strings,
                    onClick = { viewModel.handleIntent(LevelSelectIntent.SelectLevel(level.id)) }
                )
            }
        }
    }
}

@Composable
fun LevelCard(
    level: Level,
    progress: LevelProgress?,
    strings: Strings,
    onClick: () -> Unit
) {
    val isCompleted = progress?.isCompleted == true
    val hasSavedState = progress?.savedState != null && !isCompleted

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        if (isCompleted)
                            listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))
                        else
                            listOf(
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.colorScheme.surface
                            )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Level number
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${level.id}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                }

                // Level name
                Text(
                    text = level.nameZh,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    color = if (isCompleted) Color.White else MaterialTheme.colorScheme.onSurface
                )

                // Stars difficulty
                Row {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= level.difficulty) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = null,
                            tint = HuaRongColors.CaoCaoGold,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Status
                when {
                    isCompleted -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("✓", fontSize = 20.sp, color = Color.White)
                            progress?.bestMoves?.let { moves ->
                                Text(
                                    text = strings.bestMoves.replace("%d", "$moves"),
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                    hasSavedState -> {
                        Text(
                            text = strings.continueGame,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    else -> {
                        Text(
                            text = strings.newGame,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
