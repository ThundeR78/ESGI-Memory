package fr.esgi.android.project.esgi_memory;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends PreferenceActivity {
	public static final String TAG = "SettingsActivity";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_settings);
    	addPreferencesFromResource(R.xml.preferences);
    }
	
	public void DeleteAllScore(View v) {
		Log.v(TAG, ((Button) v).getText().toString());
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Log.v(TAG, prefs.getString("listpref_level", "no"));
	}
}
