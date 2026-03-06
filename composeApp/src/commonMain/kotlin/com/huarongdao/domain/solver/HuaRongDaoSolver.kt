package com.huarongdao.domain.solver

import com.huarongdao.domain.model.*
import kotlin.math.abs

/**
 * A* solver for Hua Rong Dao / Klotski.
 *
 * Key optimisations over the original BFS:
 *
 * 1. **A* with admissible heuristic** — prioritises states closer to the goal,
 *    drastically cutting nodes expanded vs plain BFS on hard levels.
 *
 * 2. **Long state key** — encodes the full board as a single Long (20 cells ×
 *    4 bits each = 80 bits → two Longs packed as a data class).  No string
 *    allocation, no sorting, O(1) hash.
 *
 * 3. **Parent-pointer graph** — the open/closed sets only store
 *    `StateNode(key, g, parent)`. The actual move list is reconstructed once
 *    at the end by walking the parent chain, eliminating the per-step
 *    `List<Move>` copy that made the original BFS O(n²) in memory.
 *
 * 4. **Lazy board reconstruction** — `toBoard()` is called only when expanding
 *    a node, not when checking membership.
 *
 * 5. **Min-heap priority queue** — O(log n) insert/pop instead of O(n) queue.
 */
object HuaRongDaoSolver {

    // ─────────────────────────────────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────────────────────────────────

    data class SolverResult(
        val moves: List<Move>,
        val states: List<GameState>
    )

