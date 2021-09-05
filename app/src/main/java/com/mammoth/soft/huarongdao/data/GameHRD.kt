package com.mammoth.soft.huarongdao.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "hrd")
data class GameHRD(
    @PrimaryKey
    @ColumnInfo(name = "hrd_id")
    val hId: Int,
    @ColumnInfo(name = "hrd_name")
    val hName: String,
    @ColumnInfo(name = "hrd_islock")
    val hLocked: Boolean,
    @ColumnInfo(name = "hrd_map")
    val map: String,
    @ColumnInfo(name = "hrd_currentmap")
    val currentMap: String,
    @ColumnInfo(name = "hrd_step")
    val step: Int = 0,
    @ColumnInfo(name = "hrd_record")
    val record: Int = -1
)
