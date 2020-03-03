package com.mammoth.soft.huarongdao.utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Created by wuhaoyong on 2/03/20.
 */
@Database(entities = {GameHRD.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GameHRDDao gameHRDDao();
}
