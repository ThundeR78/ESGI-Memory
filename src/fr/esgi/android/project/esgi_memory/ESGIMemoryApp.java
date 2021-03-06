package fr.esgi.android.project.esgi_memory;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class ESGIMemoryApp extends Application {
	
	private static final String TAG = "ESGIMemoryApp";
	
	public static String DATABASE_NAME = "esgimemory_db";
	
	//GAME TIMER
	public static final int TIMER_EASY = 45;
	public static final int TIMER_NORMAL = 50;
	public static final int TIMER_HARD = 60;
	
	//KEY INTENT
	public static final String KEY_LEVEL = "LEVEL";
	public static final int KEY_LEVEL_EASY = 1;
	public static final int KEY_LEVEL_NORMAL = 2;
	public static final int KEY_LEVEL_HARD = 3;
	public static final String KEY_TIMER = "TIMER";
	public static final String KEY_MOVE = "MOVE";
	public static final String KEY_GAME_FINISHED = "GAME_FINISHED";
	public static final String KEY_HAS_TIMER = "HAS_TIMER";
	public static final String KEY_TIME_TOTAL = "TIME_TOTAL";
	public static final String KEY_TIME_MS = "DELAY_TICK";
	public static final String KEY_DELAY_TICK = "DELAY_TICK";
	public static final String KEY_TIME_BLINK_MS = "TIME_BLINK_MS";
	public static final String KEY_BLINK = "BLINK";
	public static final String KEY_LIST_CARD = "LIST_CARD";
	public static final String KEY_PAIR_FOUND = "PAIR_FOUND";
	public static final String KEY_FIRST_CARD = "FIRST_CARD";
	public static final String KEY_IN_ANIMATION = "IN_ANIMATION";
	public static final String KEY_SAME_CARD = "SAME_CARD";
	public static final String KEY_HAS_SOUND = "HAS_SOUND";
	public static final String KEY_GAME_WON = "GAME_WON";
	
	//KEY PREFERENCES
	public static final String PREFS_APP = "ESGI_MEMORY_PREFERENCES";
	public static final String PREF_USERNAME = "PREF_USERNAME";
	public static final String PREF_HAS_SOUND = "PREF_HAS_SOUND";
	public static final String PREF_LEVEL = "PREF_LEVEL";
	public static final String PREF_TIMER = "PREF_TIMER";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//Initialize TestFlight with your app token.
//        TestFlight.takeOff(this, "1d4ee26f-3939-4f06-aa92-60f567e43898");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	public static String getLabelLevel(Context context, int level) {
		int idString = 0;
		idString = (level == KEY_LEVEL_EASY) ? R.string.level_easy : 
			(level == KEY_LEVEL_NORMAL) ? R.string.level_normal :
			(level == KEY_LEVEL_HARD) ? R.string.level_hard : 0;
		return (idString != 0) ? context.getResources().getString(idString) : "";
	}
	
	public static void getScreenSize(Context ctx) {
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Log.v("DEVICE", "Screen size = "+display.getWidth()+"x"+display.getHeight());
	}
	
	public static void getDensityDevice(int dpi) {
		String density = (dpi == DisplayMetrics.DENSITY_LOW) ? "ldpi" : 
			(dpi == DisplayMetrics.DENSITY_MEDIUM) ? "mdpi" :
			(dpi == DisplayMetrics.DENSITY_HIGH) ? "hdpi" :
			(dpi == DisplayMetrics.DENSITY_XHIGH) ? "xhdpi" : "xxhdpi";
		
		Log.v("DEVICE", "Density = "+density);
	}
}
