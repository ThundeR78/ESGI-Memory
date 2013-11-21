package fr.esgi.android.project.esgi_memory;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ESGIMemoryApp extends Application {
	
	private static final String TAG = "ESGIMemoryApp";
	
	public static String DATABASE_NAME = "esgimemory_db";
	
	public static String ESGIMEMORY_PREFS = "ESGIMEMORY_PREFERENCES";
	
	public static final int TIMER_EASY = 30;
	public static final int TIMER_NORMAL = 45;
	public static final int TIMER_HARD = 60;
	
	public static final String KEY_LEVEL = "KEY_LEVEL";
	public static final int KEY_LEVEL_EASY = 1;
	public static final int KEY_LEVEL_NORMAL = 2;
	public static final int KEY_LEVEL_HARD = 3;
	public static final String KEY_TIMER = "KEY_TIMER";
	
	public static SimpleDateFormat dateWebServiceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public static SimpleDateFormat datetimeShortFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	public static SimpleDateFormat datetimeLabelFormat = new SimpleDateFormat("HH:mm 'le' dd/MM/yyyy");
	public static SimpleDateFormat timedateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
	public static NumberFormat numberFormat = NumberFormat.getInstance();
	
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
