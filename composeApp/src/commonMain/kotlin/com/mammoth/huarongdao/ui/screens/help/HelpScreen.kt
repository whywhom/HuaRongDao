package com.mammoth.huarongdao.ui.screens.help

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.mammoth.huarongdao.domain.model.PieceType
import com.mammoth.huarongdao.ui.components.getPieceVisualConfig
import com.mammoth.huarongdao.utils.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    strings: Strings,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.helpTitle, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.back)
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Intro card
            HelpCard(
                emoji = "🎮",
                title = null,
                body = strings.helpIntro
            )

            // Goal
            HelpCard(
                emoji = "🎯",
                title = strings.helpGoal,
                body = strings.helpGoalDesc
            )

            // How to move
            HelpCard(
                emoji = "👆",
                title = strings.helpMove,
                body = strings.helpMoveDesc
            )

            // Tip / solver
            HelpCard(
                emoji = "💡",
                title = strings.helpTip,
                body = strings.helpTipDesc
            )

            // Pieces guide
            Text(
                text = strings.helpPieces,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            val pieces = listOf(
                Triple(PieceType.CAO_CAO, "2×2", ""),
                Triple(PieceType.GUAN_YU, "2×1", ""),
                Triple(PieceType.ZHANG_FEI, "1×2", ""),
                Triple(PieceType.ZHAO_YUN, "1×2", ""),
                Triple(PieceType.HUANG_ZHONG, "1×2", ""),
                Triple(PieceType.MA_CHAO, "1×2", ""),
                Triple(PieceType.SOLDIER, "1×1", "×4"),
            )

            pieces.forEach { (type, size, extra) ->
                val config = getPieceVisualConfig(type)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(config.colorLight, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(config.emoji, fontSize = 24.sp)
                        }
                        Column {
                            Text(
                                "${config.nameZh} (${config.nameEn}$extra)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                "Size: $size",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HelpCard(emoji: String, title: String?, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(emoji, fontSize = 24.sp)
                if (title != null) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            if (title != null) Spacer(Modifier.height(8.dp))
            Text(
                text = body,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}
