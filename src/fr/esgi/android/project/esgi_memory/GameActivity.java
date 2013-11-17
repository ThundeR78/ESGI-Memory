package fr.esgi.android.project.esgi_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;

public class GameActivity extends Activity {
	
	private int level;
	private List<Integer> listImageIDs = new ArrayList<Integer>();
	
	private GridView gridview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_easy);
		
		level = getIntent().getIntExtra(LevelActivity.KEY_LEVEL, LevelActivity.KEY_LEVEL_NORMAL);
		
		Log.i(LevelActivity.KEY_LEVEL, "Level= "+level);
		
		gridview = (GridView) findViewById(R.id.gridView);
		//Avoid Scroll
		gridview.setOnTouchListener(new OnTouchListener(){
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        if(event.getAction() == MotionEvent.ACTION_MOVE)
		            return true;
		        return false;
		    }
		});
		
		//Load array imagesId
		TypedArray images;
		if (level == LevelActivity.KEY_LEVEL_EASY)
			images = getResources().obtainTypedArray(R.array.images_easy);
		else if (level == LevelActivity.KEY_LEVEL_HARD)
			images = getResources().obtainTypedArray(R.array.images_hard);
		else 
			images = getResources().obtainTypedArray(R.array.images_normal);
			
		//Transfer array imagesId into List
		for (int i=0; i<images.length() ;i++) {
			listImageIDs.add(images.getResourceId(i, 0));
		}
		
		//Shuffle List imagesId
		Log.v("BEFORE SHUFFLE", listImageIDs.toString() );
		Collections.shuffle(listImageIDs); 
		Log.v("AFTER SHUFFLE", listImageIDs.toString() );

	}
	
	
}
