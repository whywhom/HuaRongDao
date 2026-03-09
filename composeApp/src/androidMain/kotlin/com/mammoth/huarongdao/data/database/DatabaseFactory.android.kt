package com.mammoth.huarongdao.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

lateinit var appContext: Context

actual fun getDatabaseBuilder(): RoomDatabase.Builder<HuaRongDatabase> {
    val dbFile = appContext.getDatabasePath("huarongdao.db")
    return Room.databaseBuilder<HuaRongDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    ).setDriver(BundledSQLiteDriver())
}
