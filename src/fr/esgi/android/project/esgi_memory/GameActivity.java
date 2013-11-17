package fr.esgi.android.project.esgi_memory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class GameActivity extends Activity {
	
	private int level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		level = getIntent().getIntExtra(LevelActivity.KEY_LEVEL, LevelActivity.KEY_LEVEL_NORMAL);
		
		Log.i(LevelActivity.KEY_LEVEL, "Level= "+level);
	}
	
	
}
