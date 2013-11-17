package fr.esgi.android.project.esgi_memory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LevelActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);
	}
	
	public void goPlayEasy(View v) {
		Intent intent = new Intent(this, GameActivity.class);
//		intent.putExtra(KEY_LEVEL, KEY_LEVEL_EASY);
		startActivity(intent);
	}
	
	public void goPlayNormal(View v) {
		Intent intent = new Intent(this, GameActivity.class);
//		intent.putExtra(KEY_LEVEL, KEY_LEVEL_NORMAL);
		startActivity(intent);
	}
	
	public void goPlayHard(View v) {
		Intent intent = new Intent(this, GameActivity.class);
//		intent.putExtra(KEY_LEVEL, KEY_LEVEL_HARD);
		startActivity(intent);
	}
	
}
