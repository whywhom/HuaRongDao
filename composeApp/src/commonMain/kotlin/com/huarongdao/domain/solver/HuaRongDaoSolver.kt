package com.huarongdao.domain.solver

import com.huarongdao.domain.model.*

/**
 * BFS (Breadth-First Search) solver for Hua Rong Dao / Klotski puzzle.
 * Finds the shortest solution (minimum moves) from the current state to solved state.
 */
object HuaRongDaoSolver {

    data class SolverResult(
        val moves: List<Move>,
        val states: List<GameState>
    )

    fun solve(initialState: GameState): SolverResult? {
        if (initialState.isSolved()) return SolverResult(emptyList(), listOf(initialState))

        val visited = HashSet<String>()
        // Queue stores: current state + moves taken to reach it
        val queue = ArrayDeque<Pair<GameState, List<Move>>>()

        val initialKey = initialState.encodeState()
        visited.add(initialKey)
        queue.add(Pair(initialState, emptyList()))

        while (queue.isNotEmpty()) {
            val (state, moves) = queue.removeFirst()

            for (move in getPossibleMoves(state)) {
                val newState = applyMove(state, move)
                val key = newState.encodeState()

                if (key !in visited) {
                    val newMoves = moves + move
                    if (newState.isSolved()) {
                        // Build state list for animation
                        val states = buildStateList(initialState, newMoves)
                        return SolverResult(newMoves, states)
                    }
                    visited.add(key)
                    queue.add(Pair(newState, newMoves))
                }
            }
        }
        return null
    }

    private fun buildStateList(initial: GameState, moves: List<Move>): List<GameState> {
        val states = mutableListOf(initial)
        var current = initial
        for (move in moves) {
            current = applyMove(current, move)
            states.add(current)
        }
        return states
    }

    private fun getPossibleMoves(state: GameState): List<Move> {
        val board = state.toBoard()
        val moves = mutableListOf<Move>()
        val directions = listOf(Pair(0, -1), Pair(0, 1), Pair(-1, 0), Pair(1, 0))

        for (piece in state.pieces) {
            for ((dc, dr) in directions) {
                if (canMove(board, piece, dc, dr)) {
                    moves.add(Move(piece.id, dc, dr))
                }
            }
        }
        return moves
    }

    fun canMove(board: Array<IntArray>, piece: Piece, dc: Int, dr: Int): Boolean {
        val newCol = piece.col + dc
        val newRow = piece.row + dr

        // Check bounds
        if (newCol < 0 || newRow < 0) return false
        if (newCol + piece.width > BOARD_COLS) return false
        if (newRow + piece.height > BOARD_ROWS) return false

        // Check if target cells are empty (not occupied by another piece)
        for (r in newRow until newRow + piece.height) {
            for (c in newCol until newCol + piece.width) {
                val occupant = board[r][c]
                // Cell must be empty OR occupied by the same piece
                val pieceIdx = findPieceIndex(board, piece)
                if (occupant != -1 && occupant != pieceIdx) return false
            }
        }
        return true
    }

    private fun findPieceIndex(board: Array<IntArray>, piece: Piece): Int {
        if (piece.row < BOARD_ROWS && piece.col < BOARD_COLS) {
            return board[piece.row][piece.col]
        }
        return -1
    }

    fun applyMove(state: GameState, move: Move): GameState {
        val newPieces = state.pieces.map { piece ->
            if (piece.id == move.pieceId) {
                piece.copy(col = piece.col + move.dCol, row = piece.row + move.dRow)
            } else piece
        }
        return state.copy(pieces = newPieces, moveCount = state.moveCount + 1)
    }

    fun getPossibleMovesForPiece(state: GameState, pieceId: Int): List<Move> {
        val board = state.toBoard()
        val piece = state.pieces.find { it.id == pieceId } ?: return emptyList()
        val directions = listOf(Pair(0, -1), Pair(0, 1), Pair(-1, 0), Pair(1, 0))
        return directions
            .filter { (dc, dr) -> canMove(board, piece, dc, dr) }
            .map { (dc, dr) -> Move(pieceId, dc, dr) }
    }
}
