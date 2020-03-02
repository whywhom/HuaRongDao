package com.whywhom.soft.huarongdao.utils;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by wuhaoyong on 02-03-2020.
 */
@Dao
public interface  GameHRDDao {
    @Query("SELECT * FROM hrd")
    List<GameHRD> getAll();

    /**
     * Update GameHRD
     */
    @Update
    void updateGame(GameHRD gameHRD);

    /**
     * Insert GameHRD
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGame(GameHRD gameHRD);
}
