package com.whywhom.soft.huarongdao.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.whywhom.soft.huarongdao.AppContext;
import com.whywhom.soft.huarongdao.ui.splash.SplashActivity;

import java.util.ArrayList;

/**
 * Created by wuhaoyong on 2/03/20.
 */
public class CommonFuncs {
    public static ArrayList<GameHRD> listGameHRD = new ArrayList<GameHRD>();

    public static Boolean getIsFirstInAppSPFS(Context context, boolean init) {
        SharedPreferences sharedPreferences= context.getSharedPreferences(AppContext.sharedPF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isFirstInApp",init);
    }

    public static void setIsFirstInAppSPFS(Context context, boolean value) {
        SharedPreferences sharedPreferences= context.getSharedPreferences(AppContext.sharedPF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstInApp", value);
        editor.putBoolean("Music", false);
        editor.putBoolean("Sound", false);
        editor.putBoolean("level_unlock", false);
        editor.commit();
    }

    public static boolean getMusicSet(Context context, boolean b) {
        SharedPreferences sharedPreferences= context.getSharedPreferences(AppContext.sharedPF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(AppContext.MUSIC, b);
    }

    public static boolean getSoundSet(Context context, boolean b) {
        SharedPreferences sharedPreferences= context.getSharedPreferences(AppContext.sharedPF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(AppContext.SOUND, b);
    }
}
