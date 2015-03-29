package com.whywhom.soft.huarongdao.database;

import java.util.ArrayList;

import com.whywhom.soft.huarongdao.util.ScoreItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	private static DatabaseHelper mInstance = null;
	public static final String DATABASE_NAME = "HrdDatabase.db";
	public static final String DATABASE_TABLE = "score";
	public static final int DATABASE_VERSION = 1;
	public static final String KEY_ID = "_id";
	public static final String KEY_LEVEL_COLUMN = "GAME_LEVEL";
	public static final String KEY_SCORE_COLUMN = "GAME_SCORE";
	public static final String KEY_PLAYER_COLUMN = "PLAYER";
	
	public static final String DATABASE_CREATE = " create table " + DATABASE_TABLE + " ( " + KEY_ID
			+ " INTEGER primary key autoincrement, " 
			+ KEY_LEVEL_COLUMN + " TEXT, "
			+ KEY_SCORE_COLUMN + " INTEGER, " 
			+ KEY_PLAYER_COLUMN + " TEXT " + " ) ";
	
	static public DatabaseHelper getInstance(Context context) {
		if (mInstance  == null) {
			mInstance = new DatabaseHelper(context);
		}
		return mInstance;
	}

	private SQLiteDatabase mDb;
	
	private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	private DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);//需要异常捕获
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// KILL PREVIOUS TABLE IF UPGRADED
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        // CREATE NEW INSTANCE OF TABLE
        onCreate(db);
	}

	public ArrayList<ScoreItem> getItems(String level) {
		int score = 0;
		String name = "";
		ArrayList<ScoreItem> fl1 = new ArrayList<ScoreItem>();
		mDb = getWritableDatabase();
		String q = "SELECT *" + " FROM " + DATABASE_TABLE + " WHERE "
				+ KEY_LEVEL_COLUMN + "=? " + " order by " +KEY_SCORE_COLUMN+ " asc" ;
		String[] whereValues = { "" + level };
		Cursor cursor = mDb.rawQuery(q, whereValues);
		if (cursor != null && cursor.moveToFirst()) {
			while(cursor.isAfterLast() == false){
				score = cursor.getInt(cursor.getColumnIndex(KEY_SCORE_COLUMN));
				name = cursor.getString(cursor.getColumnIndex(KEY_PLAYER_COLUMN));
				ScoreItem sItem = new ScoreItem(score, name, level);
				fl1.add(sItem);
				cursor.moveToNext();
			}
		}
		cursor.close();
		mDb.close();
		return fl1;
	}

	public long saveItems(ScoreItem si) {
		// TODO Auto-generated method stub
		long id = -1;
		mDb = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(KEY_SCORE_COLUMN, si.score);
		cv.put(KEY_PLAYER_COLUMN, si.name);
		cv.put(KEY_LEVEL_COLUMN, si.level);
		id = mDb.insert(DATABASE_TABLE, null, cv);
		mDb.close();
		return id;
	}
}
