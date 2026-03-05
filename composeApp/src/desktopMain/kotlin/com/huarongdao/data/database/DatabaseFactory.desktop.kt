package com.huarongdao.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<HuaRongDatabase> {
    val dbFile = File(System.getProperty("user.home"), ".huarongdao/huarongdao.db").also {
        it.parentFile?.mkdirs()
    }
    return Room.databaseBuilder<HuaRongDatabase>(name = dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
}
