package fr.esgi.android.project.esgi_memory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class LevelActivity extends Activity {
	
	public static final String TAG = "LevelActivity";
	
	private CheckBox checkboxChrono;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);
		
		checkboxChrono = (CheckBox) findViewById(R.id.checkboxChrono);
	}
	
	public void goPlayGame(View v) {
		Intent intent = new Intent(this, GameActivity.class);
		
		//Send Level selected
		switch(v.getId()){
	        case R.id.buttonEasy:
	        	intent.putExtra(ESGIMemoryApp.KEY_LEVEL, ESGIMemoryApp.KEY_LEVEL_EASY);
	        break;
	        case R.id.buttonNormal:
	        	intent.putExtra(ESGIMemoryApp.KEY_LEVEL, ESGIMemoryApp.KEY_LEVEL_NORMAL);
	        break;
	        case R.id.buttonHard:
	        	intent.putExtra(ESGIMemoryApp.KEY_LEVEL, ESGIMemoryApp.KEY_LEVEL_HARD);
	        break;
		}
		
		intent.putExtra(ESGIMemoryApp.KEY_CHRONO, checkboxChrono.isChecked());
     
		startActivity(intent);
	}

}
