package com.huarongdao.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.huarongdao.domain.model.*
import com.huarongdao.ui.theme.HuaRongColors
import kotlin.math.abs

@Composable
fun GameBoard(
    gameState: GameState,
    selectedPieceId: Int?,
    cellSize: Dp,
    isDemoMode: Boolean,
    onPieceClick: (Int) -> Unit,
    onPieceDrag: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val boardWidth = cellSize * BOARD_COLS
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
        // Grid lines
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawBoardGrid(cellSize.toPx())
        }

        // Pieces
        gameState.pieces.forEach { piece ->
            if (piece.type != PieceType.EMPTY) {
                PieceItem(
                    piece = piece,
                    cellSize = cellSize,
                    isSelected = piece.id == selectedPieceId,
                    isDemoMode = isDemoMode,
                    onPieceClick = onPieceClick,
                    onPieceDrag = onPieceDrag
                )
            }
        }

        // Exit indicator at bottom center
        ExitIndicator(cellSize = cellSize, boardHeight = boardHeight)
    }
}

@Composable
private fun ExitIndicator(cellSize: Dp, boardHeight: Dp) {
    // Arrow at the bottom center showing exit
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

    val cellPx = with(LocalDensity.current) { cellSize.toPx() }
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
                if (!isDemoMode) {
                    detectTapGestures { onPieceClick(piece.id) }
                }
            }
            .pointerInput(piece.id, isDemoMode) {
                if (!isDemoMode) {
                    detectDragGestures(
                        onDragStart = {
                            dragAccumX = 0f
                            dragAccumY = 0f
                        },
                        onDrag = { _, dragAmount ->
                            dragAccumX += dragAmount.x
                            dragAccumY += dragAmount.y
                            val threshold = cellPx * 0.4f
                            when {
                                abs(dragAccumX) > abs(dragAccumY) && abs(dragAccumX) > threshold -> {
                                    val dc = if (dragAccumX > 0) 1 else -1
                                    onPieceDrag(piece.id, dc, 0)
                                    dragAccumX = 0f
                                    dragAccumY = 0f
                                }
                                abs(dragAccumY) > abs(dragAccumX) && abs(dragAccumY) > threshold -> {
                                    val dr = if (dragAccumY > 0) 1 else -1
                                    onPieceDrag(piece.id, 0, dr)
                                    dragAccumX = 0f
                                    dragAccumY = 0f
                                }
                            }
                        }
                    )
                }
            }
    ) {
        PieceContent(piece = piece, isSelected = isSelected)
    }
}

@Composable
fun PieceContent(piece: Piece, isSelected: Boolean) {
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
        // Cartoon decoration via Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPieceDecoration(piece.type, config, size)
        }

        // Name label
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Emoji / character face
            Text(
                text = config.emoji,
                fontSize = when {
                    piece.type == PieceType.CAO_CAO -> 28.sp
                    piece.width == 2 || piece.height == 2 -> 20.sp
                    else -> 16.sp
                },
                textAlign = TextAlign.Center
            )
            Text(
                text = config.nameZh,
                color = config.textColor,
                fontSize = when {
                    piece.type == PieceType.CAO_CAO -> 13.sp
                    piece.type == PieceType.SOLDIER -> 10.sp
                    else -> 11.sp
                },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class PieceVisualConfig(
    val colorLight: Color,
    val colorDark: Color,
    val textColor: Color,
    val nameZh: String,
    val nameEn: String,
    val emoji: String
)

fun getPieceVisualConfig(type: PieceType): PieceVisualConfig = when (type) {
    PieceType.CAO_CAO -> PieceVisualConfig(
        colorLight = Color(0xFFFFD700),
        colorDark = Color(0xFFB8860B),
        textColor = Color(0xFF5D4037),
        nameZh = "曹操",
        nameEn = "Cao Cao",
        emoji = "👑"
    )
    PieceType.GUAN_YU -> PieceVisualConfig(
        colorLight = Color(0xFF4CAF50),
        colorDark = Color(0xFF1B5E20),
        textColor = Color.White,
        nameZh = "关羽",
        nameEn = "Guan Yu",
        emoji = "🗡️"
    )
    PieceType.ZHANG_FEI -> PieceVisualConfig(
        colorLight = Color(0xFF546E7A),
        colorDark = Color(0xFF263238),
        textColor = Color.White,
        nameZh = "张飞",
        nameEn = "Zhang Fei",
        emoji = "⚔️"
    )
    PieceType.ZHAO_YUN -> PieceVisualConfig(
        colorLight = Color(0xFFECEFF1),
        colorDark = Color(0xFF90A4AE),
        textColor = Color(0xFF263238),
        nameZh = "赵云",
        nameEn = "Zhao Yun",
        emoji = "🏹"
    )
    PieceType.HUANG_ZHONG -> PieceVisualConfig(
        colorLight = Color(0xFFFF8F00),
        colorDark = Color(0xFFE65100),
        textColor = Color.White,
        nameZh = "黄忠",
        nameEn = "Huang Zhong",
        emoji = "🏹"
    )
    PieceType.MA_CHAO -> PieceVisualConfig(
        colorLight = Color(0xFF9C27B0),
        colorDark = Color(0xFF4A148C),
        textColor = Color.White,
        nameZh = "马超",
        nameEn = "Ma Chao",
        emoji = "🐴"
    )
    PieceType.SOLDIER -> PieceVisualConfig(
        colorLight = Color(0xFF42A5F5),
        colorDark = Color(0xFF1565C0),
        textColor = Color.White,
        nameZh = "卒",
        nameEn = "Soldier",
        emoji = "🛡️"
    )
    PieceType.EMPTY -> PieceVisualConfig(
        colorLight = Color.Transparent,
        colorDark = Color.Transparent,
        textColor = Color.Transparent,
        nameZh = "",
        nameEn = "",
        emoji = ""
    )
}

private fun DrawScope.drawPieceDecoration(type: PieceType, config: PieceVisualConfig, sz: Size) {
    // Draw decorative border inside piece
    drawRoundRect(
        color = config.textColor.copy(alpha = 0.2f),
        topLeft = Offset(4f, 4f),
        size = Size(sz.width - 8f, sz.height - 8f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f),
        style = Stroke(width = 2f)
    )

    // Special decoration for Cao Cao - dragon pattern corners
    if (type == PieceType.CAO_CAO) {
        val cornerSize = sz.width * 0.12f
        listOf(
            Offset(8f, 8f), Offset(sz.width - 8f, 8f),
            Offset(8f, sz.height - 8f), Offset(sz.width - 8f, sz.height - 8f)
        ).forEach { corner ->
            drawCircle(
                color = Color(0xFFB8860B),
                radius = cornerSize,
                center = corner
            )
        }
    }
}