package com.mammoth.huarongdao.data.database

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<HuaRongDatabase>
