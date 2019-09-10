package com.whywhom.soft.huarongdao;

import android.app.Application;
import android.content.SharedPreferences;
import android.media.SoundPool;

public class AppContext extends Application {
    public static String sharedPF = "HrdSharedPreferences";
    public static String FIRSTSTART = "FirstStart";
    public static String PLAYNAME = "PlayName";
    public static String MUSIC = "Music";
    public static String SOUND = "Sound";
    public static SharedPreferences sp = null;
    public static String player = "";
    private SoundPool soundpool;
    private int id;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        sp = getSharedPreferences(AppContext.sharedPF, MODE_PRIVATE);

        if(sp.getBoolean("FirstStart", true)){
            SharedPreferences.Editor editor = sp.edit();
            //editor.putBoolean("FirstStart", false);
            editor.putBoolean("Music", false);
            editor.putBoolean("Sound", false);
            editor.putBoolean("level_unlock", false);
            editor.commit();
        }
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }
}
