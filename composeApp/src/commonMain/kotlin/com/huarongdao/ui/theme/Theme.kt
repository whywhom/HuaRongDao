package com.huarongdao.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ==================== Color Palette ====================

object HuaRongColors {
    // Primary - Deep Red (Ancient Chinese)
    val CrimsonRed = Color(0xFFB71C1C)
    val RedLight = Color(0xFFEF5350)
    val RedDark = Color(0xFF7F0000)

    // Board colors
    val BoardWood = Color(0xFFD7A96A)
    val BoardWoodDark = Color(0xFF8D5524)
    val BoardBorder = Color(0xFF5D4037)

    // Piece colors - each hero has distinct color
    val CaoCaoGold = Color(0xFFFFD700)
    val CaoCaoGoldDark = Color(0xFFB8860B)
    val GuanYuGreen = Color(0xFF2E7D32)
    val GuanYuGreenLight = Color(0xFF66BB6A)
    val ZhangFeiBlack = Color(0xFF37474F)
    val ZhaoYunWhite = Color(0xFFECEFF1)
    val ZhaoYunBorder = Color(0xFF90A4AE)
    val HuangZhongOrange = Color(0xFFE65100)
    val MaChaoPurple = Color(0xFF4A148C)
    val SoldierBlue = Color(0xFF1565C0)

    // Background
    val BgLight = Color(0xFFFFF8E1)
    val BgDark = Color(0xFF1A1A2E)
    val SurfaceLight = Color(0xFFFFFDE7)
    val SurfaceDark = Color(0xFF16213E)

    // Text
    val TextPrimary = Color(0xFF212121)
    val TextPrimaryDark = Color(0xFFEEEEEE)
    val TextSecondary = Color(0xFF757575)
}

private val LightColorScheme = lightColorScheme(
    primary = HuaRongColors.CrimsonRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFCDD2),
    onPrimaryContainer = HuaRongColors.RedDark,
    secondary = HuaRongColors.BoardWoodDark,
    onSecondary = Color.White,
    background = HuaRongColors.BgLight,
    onBackground = HuaRongColors.TextPrimary,
    surface = HuaRongColors.SurfaceLight,
    onSurface = HuaRongColors.TextPrimary,
    surfaceVariant = Color(0xFFEFEBE9),
    onSurfaceVariant = Color(0xFF4E342E),
)

private val DarkColorScheme = darkColorScheme(
    primary = HuaRongColors.RedLight,
    onPrimary = Color.Black,
    primaryContainer = HuaRongColors.RedDark,
    onPrimaryContainer = Color(0xFFFFCDD2),
    secondary = HuaRongColors.BoardWood,
    onSecondary = Color.Black,
    background = HuaRongColors.BgDark,
    onBackground = HuaRongColors.TextPrimaryDark,
    surface = HuaRongColors.SurfaceDark,
    onSurface = HuaRongColors.TextPrimaryDark,
    surfaceVariant = Color(0xFF2D2D44),
    onSurfaceVariant = Color(0xFFD7CCC8),
)

@Composable
fun HuaRongDaoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
