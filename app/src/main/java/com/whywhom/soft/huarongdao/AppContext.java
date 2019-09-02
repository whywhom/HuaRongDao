package com.whywhom.soft.huarongdao;

import com.whywhom.soft.huarongdao.database.DatabaseHelper;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.SoundPool;

public class AppContext extends Application{
	public static String sharedPF = "HrdSharedPreferences";
	public static String FIRSTSTART = "FirstStart";
	public static String PLAYNAME = "PlayName";
	public static String MUSIC = "Music";
	public static String SOUND = "Sound";
	public static SharedPreferences sp = null;
	public static DatabaseHelper dbHelper;
	public static String player = "";
	private SoundPool soundpool;
	private int id;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		sp = getSharedPreferences(AppContext.sharedPF, MODE_PRIVATE);
		dbHelper = DatabaseHelper.getInstance(getApplicationContext());
		if(sp.getBoolean("FirstStart", true)){
			Editor editor = sp.edit();
	        //editor.putBoolean("FirstStart", false);
	        editor.putBoolean("Music", false);
	        editor.putBoolean("Sound", false);
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
