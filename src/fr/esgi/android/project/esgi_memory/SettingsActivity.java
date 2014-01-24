package fr.esgi.android.project.esgi_memory;

import fr.esgi.android.project.esgi_memory.db.DatabaseHandler;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;

public class SettingsActivity extends PreferenceActivity {
	public static final String TAG = "SettingsActivity";
	
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_settings);
    	addPreferencesFromResource(R.xml.preferences);
    }
	
	public void DeleteAllScore(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.alert_delete_all_title);
	    builder.setMessage(R.string.alert_delete_all_message);
	    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	//Delete all scores in DB
	            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	            db.deleteAllScore();
	        	
	            dialog.dismiss();
	        }

	    });
	    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            
	            dialog.dismiss();
	        }
	    });
	    AlertDialog alert = builder.create();
	    alert.show();
	}
}
