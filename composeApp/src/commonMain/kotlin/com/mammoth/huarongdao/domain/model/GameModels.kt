package com.mammoth.huarongdao.domain.model

import kotlinx.serialization.Serializable

// Board is 4 columns x 5 rows
const val BOARD_COLS = 4
const val BOARD_ROWS = 5

enum class PieceType {
    CAO_CAO,      // 曹操 - 2x2
    GUAN_YU,      // 关羽 - 1x2 (horizontal)
    ZHANG_FEI,    // 张飞 - 2x1 (vertical)
    ZHAO_YUN,     // 赵云 - 2x1 (vertical)
    HUANG_ZHONG,  // 黄忠 - 2x1 (vertical)
    MA_CHAO,      // 马超 - 2x1 (vertical)
    SOLDIER,      // 卒   - 1x1
    EMPTY         // empty cell
}

@Serializable
data class Piece(
    val id: Int,
    val type: PieceType,
    val col: Int,     // left column (0-based)
    val row: Int,     // top row (0-based)
) {
    val width: Int get() = when (type) {
        PieceType.CAO_CAO -> 2
        PieceType.GUAN_YU -> 2
        PieceType.ZHANG_FEI, PieceType.ZHAO_YUN,
        PieceType.HUANG_ZHONG, PieceType.MA_CHAO -> 1
        PieceType.SOLDIER -> 1
        PieceType.EMPTY -> 1
    }
    val height: Int get() = when (type) {
        PieceType.CAO_CAO -> 2
        PieceType.GUAN_YU -> 1
        PieceType.ZHANG_FEI, PieceType.ZHAO_YUN,
        PieceType.HUANG_ZHONG, PieceType.MA_CHAO -> 2
        PieceType.SOLDIER -> 1
        PieceType.EMPTY -> 1
    }
}

@Serializable
data class GameState(
    val pieces: List<Piece>,
    val moveCount: Int = 0
) {
    fun isSolved(): Boolean {
        val caoCao = pieces.find { it.type == PieceType.CAO_CAO }
        // Cao Cao must be at col=1, row=3 (exit at bottom center)
        return caoCao?.col == 1 && caoCao.row == 3
    }

    fun toBoard(): Array<IntArray> {
        val board = Array(BOARD_ROWS) { IntArray(BOARD_COLS) { -1 } }
        pieces.forEachIndexed { idx, piece ->
            for (r in piece.row until piece.row + piece.height) {
                for (c in piece.col until piece.col + piece.width) {
                    if (r in 0 until BOARD_ROWS && c in 0 until BOARD_COLS) {
                        board[r][c] = idx
                    }
                }
            }
        }
        return board
    }

    fun encodeState(): String {
        // Encode as sorted piece positions for BFS state key
        return pieces.sortedBy { it.id }
            .joinToString(",") { "${it.col}:${it.row}" }
    }
}

data class Move(
    val pieceId: Int,
    val dCol: Int,
    val dRow: Int
)

@Serializable
data class Level(
    val id: Int,
    val nameZh: String,
    val nameEn: String,
    val initialState: GameState,
    val difficulty: Int = 1  // 1-5 stars
)

data class LevelProgress(
    val levelId: Int,
    val savedState: GameState?,
    val bestMoves: Int?,
    val isCompleted: Boolean
)