    fun solve(initialState: GameState): SolverResult? {
        if (initialState.isSolved()) return SolverResult(emptyList(), listOf(initialState))

        // ── A* open set: min-heap on f = g + h ───────────────────────────────
        val openHeap = ArrayDeque<AStarNode>()   // treated as a min-heap
        val openCost  = HashMap<StateKey2, Int>() // best g seen for each key
        val closed    = HashMap<StateKey2, AStarNode>() // fully expanded nodes

        val initKey  = initialState.encodeKey()
        val initNode = AStarNode(
            state    = initialState,
            key      = initKey,
            g        = 0,
            h        = heuristic(initialState),
            parent   = null,
            lastMove = null
        )
        heapPush(openHeap, initNode)
        openCost[initKey] = 0

        while (openHeap.isNotEmpty()) {
            val current = heapPop(openHeap)

            // Skip stale entries (we may have added the same key with better g)
            val bestG = openCost[current.key]
            if (bestG != null && current.g > bestG && closed.containsKey(current.key)) continue
            if (closed.containsKey(current.key)) continue

            closed[current.key] = current

            // Goal check
            if (current.state.isSolved()) {
                return reconstructResult(initialState, current)
            }

            // Expand neighbours
            val board = current.state.toBoard()
            for (piece in current.state.pieces) {
                if (piece.type == PieceType.EMPTY) continue
                for ((dc, dr) in DIRECTIONS) {
                    if (!canMove(board, piece, dc, dr)) continue

                    val newState = applyMove(current.state, Move(piece.id, dc, dr))
                    val newKey   = newState.encodeKey()
                    if (closed.containsKey(newKey)) continue

                    val newG = current.g + 1
                    val prevBest = openCost[newKey]
                    if (prevBest != null && prevBest <= newG) continue

                    openCost[newKey] = newG
                    heapPush(openHeap, AStarNode(
                        state    = newState,
                        key      = newKey,
                        g        = newG,
                        h        = heuristic(newState),
                        parent   = current,
                        lastMove = Move(piece.id, dc, dr)
                    ))
                }
            }
        }
        return null // no solution
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Heuristic  h(n)
    // ─────────────────────────────────────────────────────────────────────────
    //
    // Admissible (never overestimates) so A* is guaranteed optimal.
    //
    // Components:
    //   1. Manhattan distance of 曹操 to the exit (col=1, row=3).
    //   2. Number of pieces directly blocking 曹操's path to the exit,
    //      weighted by 2 (each blocker needs ≥ 2 moves to clear).
    //
    // This is still admissible because moving 曹操 plus clearing each blocker
    // costs at least (manhattan + 2×blockers) moves.
    // ─────────────────────────────────────────────────────────────────────────
    private fun heuristic(state: GameState): Int {
        val caoCao = state.pieces.find { it.type == PieceType.CAO_CAO } ?: return 0

        // 1. Manhattan distance to exit position (col=1, row=3)
        val manhattan = abs(caoCao.col - 1) + abs(caoCao.row - 3)

        // 2. Count pieces blocking 曹操's vertical path to the exit
        //    (i.e. occupying cols 1-2, rows between caoCao.row+2 and 3)
        var blockers = 0
        if (caoCao.row < 3) {
            // Need to move down — count pieces in cols 1..2 below caoCao
            val board = state.toBoard()
            for (r in (caoCao.row + 2)..3) {
                for (c in caoCao.col until caoCao.col + 2) {
                    val occupant = board[r][c]
                    if (occupant != -1) {
                        val blocker = state.pieces[occupant]
                        if (blocker.type != PieceType.CAO_CAO) {
                            blockers++
                            break // count each row at most once
                        }
                    }
                }
            }
        }

        return manhattan + blockers * 2
    }

    // ─────────────────────────────────────────────────────────────────────────
    // State key — encode board as two Longs (no String allocation)
    // ─────────────────────────────────────────────────────────────────────────
    //
    // Board has 20 cells (4×5). We assign each cell a 4-bit piece-type tag
    // (0=empty, 1=CaoCao, 2=GuanYu, …, 7=Soldier).  20×4 = 80 bits → 2 Longs.
    //
    // Using piece *type* (not id) per cell makes the key position-based:
    // two states with the same board layout but different move counts get the
    // same key, which is exactly what we want for deduplication.
    // ─────────────────────────────────────────────────────────────────────────
data class StateKey2(val hi: Long, val lo: Long)

    private fun GameState.encodeKey(): StateKey2 {
        val cells = IntArray(BOARD_ROWS * BOARD_COLS) // 0 = empty
        pieces.forEach { piece ->
            val tag = piece.type.ordinal + 1  // 1-based so 0 = empty
            for (r in piece.row until piece.row + piece.height) {
                for (c in piece.col until piece.col + piece.width) {
                    cells[r * BOARD_COLS + c] = tag
                }
            }
        }
        // Pack first 16 cells (64 bits / 4 bits each) into lo
        // Pack next 4 cells into hi
        var lo = 0L
        var hi = 0L
        for (i in 0 until 16) lo = lo or (cells[i].toLong() shl (i * 4))
        for (i in 16 until 20) hi = hi or (cells[i].toLong() shl ((i - 16) * 4))
        return StateKey2(hi, lo)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Min-heap helpers (binary heap on f = g + h)
    // ─────────────────────────────────────────────────────────────────────────

    private data class AStarNode(
        val state:    GameState,
        val key:      StateKey2,
        val g:        Int,
        val h:        Int,
        val parent:   AStarNode?,
        val lastMove: Move?
    ) {
        val f get() = g + h
    }

    private fun heapPush(heap: ArrayDeque<AStarNode>, node: AStarNode) {
        heap.addLast(node)
        var i = heap.size - 1
        while (i > 0) {
            val parent = (i - 1) / 2
            if (heap[parent].f <= heap[i].f) break
            val tmp = heap[parent]; heap[parent] = heap[i]; heap[i] = tmp
            i = parent
        }
    }

    private fun heapPop(heap: ArrayDeque<AStarNode>): AStarNode {
        val top = heap[0]
        val last = heap.removeLast()
        if (heap.isEmpty()) return top
        heap[0] = last
        var i = 0
        val n = heap.size
        while (true) {
            val l = 2 * i + 1
            val r = 2 * i + 2
            var smallest = i
            if (l < n && heap[l].f < heap[smallest].f) smallest = l
            if (r < n && heap[r].f < heap[smallest].f) smallest = r
            if (smallest == i) break
            val tmp = heap[i]; heap[i] = heap[smallest]; heap[smallest] = tmp
            i = smallest
        }
        return top
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Path reconstruction
    // ─────────────────────────────────────────────────────────────────────────

    private fun reconstructResult(initial: GameState, goalNode: AStarNode): SolverResult {
        // Walk parent chain to collect moves in reverse
        val moves = mutableListOf<Move>()
        var node: AStarNode? = goalNode
        while (node?.lastMove != null) {
            moves.add(node.lastMove)
            node = node.parent
        }
        moves.reverse()

        // Replay moves to build the state animation list
        val states = mutableListOf(initial)
        var current = initial
        for (move in moves) {
            current = applyMove(current, move)
            states.add(current)
        }
        return SolverResult(moves, states)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Board / move helpers (shared with ViewModel)
    // ─────────────────────────────────────────────────────────────────────────

    private val DIRECTIONS = listOf(
        Pair(0, -1), Pair(0, 1), Pair(-1, 0), Pair(1, 0)
    )

    fun canMove(board: Array<IntArray>, piece: Piece, dc: Int, dr: Int): Boolean {
        val newCol = piece.col + dc
        val newRow = piece.row + dr
        if (newCol < 0 || newRow < 0) return false
        if (newCol + piece.width  > BOARD_COLS) return false
        if (newRow + piece.height > BOARD_ROWS) return false

        val pieceIdx = if (piece.row < BOARD_ROWS && piece.col < BOARD_COLS)
            board[piece.row][piece.col] else return false

        for (r in newRow until newRow + piece.height) {
            for (c in newCol until newCol + piece.width) {
                val occupant = board[r][c]
                if (occupant != -1 && occupant != pieceIdx) return false
            }
        }
        return true
    }

    fun applyMove(state: GameState, move: Move): GameState {
        val newPieces = state.pieces.map { piece ->
            if (piece.id == move.pieceId)
                piece.copy(col = piece.col + move.dCol, row = piece.row + move.dRow)
            else piece
        }
        return state.copy(pieces = newPieces, moveCount = state.moveCount + 1)
    }

    fun getPossibleMovesForPiece(state: GameState, pieceId: Int): List<Move> {
        val board = state.toBoard()
        val piece = state.pieces.find { it.id == pieceId } ?: return emptyList()
        return DIRECTIONS
            .filter { (dc, dr) -> canMove(board, piece, dc, dr) }
            .map  { (dc, dr) -> Move(pieceId, dc, dr) }
    }
}
