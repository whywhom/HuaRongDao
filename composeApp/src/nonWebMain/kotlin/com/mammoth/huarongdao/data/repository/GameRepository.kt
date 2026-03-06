package com.mammoth.huarongdao.data.repository

import com.mammoth.huarongdao.data.database.HuaRongDatabase
import com.mammoth.huarongdao.data.database.LevelProgressEntity
import com.mammoth.huarongdao.data.database.GameStateConverter
import com.mammoth.huarongdao.domain.model.GameState
import com.mammoth.huarongdao.domain.model.Level
import com.mammoth.huarongdao.domain.model.LevelData
import com.mammoth.huarongdao.domain.model.LevelProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameRepository(private val database: HuaRongDatabase) {

    private val dao = database.levelProgressDao()
    private val converter = GameStateConverter()
    private val json = Json { ignoreUnknownKeys = true }

    fun getAllLevels(): List<Level> = LevelData.levels

    fun getAllProgress(): Flow<List<LevelProgress>> {
        return dao.getAllProgress().map { entities ->
            LevelData.levels.map { level ->
                val entity = entities.find { it.levelId == level.id }
                LevelProgress(
                    levelId = level.id,
                    savedState = entity?.savedStateJson?.let { converter.stringToGameState(it) },
                    bestMoves = entity?.bestMoves,
                    isCompleted = entity?.isCompleted ?: false
                )
            }
        }
    }

    suspend fun getProgress(levelId: Int): LevelProgress {
        val entity = dao.getProgress(levelId)
        return LevelProgress(
            levelId = levelId,
            savedState = entity?.savedStateJson?.let { converter.stringToGameState(it) },
            bestMoves = entity?.bestMoves,
            isCompleted = entity?.isCompleted ?: false
        )
    }

    suspend fun saveGameState(levelId: Int, state: GameState) {
        val stateJson = json.encodeToString(state)
        val existing = dao.getProgress(levelId)
        if (existing == null) {
            dao.upsertProgress(
                LevelProgressEntity(
                    levelId = levelId,
                    savedStateJson = stateJson,
                    bestMoves = null,
                    isCompleted = false
                )
            )
        } else {
            dao.updateSavedState(levelId, stateJson)
        }
    }

    suspend fun markLevelCompleted(levelId: Int, moves: Int) {
        val existing = dao.getProgress(levelId)
        if (existing == null) {
            dao.upsertProgress(
                LevelProgressEntity(
                    levelId = levelId,
                    savedStateJson = null,
                    bestMoves = moves,
                    isCompleted = true
                )
            )
        } else {
            val currentBest = existing.bestMoves
            if (currentBest == null || moves < currentBest) {
                dao.markCompleted(levelId, moves)
            } else {
                dao.markCompleted(levelId, currentBest)
            }
        }
    }

    suspend fun resetLevel(levelId: Int) {
        dao.resetProgress(levelId)
    }
}
