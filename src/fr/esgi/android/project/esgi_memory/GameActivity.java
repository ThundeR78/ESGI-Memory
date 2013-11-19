package fr.esgi.android.project.esgi_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import fr.esgi.android.project.esgi_memory.view.ImageAdapter;

public class GameActivity extends Activity {
	
	private int level;
	private List<Integer> listImageIDs = new ArrayList<Integer>();
	private int firstCard = -1; //index of the first card selected
	private int nbMove = 0;
	private CountDownTimer countdownTimer;
	private long timeBlinkInMilliseconds = 10000; //Start time of start blinking (10sec)
	private boolean blink; 	//Controls the blinking, on and off
	private int nbPairFound = 0;
	
	private Chronometer chrono;
	private TextView txtCountdown;
	private GridView gridview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_easy);
		
		level = getIntent().getIntExtra(ESGIMemoryApp.KEY_LEVEL, ESGIMemoryApp.KEY_LEVEL_NORMAL);
		
		Log.i(ESGIMemoryApp.KEY_LEVEL, "Level= "+level);
		
		gridview = (GridView) findViewById(R.id.gridView);
		chrono = (Chronometer) findViewById(R.id.chronometer);
		txtCountdown = (TextView) findViewById(R.id.text_countdown);
		
		loadImages();
		
		initGridView();
		
		launchCountdown(15);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		chrono.setBase(SystemClock.elapsedRealtime());
		chrono.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		chrono.stop();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	private void initGridView() {
		//Avoid Scroll
		gridview.setOnTouchListener(new OnTouchListener(){
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        if(event.getAction() == MotionEvent.ACTION_MOVE)
		            return true;
		        return false;
		    }
		});
		//Get Click Listener
		gridview.setOnItemClickListener(onGridViewItemClickListener);
	}
	
	private void loadImages() {
		//Load array imagesId
		TypedArray images;
		if (level == ESGIMemoryApp.KEY_LEVEL_EASY) {
			images = getResources().obtainTypedArray(R.array.images_easy);
			gridview.setNumColumns(3);
		} else if (level == ESGIMemoryApp.KEY_LEVEL_HARD) {
			images = getResources().obtainTypedArray(R.array.images_hard);
			gridview.setNumColumns(4);
		} else  {
			images = getResources().obtainTypedArray(R.array.images_normal);
			gridview.setNumColumns(3);
		}
			
		//Transfer array imagesId into List
		for (int i=0; i<images.length() ;i++) 
			listImageIDs.add(images.getResourceId(i, 0));
		//Add pair of each imageId
		listImageIDs.addAll(listImageIDs);
		
		//Shuffle List imagesId
		Log.v("BEFORE SHUFFLE", listImageIDs.toString());
		Collections.shuffle(listImageIDs); 
		Log.v("AFTER SHUFFLE", listImageIDs.toString());

		Integer[] array = listImageIDs.toArray(new Integer[listImageIDs.size()]);
		gridview.setAdapter(new ImageAdapter(this, array));
	}
	
	private void launchCountdown (int seconds) {
		countdownTimer = new CountDownTimer(seconds*1000, 500) {
			
			public void onTick(long millisUntilFinished) {
				long seconds = millisUntilFinished / 1000;
		    	 
				if (millisUntilFinished < timeBlinkInMilliseconds) {
					txtCountdown.setTextAppearance(getApplicationContext(), R.style.blinkText);	//Change the style of the textview, giving a red alert style
					txtCountdown.setVisibility(blink ? View.VISIBLE : View.INVISIBLE);
					blink = !blink; 	//Toggle the value of blink
				}
					
				txtCountdown.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
			}
			
			public void onFinish() {
				txtCountdown.setVisibility(View.VISIBLE);
				txtCountdown.setText("Temps écoulé !");
			}
		}.start();
	}
	
	private void gameSuccess() {
		gameFinished();
		Toast.makeText(GameActivity.this, "BRAVO", Toast.LENGTH_LONG).show();
	}
	
	private void gameFinished() {
		chrono.stop();
//		countdownTimer.
	}
	
	OnItemClickListener onGridViewItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        	if (firstCard == position)
        		return;
        	
        	if (firstCard == -1 || (firstCard != -1 && position != firstCard)) {
            	nbMove++;
            	
            	if (firstCard == -1) {
            		firstCard = position;
//            		v.setEnabled(false);
            		gridview.getAdapter().isEnabled(position);
//            		v.setFocusable(false);
//            		v.setFocusableInTouchMode(false);
//            		v.setClickable(false);
//            		v.setVisibility(View.INVISIBLE);
//            		v.setOnClickListener(null);
            	} else {
            		if (gridview.getChildAt(firstCard).getTag() == v.getTag()) {
            			//TODO: Same
            			Log.d("CARD", "SAME: "+gridview.getChildAt(firstCard).getTag()+" = "+v.getTag());
            			nbPairFound++;
            			gridview.getChildAt(firstCard).setVisibility(View.INVISIBLE);
            			v.setVisibility(View.INVISIBLE);
            			
            			if (nbPairFound == parent.getCount()/2)
            				gameSuccess();
            		} else {
            			//TODO: Different
            			Log.d("CARD", "DIFFERENT: "+gridview.getChildAt(firstCard).getTag()+" != "+v.getTag());
            		}
            		
//            		gridview.getChildAt(firstCard).setOnClickListener((OnClickListener) onGridViewItemClickListener);
//            		gridview.getChildAt(firstCard).setEnabled(true);
            		firstCard = -1;
            	}
            	
//                Toast.makeText(GameActivity.this, "" + position, Toast.LENGTH_SHORT).show();
        	} else {
        		if (firstCard != -1)
        			Log.d("CARD", "SAME CARD");
        	}
        	Log.d("CARD", "POSITION="+position+" - FIRSTCARD="+firstCard);
        }
        
    };
}
