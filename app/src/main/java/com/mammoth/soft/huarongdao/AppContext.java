package com.mammoth.soft.huarongdao;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import com.mammoth.soft.huarongdao.utils.AppDatabase;
import com.mammoth.soft.huarongdao.utils.CommonFuncsUtils;

public class AppContext extends Application {
    public static String sharedPF = "HrdSharedPreferences";
    public static String FIRSTSTART = "isFirstInApp";
    public static String LEVEL_UNLOCK = "level_unlock";
    public static String MUSIC = "Music";
    public static String SOUND = "Sound";
    public static String MODE_DAY_NIGHT = "mode_day_night";
//    public static SharedPreferences sp = null;
    public static String player = "";
//    private SoundPool soundpool;
//    private int id;
    private static AppDatabase db;
    private static boolean bNight = false;

    @Override
    public void onCreate() {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "hrd_db").build();
        bNight = CommonFuncsUtils.getDayNightModeSet(this,false);
        super.onCreate();
    }
    static public boolean getDayNightMode(){
        return bNight;
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
