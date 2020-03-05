package com.mammoth.soft.huarongdao;

import android.app.Application;
import android.content.Context;
import android.media.SoundPool;

import androidx.room.Room;

import com.mammoth.soft.huarongdao.utils.AppDatabase;

public class AppContext extends Application {
    public static String sharedPF = "HrdSharedPreferences";
//    public static String FIRSTSTART = "FirstStart";
//    public static String PLAYNAME = "PlayName";
    public static String MUSIC = "Music";
    public static String SOUND = "Sound";
//    public static SharedPreferences sp = null;
    public static String player = "";
//    private SoundPool soundpool;
//    private int id;
    private static AppDatabase db;

    @Override
    public void onCreate() {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "hrd_db").build();
        super.onCreate();
    }

    static public AppDatabase getGameDatabase(Context context){
        if(db == null){
            db = Room.databaseBuilder(context,
                    AppDatabase.class, "hrd_db").build();
        }
        return db;
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }
}
