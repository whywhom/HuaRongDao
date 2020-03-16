package com.mammoth.soft.huarongdao.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mammoth.soft.huarongdao.AppContext;

import java.util.ArrayList;

/**
 * Created by wuhaoyong on 2/03/20.
 */
public class CommonFuncsUtils {
    public static ArrayList<GameHRD> listGameHRD = new ArrayList<GameHRD>();

    public static Boolean getIsFirstInAppSPFS(Context context, boolean init) {
        SharedPreferences sharedPreferences= context.getSharedPreferences(AppContext.sharedPF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isFirstInApp",init);
    }

    public static void setIsFirstInAppSPFS(Context context, boolean value) {
        SharedPreferences sharedPreferences= context.getSharedPreferences(AppContext.sharedPF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppContext.FIRSTSTART, value);
        editor.putBoolean(AppContext.MUSIC, false);
        editor.putBoolean(AppContext.SOUND, false);
        editor.putBoolean(AppContext.MODE_DAY_NIGHT, false);
        editor.putBoolean(AppContext.LEVEL_UNLOCK, false);
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

    public static boolean getDayNightModeSet(Context context, boolean b) {
        SharedPreferences sharedPreferences= context.getSharedPreferences(AppContext.sharedPF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(AppContext.MODE_DAY_NIGHT, b);
    }
    public static void setDayNightModeSet(Context context, boolean b) {
        SharedPreferences sharedPreferences= context.getSharedPreferences(AppContext.sharedPF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppContext.MODE_DAY_NIGHT, b);
        editor.commit();
    }
}
