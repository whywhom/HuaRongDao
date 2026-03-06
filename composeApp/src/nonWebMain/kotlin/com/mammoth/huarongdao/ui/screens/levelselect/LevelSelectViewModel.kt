package com.mammoth.huarongdao.ui.screens.levelselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mammoth.huarongdao.data.repository.GameRepository
import com.mammoth.huarongdao.domain.model.Level
import com.mammoth.huarongdao.domain.model.LevelProgress
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LevelSelectUiState(
    val levels: List<Level> = emptyList(),
    val progressMap: Map<Int, LevelProgress> = emptyMap(),
    val isLoading: Boolean = true
)

sealed class LevelSelectIntent {
    object Load : LevelSelectIntent()
    data class SelectLevel(val levelId: Int) : LevelSelectIntent()
}

class LevelSelectViewModel(
    private val repository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LevelSelectUiState())
    val uiState: StateFlow<LevelSelectUiState> = _uiState.asStateFlow()

    private val _navigateToLevel = MutableSharedFlow<Int>()
    val navigateToLevel: SharedFlow<Int> = _navigateToLevel.asSharedFlow()

    init {
        load()
    }

    fun handleIntent(intent: LevelSelectIntent) {
        when (intent) {
            is LevelSelectIntent.Load -> load()
            is LevelSelectIntent.SelectLevel -> {
                viewModelScope.launch {
                    _navigateToLevel.emit(intent.levelId)
                }
            }
        }
    }

    private fun load() {
        val levels = repository.getAllLevels()
        viewModelScope.launch {
            repository.getAllProgress().collect { progressList ->
                val map = progressList.associateBy { it.levelId }
                _uiState.update {
                    it.copy(levels = levels, progressMap = map, isLoading = false)
                }
            }
        }
    }
}
