package com.huarongdao.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.huarongdao.domain.model.*
import com.huarongdao.ui.theme.HuaRongColors
import huarongdao.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs

@Composable
fun GameBoard(
    lan: String,
    gameState: GameState,
    selectedPieceId: Int?,
    cellSize: Dp,
    isDemoMode: Boolean,
    onPieceClick: (Int) -> Unit,
    onPieceDrag: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val boardWidth  = cellSize * BOARD_COLS
    val boardHeight = cellSize * BOARD_ROWS
    val isDark = MaterialTheme.colorScheme.background == HuaRongColors.BgDark

    Box(
        modifier = modifier
            .width(boardWidth)
            .height(boardHeight)
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        if (isDark) Color(0xFF3E2723) else HuaRongColors.BoardWood,
                        if (isDark) Color(0xFF4E342E) else Color(0xFFC49A6C)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(3.dp, HuaRongColors.BoardBorder, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawBoardGrid(cellSize.toPx())
        }
        gameState.pieces.forEach { piece ->
            if (piece.type != PieceType.EMPTY) {
                PieceItem(
                    lan = lan,
                    piece = piece,
                    cellSize = cellSize,
                    isSelected = piece.id == selectedPieceId,
                    isDemoMode = isDemoMode,
                    onPieceClick = onPieceClick,
                    onPieceDrag = onPieceDrag
                )
            }
        }
        ExitIndicator(cellSize = cellSize, boardHeight = boardHeight)
    }
}

@Composable
private fun ExitIndicator(cellSize: Dp, boardHeight: Dp) {
    Box(
        modifier = Modifier
            .offset(x = cellSize, y = boardHeight - 4.dp)
            .width(cellSize * 2)
            .height(8.dp)
            .background(Color(0xFFFFD700), RoundedCornerShape(4.dp))
    )
}

private fun DrawScope.drawBoardGrid(cellPx: Float) {
    val gridColor = Color(0x33000000)
    for (c in 1 until BOARD_COLS) {
        drawLine(gridColor, Offset(c * cellPx, 0f), Offset(c * cellPx, size.height), 1f)
    }
    for (r in 1 until BOARD_ROWS) {
        drawLine(gridColor, Offset(0f, r * cellPx), Offset(size.width, r * cellPx), 1f)
    }
}

@Composable
fun PieceItem(
    lan: String,
    piece: Piece,
    cellSize: Dp,
    isSelected: Boolean,
    isDemoMode: Boolean,
    onPieceClick: (Int) -> Unit,
    onPieceDrag: (Int, Int, Int) -> Unit
) {
    val animatedCol by animateFloatAsState(
        targetValue = piece.col.toFloat(),
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "col_${piece.id}"
    )
    val animatedRow by animateFloatAsState(
        targetValue = piece.row.toFloat(),
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "row_${piece.id}"
    )
    val selectedElevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 3.dp,
        label = "elevation_${piece.id}"
    )

    val density = LocalDensity.current
    val cellPx: Float = with(density) { cellSize.toPx() }
    var dragAccumX by remember { mutableStateOf(0f) }
    var dragAccumY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .offset(
                x = cellSize * animatedCol + 2.dp,
                y = cellSize * animatedRow + 2.dp
            )
            .width(cellSize * piece.width - 4.dp)
            .height(cellSize * piece.height - 4.dp)
            .shadow(selectedElevation, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .pointerInput(piece.id, isDemoMode) {
                if (!isDemoMode) detectTapGestures { onPieceClick(piece.id) }
            }
            .pointerInput(piece.id, isDemoMode, cellPx) {
                if (!isDemoMode) {
                    detectDragGestures(
                        onDragStart = { dragAccumX = 0f; dragAccumY = 0f },
                        onDrag = { _, dragAmount ->
                            dragAccumX += dragAmount.x
                            dragAccumY += dragAmount.y
                            val threshold: Float = cellPx * 0.4f
                            val ax: Float = abs(dragAccumX)
                            val ay: Float = abs(dragAccumY)
                            when {
                                ax > ay && ax > threshold -> {
                                    onPieceDrag(piece.id, if (dragAccumX > 0f) 1 else -1, 0)
                                    dragAccumX = 0f; dragAccumY = 0f
                                }
                                ay > ax && ay > threshold -> {
                                    onPieceDrag(piece.id, 0, if (dragAccumY > 0f) 1 else -1)
                                    dragAccumX = 0f; dragAccumY = 0f
                                }
                            }
                        }
                    )
                }
            }
    ) {
        PieceContent(lan = lan, piece = piece, isSelected = isSelected)
    }
}

