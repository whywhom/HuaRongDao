package com.mammoth.soft.huarongdao.data

import androidx.room.Database
import com.mammoth.soft.huarongdao.data.GameHRD
import androidx.room.RoomDatabase
import com.mammoth.soft.huarongdao.data.GameHRDDao

/**
 * Created by wuhaoyong on 2/03/20.
 */
@Database(entities = arrayOf(GameHRD::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameHRDDao(): GameHRDDao?
}