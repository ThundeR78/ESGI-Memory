package fr.esgi.android.project.esgi_memory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	public void goPlayGame(View v) {
		Intent intent = new Intent(this, LevelActivity.class);
		startActivity(intent);
	}
	
	public void goQuickGame(View v) {
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}

	public void goScores(View v) {
		Intent intent = new Intent(this, ScoreListActivity.class);
		startActivity(intent);
	}
	
	public void goSettings(View v) {
//		Intent intent = new Intent(this, SettingsActivity.class);
//		startActivity(intent);
	}
}
