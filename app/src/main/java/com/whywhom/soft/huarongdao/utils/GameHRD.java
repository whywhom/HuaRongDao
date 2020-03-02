package com.whywhom.soft.huarongdao.utils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by wuhaoyong on 08-09-2019.
 */
@Entity (tableName = "hrd")
public class GameHRD {
    @PrimaryKey
    @ColumnInfo(name = "hrd_id")
    public int hId;
    @ColumnInfo(name = "hrd_name")
    public String hName;
    @ColumnInfo(name = "hrd_islock")
    public boolean hLocked;
    @ColumnInfo(name = "hrd_map")
    public String map;
    @ColumnInfo(name = "hrd_currentmap")
    public String currentMap;
    @ColumnInfo(name = "hrd_step")
    public int step = 0;
    @ColumnInfo(name = "hrd_record")
    public int record = -1;

    @Ignore
    private int vol = 4;//列数
    @Ignore
    private int row = 5;//行数
    @Ignore
    private int[][] chessboard = new int[vol][row];

    public GameHRD(){

    }

    public int gethId() {
        return hId;
    }

    public void sethId(int hId) {
        this.hId = hId;
    }

    public String gethMap() {
        return map;
    }

    public void sethMap(String map) {
        this.map = map;
    }

    public String gethCurrentMap() {
        return currentMap;
    }

    public void sethCurrentMap(String curmap) {
        this.currentMap = curmap;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public boolean ishLocked() {
        return hLocked;
    }

    public void sethLocked(boolean hLocked) {
        this.hLocked = hLocked;
    }

    public void setIntegerMap(int[][] chessboard){
        String map = "";
        for (int j = 0; j < vol; j++) {
            for (int k = 0; k < row; k++) {
                int value = chessboard[j][k];
                map += String.valueOf(value) + ",";
            }
        }
        this.currentMap = map;
    }
    public int[][] getIntegerMap(){
        int[][] chessboard = new int[vol][row];
        String str[] = currentMap.split(",");
        for(int i=0;i<chessboard.length;i++){
            for(int j=0;j<chessboard[i].length;j++){
                String s = str[i*row+j];
                chessboard[i][j] = Integer.parseInt(s);
            }
        }
        return chessboard;
    }

    public void sethRecord(int total_step) {
        this.record = total_step;
    }

    public int gethRecord() {
        return this.record;
    }

    public void sethStep(int i) {
        this.step = i;
    }

    public int gethStep() {
        return this.step;
    }
}
