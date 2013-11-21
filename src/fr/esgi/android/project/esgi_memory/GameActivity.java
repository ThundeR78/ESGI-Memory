package fr.esgi.android.project.esgi_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import fr.esgi.android.project.esgi_memory.view.ImageAdapter;

public class GameActivity extends Activity {
	
	//Game
	private int level;
	private int nbMove = 0;
	private boolean isGameFinished = false;
	
	//Time
	private boolean hasTimer = false;
	private int timeTotal = 0;
	private long timeInMilliseconds = 0;
	private final int delayBetweenEachTick = 500;
	private final long timeBlinkInMilliseconds = 10000; //Start time of start blinking (10sec)
	private boolean blink; 	//Controls the blinking, on and off
	
	//Card
	private List<Integer> listImageIDs = new ArrayList<Integer>();
	private int nbPairFound = 0;
	private int firstCard = -1; //index of the first card selected
	
	//Components
	private TextView txtTimer, txtMove;
	private GridView gridview;
	private Chronometer chrono;
	private CountDownTimer countdownTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		//Get parameters
		level = getIntent().getIntExtra(ESGIMemoryApp.KEY_LEVEL, ESGIMemoryApp.KEY_LEVEL_NORMAL);
		hasTimer = getIntent().getBooleanExtra(ESGIMemoryApp.KEY_TIMER, false);
		
		//Link interface and components
		gridview = (GridView) findViewById(R.id.gridView);
		chrono = (Chronometer) findViewById(R.id.chronometer);
		txtTimer = (TextView) findViewById(R.id.text_countdown);
		txtMove = (TextView) findViewById(R.id.text_move);
		
		initGame();
		loadGame();

		Log.i(ESGIMemoryApp.KEY_LEVEL, "Level= "+level);
		Log.i(ESGIMemoryApp.KEY_TIMER, "Timer= "+timeTotal);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		startTime();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		stopTime();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	//Initialize GridView
	private void initGridView() {
		//Init GridView columns
		gridview.setNumColumns((level == ESGIMemoryApp.KEY_LEVEL_EASY) ? 3 : (level == ESGIMemoryApp.KEY_LEVEL_HARD) ? 4 : 3);
		
		//Get images
		Integer[] array = listImageIDs.toArray(new Integer[listImageIDs.size()]);
		gridview.setAdapter(new ImageAdapter(this, array));
		
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
	
	//Initialize Game
	private void initGame() {
		//Get array imagesId + timeTotal
		TypedArray images;
		if (level == ESGIMemoryApp.KEY_LEVEL_EASY) {
			timeTotal = ESGIMemoryApp.TIMER_EASY;
			images = getResources().obtainTypedArray(R.array.images_easy);
		} else if (level == ESGIMemoryApp.KEY_LEVEL_HARD) {
			timeTotal = ESGIMemoryApp.TIMER_HARD;
			images = getResources().obtainTypedArray(R.array.images_hard);
		} else  {
			timeTotal = ESGIMemoryApp.TIMER_NORMAL;
			images = getResources().obtainTypedArray(R.array.images_normal);
		}
			
		//Transfer array imagesId into List
		for (int i=0; i<images.length() ;i++) 
			listImageIDs.add(images.getResourceId(i, 0));
		//Add pair of each imageId
		listImageIDs.addAll(listImageIDs);
		
		initGridView();
		
		initTime();
	}
	
	//Load Game
	private void loadGame() {
		//Shuffle List imagesId
		Log.v("BEFORE SHUFFLE", listImageIDs.toString());
		Collections.shuffle(listImageIDs); 
		Log.v("AFTER SHUFFLE", listImageIDs.toString());
	}
	
	//Initialize Time
	private void initTime() {
		if (hasTimer) {
			chrono.setVisibility(View.GONE);
			timeInMilliseconds = timeTotal * 1000;
		} else {
			txtTimer.setVisibility(View.GONE);
			chrono.setBase(SystemClock.elapsedRealtime());
			chrono.setOnChronometerTickListener(new OnChronometerTickListener() {
			    public void onChronometerTick(Chronometer cArg) {
//			        long t = SystemClock.elapsedRealtime() - cArg.getBase();
//			        cArg.setText(DateFormat.format("m'min'ss", t));
			        
			        cArg.setText(millisecondFormat(SystemClock.elapsedRealtime() - cArg.getBase()));
			    }
			});
		}
	}
	
	//Start Timer or Chrono
	private void startTime() {
		if (!isGameFinished) {
			if (hasTimer) {
				startCountdown(timeInMilliseconds);
			} else {
				chrono.start();
			}
		}
	}
	
	//Start Timer
	private void startCountdown (long timeInMillisec) {
		countdownTimer = new CountDownTimer(timeInMillisec, delayBetweenEachTick) {
			public void onTick(long millisUntilFinished) {
				timeInMilliseconds = millisUntilFinished;
				long seconds = millisUntilFinished / 1000;
				
				if (millisUntilFinished < timeBlinkInMilliseconds) {
					txtTimer.setTextAppearance(getApplicationContext(), R.style.blinkText);	//Change the style of the textview, giving a red alert style
					txtTimer.setVisibility(blink ? View.VISIBLE : View.INVISIBLE);
					blink = !blink; 	//Toggle the value of blink
				}
					
				txtTimer.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
			}
			
			public void onFinish() {
				txtTimer.setVisibility(View.VISIBLE);
				txtTimer.setText("Temps écoulé !");
				gameFailed();
			}
		}.start();
	}
	
	//Stop Timer or Chrono
	private void stopTime() {
		if (hasTimer) 
			countdownTimer.cancel();
		else {
			chrono.stop();
			//TODO: restart with good time
			Log.d("CHRONO", chrono.getText().toString());
		}
	}
	
	private void gameSuccess() {
		gameFinished();
		
		String timeToFinish = (hasTimer) ? millisecondFormat(timeInMilliseconds) : millisecondFormat(SystemClock.elapsedRealtime() - chrono.getBase());
		Toast.makeText(GameActivity.this, "BRAVO, tu as finis en "+timeToFinish+" et "+nbMove+"coups !", Toast.LENGTH_LONG).show();

	}
	
	private void gameFailed() {
		gameFinished();
		Toast.makeText(GameActivity.this, "P'TITE CAISSE !", Toast.LENGTH_LONG).show();
	}
	
	private void gameFinished() {
		isGameFinished = true;
		
		stopTime();
	}
	
	private String millisecondFormat(long time) {
	    if (time > 60000)	//1min
	    	return (String) DateFormat.format("m'min' ss'sec'", new Date(time));
	    else 
	    	return (String) DateFormat.format("s'sec'", new Date(time));
	}
	
	//Click on a card
	OnItemClickListener onGridViewItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        	if (firstCard == position)
        		return;
        	
        	if (firstCard == -1 || (firstCard != -1 && position != firstCard)) {
            	nbMove++;
            	txtMove.setText(getResources().getQuantityString(R.plurals.numberMove, nbMove, nbMove));
            	
            	if (firstCard == -1) {
            		firstCard = position;
//            		v.setEnabled(false);
//            		gridview.getAdapter().isEnabled(position);
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
        			Log.d("CARD", "EXACTLY THE SAME CARD");
        	}
        	Log.d("CARD", "POSITION="+position+" - FIRSTCARD="+firstCard);
        }
        
    };
}
