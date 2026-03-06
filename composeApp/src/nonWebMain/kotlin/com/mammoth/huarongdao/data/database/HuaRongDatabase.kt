package com.mammoth.huarongdao.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.mammoth.huarongdao.domain.model.GameState

// ==================== Entity ====================

@Entity(tableName = "level_progress")
data class LevelProgressEntity(
    @PrimaryKey val levelId: Int,
    val savedStateJson: String?,   // serialized GameState
    val bestMoves: Int?,
    val isCompleted: Boolean
)

// ==================== DAO ====================

@Dao
interface LevelProgressDao {

    @Query("SELECT * FROM level_progress")
    fun getAllProgress(): Flow<List<LevelProgressEntity>>

    @Query("SELECT * FROM level_progress WHERE levelId = :levelId")
    suspend fun getProgress(levelId: Int): LevelProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: LevelProgressEntity)

    @Query("UPDATE level_progress SET savedStateJson = :stateJson WHERE levelId = :levelId")
    suspend fun updateSavedState(levelId: Int, stateJson: String)

    @Query("UPDATE level_progress SET bestMoves = :moves, isCompleted = 1 WHERE levelId = :levelId")
    suspend fun markCompleted(levelId: Int, moves: Int)

    @Query("DELETE FROM level_progress WHERE levelId = :levelId")
    suspend fun resetProgress(levelId: Int)
}

// ==================== Database ====================

@Database(
    entities = [LevelProgressEntity::class],
    version = 1,
    exportSchema = true
)
abstract class HuaRongDatabase : RoomDatabase() {
    abstract fun levelProgressDao(): LevelProgressDao
}

// ==================== Type Converters ====================

class GameStateConverter {
    private val json = Json { ignoreUnknownKeys = true }

    fun gameStateToString(state: GameState?): String? =
        state?.let { json.encodeToString(it) }

    fun stringToGameState(str: String?): GameState? =
        str?.let { json.decodeFromString(it) }
}
