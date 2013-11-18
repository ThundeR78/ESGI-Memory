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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import fr.esgi.android.project.esgi_memory.view.ImageAdapter;

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
		//Add pair of images Id
		listImageIDs.addAll(listImageIDs);
		
		//Shuffle List imagesId
		Log.v("BEFORE SHUFFLE", listImageIDs.toString() );
		Collections.shuffle(listImageIDs); 
		Log.v("AFTER SHUFFLE", listImageIDs.toString() );

		Integer[] array = listImageIDs.toArray(new Integer[listImageIDs.size()]);
		gridview.setAdapter(new ImageAdapter(this, array));
		 
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(GameActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
	}
	
	
}