@Composable
fun PieceContent(lan: String, piece: Piece, isSelected: Boolean) {
    val config = getPieceVisualConfig(piece.type)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    listOf(config.colorLight, config.colorDark),
                    start = Offset(0f, 0f),
                    end = Offset(1f, 1f)
                )
            )
            .then(
                if (isSelected)
                    Modifier.border(2.dp, Color.White, RoundedCornerShape(10.dp))
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPieceDecoration(piece.type, config, size)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Avatar image fills most of the piece
            val avatarRes = getPieceAvatarRes(piece.type)
            if (avatarRes != null) {
                androidx.compose.foundation.Image(
                    painter = painterResource(avatarRes),
                    contentDescription = if(lan == "zh") config.nameZh else config.nameEn,
                    contentScale = if (piece.type == PieceType.CAO_CAO)
                        ContentScale.Fit else ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(2.dp)
                )
            } else {
                Spacer(Modifier.weight(1f))
            }
            // Name label at bottom
            Text(
                text = if(lan == "zh") config.nameZh else config.nameEn,
                color = config.textColor,
                fontSize = when (piece.type) {
                    PieceType.CAO_CAO -> 12.sp
                    PieceType.SOLDIER -> 9.sp
                    else -> 10.sp
                },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(config.colorDark.copy(alpha = 0.55f))
                    .padding(vertical = 1.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Resource mapping
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun getPieceAvatarRes(type: PieceType): org.jetbrains.compose.resources.DrawableResource? =
    when (type) {
        PieceType.CAO_CAO      -> Res.drawable.avatar_caocao
        PieceType.GUAN_YU      -> Res.drawable.avatar_guanyu
        PieceType.ZHANG_FEI    -> Res.drawable.avatar_zhangfei
        PieceType.ZHAO_YUN     -> Res.drawable.avatar_zhaoyun
        PieceType.HUANG_ZHONG  -> Res.drawable.avatar_huangzhong
        PieceType.MA_CHAO      -> Res.drawable.avatar_machao
        PieceType.SOLDIER      -> Res.drawable.avatar_soldier
        PieceType.EMPTY        -> null
    }

// ─────────────────────────────────────────────────────────────────────────────
// Visual config (colours + labels)
// ─────────────────────────────────────────────────────────────────────────────

data class PieceVisualConfig(
    val colorLight: Color,
    val colorDark:  Color,
    val textColor:  Color,
    val nameZh: String,
    val nameEn: String,
    val emoji:  String
)

fun getPieceVisualConfig(type: PieceType): PieceVisualConfig = when (type) {
    PieceType.CAO_CAO     -> PieceVisualConfig(Color(0xFFFFD700), Color(0xFFB8860B), Color(0xFF5D4037), "曹操", "Cao Cao",     "👑")
    PieceType.GUAN_YU     -> PieceVisualConfig(Color(0xFF4CAF50), Color(0xFF1B5E20), Color.White,       "关羽", "Guan Yu",     "🗡️")
    PieceType.ZHANG_FEI   -> PieceVisualConfig(Color(0xFF546E7A), Color(0xFF263238), Color.White,       "张飞", "Zhang Fei",   "⚔️")
    PieceType.ZHAO_YUN    -> PieceVisualConfig(Color(0xFFECEFF1), Color(0xFF90A4AE), Color(0xFF263238), "赵云", "Zhao Yun",    "🏹")
    PieceType.HUANG_ZHONG -> PieceVisualConfig(Color(0xFFFF8F00), Color(0xFFE65100), Color.White,       "黄忠", "Huang Zhong", "🏹")
    PieceType.MA_CHAO     -> PieceVisualConfig(Color(0xFF9C27B0), Color(0xFF4A148C), Color.White,       "马超", "Ma Chao",     "🐴")
    PieceType.SOLDIER     -> PieceVisualConfig(Color(0xFF42A5F5), Color(0xFF1565C0), Color.White,       "卒",   "Soldier",     "🛡️")
    PieceType.EMPTY       -> PieceVisualConfig(Color.Transparent, Color.Transparent, Color.Transparent, "",    "",            "")
}

// ─────────────────────────────────────────────────────────────────────────────
// Canvas decorations
// ─────────────────────────────────────────────────────────────────────────────

private fun DrawScope.drawPieceDecoration(type: PieceType, config: PieceVisualConfig, sz: Size) {
    drawRoundRect(
        color = config.textColor.copy(alpha = 0.2f),
        topLeft = Offset(4f, 4f),
        size = Size(sz.width - 8f, sz.height - 8f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f),
        style = Stroke(width = 2f)
    )
    if (type == PieceType.CAO_CAO) {
        val r = sz.width * 0.08f
        listOf(
            Offset(10f, 10f), Offset(sz.width - 10f, 10f),
            Offset(10f, sz.height - 10f), Offset(sz.width - 10f, sz.height - 10f)
        ).forEach { center ->
            drawCircle(color = Color(0xFFB8860B), radius = r, center = center)
        }
    }
}
