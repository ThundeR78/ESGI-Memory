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
	
	public static SimpleDateFormat parserDateWebService = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public static SimpleDateFormat parserMonthDateWebService = new SimpleDateFormat("yyyy-MM");
	public static SimpleDateFormat parserDayDateWebService = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
	
	public static ProgressDialog progressDialog(Context context, String message) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage(message);
		
		return dialog;
	}
	
	public static void dismissDialog(Dialog dialog) {
		try {
			if (dialog != null)
				dialog.dismiss();
			dialog = null;
		} catch (Exception e) {}
	}
}
