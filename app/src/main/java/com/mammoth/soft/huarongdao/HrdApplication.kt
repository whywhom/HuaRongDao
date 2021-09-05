package com.mammoth.soft.huarongdao

import android.app.Application
import com.mammoth.soft.huarongdao.data.AppDatabase

import androidx.room.Room
import kotlin.properties.Delegates


class HrdApplication: Application() {
    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "hrd_db"
        ).build()
    }

    companion object{
        private var instance: HrdApplication by Delegates.notNull()
        fun instance() = instance
    }
}