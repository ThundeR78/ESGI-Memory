package fr.esgi.android.project.esgi_memory;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class ESGIMemoryApp extends Application {
	
	private static final String TAG = "ESGIMemoryApp";
	
	public static String DATABASE_NAME = "esgimemory_db";
	
	//GAME TIMER
	public static final int TIMER_EASY = 45;
	public static final int TIMER_NORMAL = 50;
	public static final int TIMER_HARD = 60;
	
	//KEY INTENT
	public static final String KEY_LEVEL = "KEY_LEVEL";
	public static final int KEY_LEVEL_EASY = 1;
	public static final int KEY_LEVEL_NORMAL = 2;
	public static final int KEY_LEVEL_HARD = 3;
	public static final String KEY_TIMER = "KEY_TIMER";
	public static final String KEY_MOVE = "KEY_MOVE";
	public static final String KEY_GAME_FINISHED = "KEY_GAME_FINISHED";
	public static final String KEY_HAS_TIMER = "KEY_HAS_TIMER";
	public static final String KEY_TIME_TOTAL = "KEY_TIME_TOTAL";
	public static final String KEY_TIME_MS = "KEY_DELAY_TICK";
	public static final String KEY_DELAY_TICK = "KEY_DELAY_TICK";
	public static final String KEY_TIME_BLINK_MS = "KEY_TIME_BLINK_MS";
	public static final String KEY_BLINK = "KEY_BLINK";
	public static final String KEY_LIST_IMAGEID = "KEY_LIST_IMAGEID";
	public static final String KEY_PAIR_FOUND = "KEY_PAIR_FOUND";
	public static final String KEY_FIRST_CARD = "KEY_FIRST_CARD";
	public static final String KEY_IN_ANIMATION = "KEY_IN_ANIMATION";
	public static final String KEY_SAME_CARD = "KEY_SAME_CARD";
	public static final String KEY_HAS_SOUND = "KEY_HAS_SOUND";
	
	//KEY PREFERENCES
	public static final String PREFS_APP = "ESGI_MEMORY_PREFERENCES";
	public static final String PREF_USERNAME = "PREF_USERNAME";
	public static final String PREF_HAS_SOUND = "PREF_HAS_SOUND";
	public static final String PREF_LEVEL = "PREF_LEVEL";
	public static final String PREF_TIMER = "PREF_TIMER";
	
	
	private static SQLiteDatabase db;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	//Get access to the Database
//	public static SQLiteDatabase getDB(Context context) {
//		if (null == db) {
//			DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME, null);
//			db = helper.getWritableDatabase();
//		}
//		return db;
//	}
//	
//	//Recreate Database
//	public static void recreateDB(Context context) {
//		DaoMaster.dropAllTables(ESGIMemoryApp.getDB(context), true);
//		DaoMaster.createAllTables(ESGIMemoryApp.getDB(context), true);
//	}
	
}
