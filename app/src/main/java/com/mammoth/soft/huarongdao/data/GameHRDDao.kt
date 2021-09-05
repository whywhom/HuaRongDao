package com.mammoth.soft.huarongdao.data

import androidx.room.*
import com.mammoth.soft.huarongdao.data.GameHRD

/**
 * Created by andy on 02-03-2020.
 */
@Dao
interface GameHRDDao {
    @get:Query("SELECT * FROM hrd")
    val all: List<GameHRD?>?

    /**
     * Update GameHRD
     */
    @Update
    fun updateGame(gameHRD: GameHRD?)

    /**
     * Insert GameHRD
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(gameHRD: GameHRD?)
}