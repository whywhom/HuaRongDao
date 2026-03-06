package com.mammoth.huarongdao.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mammoth.huarongdao.data.repository.GameRepository
import com.mammoth.huarongdao.domain.solver.HuaRongDaoSolver
import com.mammoth.huarongdao.domain.model.GameState
import com.mammoth.huarongdao.domain.model.Level
import com.mammoth.huarongdao.domain.model.Move
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// ==================== MVI Contract ====================

data class GameUiState(
    val level: Level? = null,
    val currentState: GameState? = null,
    val selectedPieceId: Int? = null,
    val isLoading: Boolean = false,
    val isSolved: Boolean = false,
    val isDemoMode: Boolean = false,
    val demoStates: List<GameState> = emptyList(),
    val demoCurrentIndex: Int = 0,
    val isSolving: Boolean = false,
    val noSolutionFound: Boolean = false,
    val error: String? = null
)

sealed class GameIntent {
    data class LoadLevel(val levelId: Int) : GameIntent()
    data class SelectPiece(val pieceId: Int) : GameIntent()
    data class MovePiece(val dCol: Int, val dRow: Int) : GameIntent()
    data class DragPiece(val pieceId: Int, val dCol: Int, val dRow: Int) : GameIntent()
    object UnselectPiece : GameIntent()
    object ResetLevel : GameIntent()
    object RequestDemo : GameIntent()
    object StopDemo : GameIntent()
    object NextDemoStep : GameIntent()
    object DismissError : GameIntent()
}

sealed class GameEffect {
    object LevelCompleted : GameEffect()
    object PieceMoved : GameEffect()
    data class ShowMessage(val message: String) : GameEffect()
}

// ==================== ViewModel ====================

class GameViewModel(
    private val repository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<GameEffect>()
    val effect: SharedFlow<GameEffect> = _effect.asSharedFlow()

    private var demoJob: Job? = null

    fun handleIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.LoadLevel -> loadLevel(intent.levelId)
            is GameIntent.SelectPiece -> selectPiece(intent.pieceId)
            is GameIntent.MovePiece -> moveSelectedPiece(intent.dCol, intent.dRow)
            is GameIntent.DragPiece -> dragPiece(intent.pieceId, intent.dCol, intent.dRow)
            is GameIntent.UnselectPiece -> _uiState.update { it.copy(selectedPieceId = null) }
            is GameIntent.ResetLevel -> resetLevel()
            is GameIntent.RequestDemo -> startDemo()
            is GameIntent.StopDemo -> stopDemo()
            is GameIntent.NextDemoStep -> advanceDemoStep()
            is GameIntent.DismissError -> _uiState.update { it.copy(error = null, noSolutionFound = false) }
        }
    }

    private fun loadLevel(levelId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val level = repository.getAllLevels().find { it.id == levelId }
            if (level == null) {
                _uiState.update { it.copy(isLoading = false, error = "Level not found") }
                return@launch
            }
            val progress = repository.getProgress(levelId)
            val state = progress.savedState ?: level.initialState
            _uiState.update {
                it.copy(
                    isLoading = false,
                    level = level,
                    currentState = state,
                    isSolved = state.isSolved()
                )
            }
        }
    }

    private fun selectPiece(pieceId: Int) {
        val current = _uiState.value.selectedPieceId
        _uiState.update {
            it.copy(selectedPieceId = if (current == pieceId) null else pieceId)
        }
    }

    private fun moveSelectedPiece(dCol: Int, dRow: Int) {
        val state = _uiState.value
        val pieceId = state.selectedPieceId ?: return
        val currentState = state.currentState ?: return
        val piece = currentState.pieces.find { it.id == pieceId } ?: return
        val board = currentState.toBoard()

        if (!HuaRongDaoSolver.canMove(board, piece, dCol, dRow)) return

        val newState = HuaRongDaoSolver.applyMove(currentState, Move(pieceId, dCol, dRow))
        handleNewState(newState)
    }

    private fun dragPiece(pieceId: Int, dCol: Int, dRow: Int) {
        val currentState = _uiState.value.currentState ?: return
        val piece = currentState.pieces.find { it.id == pieceId } ?: return
        val board = currentState.toBoard()

        if (!HuaRongDaoSolver.canMove(board, piece, dCol, dRow)) return

        val newState = HuaRongDaoSolver.applyMove(currentState, Move(pieceId, dCol, dRow))
        handleNewState(newState)
    }

    private fun handleNewState(newState: GameState) {
        val levelId = _uiState.value.level?.id ?: return
        _uiState.update { it.copy(currentState = newState) }

        viewModelScope.launch {
            _effect.emit(GameEffect.PieceMoved)
            repository.saveGameState(levelId, newState)

            if (newState.isSolved()) {
                repository.markLevelCompleted(levelId, newState.moveCount)
                _uiState.update { it.copy(isSolved = true) }
                _effect.emit(GameEffect.LevelCompleted)
            }
        }
    }

    private fun resetLevel() {
        val levelId = _uiState.value.level?.id ?: return
        viewModelScope.launch {
            repository.resetLevel(levelId)
            loadLevel(levelId)
        }
    }

    private fun startDemo() {
        val state = _uiState.value.currentState ?: return
        _uiState.update { it.copy(isSolving = true, selectedPieceId = null) }

        viewModelScope.launch(Dispatchers.Default) {
            val result = HuaRongDaoSolver.solve(state)
            withContext(Dispatchers.Main) {
                if (result == null) {
                    _uiState.update { it.copy(isSolving = false, noSolutionFound = true) }
                } else {
                    _uiState.update {
                        it.copy(
                            isSolving = false,
                            isDemoMode = true,
                            demoStates = result.states,
                            demoCurrentIndex = 0
                        )
                    }
                    playDemoAnimation()
                }
            }
        }
    }

    private fun playDemoAnimation() {
        demoJob?.cancel()
        demoJob = viewModelScope.launch {
            val states = _uiState.value.demoStates
            for (i in states.indices) {
                _uiState.update {
                    it.copy(
                        demoCurrentIndex = i,
                        currentState = states[i]
                    )
                }
                if (i < states.size - 1) delay(600L)
            }
            // Demo finished
            _uiState.update { it.copy(isDemoMode = false) }
            val lastState = states.lastOrNull()
            if (lastState?.isSolved() == true) {
                _effect.emit(GameEffect.LevelCompleted)
            }
        }
    }

    private fun stopDemo() {
        demoJob?.cancel()
        _uiState.update {
            it.copy(
                isDemoMode = false,
                demoStates = emptyList(),
                demoCurrentIndex = 0
            )
        }
        // Restore to initial state
        val levelId = _uiState.value.level?.id ?: return
        loadLevel(levelId)
    }

    private fun advanceDemoStep() {
        val state = _uiState.value
        if (!state.isDemoMode) return
        val nextIdx = state.demoCurrentIndex + 1
        if (nextIdx >= state.demoStates.size) {
            stopDemo()
            return
        }
        _uiState.update {
            it.copy(
                demoCurrentIndex = nextIdx,
                currentState = it.demoStates[nextIdx]
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        demoJob?.cancel()
    }
}
