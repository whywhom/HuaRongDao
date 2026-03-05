package com.huarongdao.ui.screens.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.huarongdao.ui.theme.HuaRongColors
import com.huarongdao.utils.Strings
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    strings: Strings,
    isDark: Boolean,
    onNavigateToLevelSelect: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    val titleScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.3f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 150f),
        label = "title_scale"
    )
    val titleAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800),
        label = "title_alpha"
    )
    val buttonAlpha by animateFloatAsState(
        targetValue = if (showButton) 1f else 0f,
        animationSpec = tween(600),
        label = "btn_alpha"
    )

    LaunchedEffect(Unit) {
        delay(200)
        visible = true
        delay(1000)
        showButton = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = if (isDark)
                        listOf(Color(0xFF4A1A00), Color(0xFF1A0A00))
                    else
                        listOf(Color(0xFFFFCC80), Color(0xFFBF360C)),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Dragon / decorative element
            Text(
                text = "🐉",
                fontSize = 80.sp,
                modifier = Modifier
                    .scale(titleScale)
                    .alpha(titleAlpha)
            )

            // Title
            Text(
                text = "华容道",
                fontSize = 52.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HuaRongColors.CaoCaoGold,
                modifier = Modifier
                    .scale(titleScale)
                    .alpha(titleAlpha)
            )

            Text(
                text = strings.appName,
                fontSize = 20.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .alpha(titleAlpha)
            )

            Text(
                text = strings.splashTagline,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .alpha(titleAlpha)
            )

            Spacer(Modifier.height(32.dp))

            // Hero emojis
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.alpha(titleAlpha)
            ) {
                listOf("👑", "🗡️", "⚔️", "🏹", "🐴", "🛡️").forEach { emoji ->
                    Text(text = emoji, fontSize = 28.sp)
                }
            }

            Spacer(Modifier.height(48.dp))

            // Start button
            Button(
                onClick = onNavigateToLevelSelect,
                modifier = Modifier
                    .alpha(buttonAlpha)
                    .padding(horizontal = 48.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HuaRongColors.CaoCaoGold,
                    contentColor = Color(0xFF5D4037)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = strings.splashStart,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Decorative corner elements
        Text(
            text = "三",
            fontSize = 120.sp,
            color = Color.White.copy(alpha = 0.04f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset((-20).dp, (-20).dp)
        )
        Text(
            text = "国",
            fontSize = 120.sp,
            color = Color.White.copy(alpha = 0.04f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(20.dp, 20.dp)
        )
    }
}
