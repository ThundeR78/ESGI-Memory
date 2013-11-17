package fr.esgi.android.project.esgi_memory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LevelActivity extends Activity {

	public static final String KEY_LEVEL = "KEY_LEVEL";
	public static final int KEY_LEVEL_EASY = 1;
	public static final int KEY_LEVEL_NORMAL = 2;
	public static final int KEY_LEVEL_HARD = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);
	}
	
	public void goPlayGame(View v) {
		Intent intent = new Intent(this, GameActivity.class);
		
		//Send Level selected
		switch(v.getId()){
	        case R.id.buttonEasy:
	        	intent.putExtra(KEY_LEVEL, KEY_LEVEL_EASY);
	        break;
	        case R.id.buttonNormal:
	        	intent.putExtra(KEY_LEVEL, KEY_LEVEL_NORMAL);
	        break;
	        case R.id.buttonHard:
	        	intent.putExtra(KEY_LEVEL, KEY_LEVEL_HARD);
	        break;
		}
     
		startActivity(intent);
	}

}
