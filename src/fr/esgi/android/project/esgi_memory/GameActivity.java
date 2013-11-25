package fr.esgi.android.project.esgi_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import fr.esgi.android.project.esgi_memory.business.Score;
import fr.esgi.android.project.esgi_memory.db.DatabaseHandler;
import fr.esgi.android.project.esgi_memory.util.FormatDate;
import fr.esgi.android.project.esgi_memory.view.ImageAdapter;

public class GameActivity extends Activity {
	
	//Game
	private int level;
	private int nbMove = 0;
	private boolean isGameFinished = false;
	Score score;
	
	//Time
	private boolean hasTimer = false;
	private int timeTotal = 0;
	private long timeInMilliseconds = 0;
	private final int delayBetweenEachTick = 500;
	private final long timeBlinkInMilliseconds = 10000; //Start time of start blinking (10sec)
	private boolean blink = false; 	//Controls the blinking, on and off
	
	//Card
	private List<Integer> listImageIDs = new ArrayList<Integer>();
	private int nbPairFound = 0;
	private int firstCardIndex = -1; //index of the first card selected
	
	//Components
	private TextView txtTimer, txtMove;
	private GridView gridview;
	private Chronometer chrono;
	private CountDownTimer countdownTimer;
	private AlertDialog dialog;
	
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
		
		//Launch Game
		initGame();
		loadGame();

		Log.i(ESGIMemoryApp.KEY_LEVEL, "Level= "+level);
		Log.i(ESGIMemoryApp.KEY_TIMER, "Timer= "+timeTotal);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
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
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//Game
		outState.putInt(ESGIMemoryApp.KEY_LEVEL, level);
		outState.putInt(ESGIMemoryApp.KEY_MOVE, nbMove);
		outState.putBoolean(ESGIMemoryApp.KEY_GAME_FINISHED, isGameFinished);
		//Time
		outState.putBoolean(ESGIMemoryApp.KEY_HAS_TIMER, hasTimer);
		outState.putInt(ESGIMemoryApp.KEY_TIME_TOTAL, timeTotal);
		outState.putLong(ESGIMemoryApp.KEY_TIME_MS, timeInMilliseconds);
//		outState.putInt(ESGIMemoryApp.KEY_DELAY_TICK, delayBetweenEachTick);
//		outState.putLong(ESGIMemoryApp.KEY_TIME_BLINK_MS, timeBlinkInMilliseconds);
		outState.putBoolean(ESGIMemoryApp.KEY_BLINK, blink);
		//Card
		outState.putIntegerArrayList(ESGIMemoryApp.KEY_LIST_IMAGEID, (ArrayList<Integer>) listImageIDs);
		outState.putInt(ESGIMemoryApp.KEY_PAIR_FOUND, nbPairFound);
		outState.putInt(ESGIMemoryApp.KEY_TIME_TOTAL, firstCardIndex);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		//Game
		level = savedInstanceState.getInt(ESGIMemoryApp.KEY_LEVEL, 2);
		nbMove = savedInstanceState.getInt(ESGIMemoryApp.KEY_MOVE, 0);
		isGameFinished = savedInstanceState.getBoolean(ESGIMemoryApp.KEY_GAME_FINISHED, false);
		//Time
		hasTimer = savedInstanceState.getBoolean(ESGIMemoryApp.KEY_HAS_TIMER, false);
		timeTotal = savedInstanceState.getInt(ESGIMemoryApp.KEY_TIME_TOTAL, ESGIMemoryApp.TIMER_NORMAL);
		timeInMilliseconds = savedInstanceState.getLong(ESGIMemoryApp.KEY_TIME_MS, 0);
//		delayBetweenEachTick = savedInstanceState.getInt(ESGIMemoryApp.KEY_DELAY_TICK, 500);
//		timeBlinkInMilliseconds = savedInstanceState.getLong(ESGIMemoryApp.KEY_TIME_BLINK_MS, 10000);
		blink = savedInstanceState.getBoolean(ESGIMemoryApp.KEY_BLINK, false);
		//Card
		listImageIDs = savedInstanceState.getIntegerArrayList(ESGIMemoryApp.KEY_LIST_IMAGEID);
		nbPairFound = savedInstanceState.getInt(ESGIMemoryApp.KEY_PAIR_FOUND, 0);
		firstCardIndex = savedInstanceState.getInt(ESGIMemoryApp.KEY_TIME_TOTAL, -1);
	}
	
	//Initialize GridView
	private void initGridView() {
		//Init GridView columns
		gridview.setNumColumns((level == ESGIMemoryApp.KEY_LEVEL_EASY) ? 3 : (level == ESGIMemoryApp.KEY_LEVEL_HARD) ? 4 : 3);
		
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
	
	//Load GridView
	private void loadGridView() {
		//Get images
		Integer[] array = listImageIDs.toArray(new Integer[listImageIDs.size()]);
		gridview.setAdapter(new ImageAdapter(this, array));
	}
	
	//Initialize Game
	private void initGame() {
		//Get array imagesId + timeTotal
		TypedArray images;
		if (level == ESGIMemoryApp.KEY_LEVEL_EASY) 
			images = getResources().obtainTypedArray(R.array.images_easy);
		else if (level == ESGIMemoryApp.KEY_LEVEL_HARD)
			images = getResources().obtainTypedArray(R.array.images_hard);
		else
			images = getResources().obtainTypedArray(R.array.images_normal);
					
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
		
		loadGridView();
		loadTime();
		
		nbMove = 0;
		nbPairFound = 0;
		blink = false;
		score = null;
		
		//TODO: AlertDialog Ready?
	}
	
	//Initialize Time
	private void initTime() {
		if (hasTimer) {
			timeTotal = (level==ESGIMemoryApp.KEY_LEVEL_EASY) ? ESGIMemoryApp.TIMER_EASY : (level==ESGIMemoryApp.KEY_LEVEL_HARD) ?  ESGIMemoryApp.TIMER_HARD : ESGIMemoryApp.TIMER_NORMAL;
			
			chrono.setVisibility(View.GONE);
		} else {
			txtTimer.setVisibility(View.GONE);
			chrono.setOnChronometerTickListener(new OnChronometerTickListener() {
			    public void onChronometerTick(Chronometer cArg) {
			        cArg.setText(FormatDate.millisecondFormat(SystemClock.elapsedRealtime() - cArg.getBase()));
			    }
			});
		}
	}
	
	private void loadTime() {
		timeInMilliseconds = (hasTimer) ? timeTotal * 1000 : 0;
		
		startTime();
	}
	
	//Start Timer or Chrono
	private void startTime() {
		if (!isGameFinished) {
			if (hasTimer) {
				startCountdown(timeInMilliseconds);
			} else {
				chrono.setBase(SystemClock.elapsedRealtime()+timeInMilliseconds);
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
				gameFinished(false);
			}
		}.start();
	}
	
	//Stop Timer or Chrono
	private void stopTime() {
		if (hasTimer) 
			countdownTimer.cancel();
		else {
			timeInMilliseconds = chrono.getBase() - SystemClock.elapsedRealtime();
			chrono.stop();
		}
	}
	
	private void gameFinished(boolean win) {
		isGameFinished = true;
			
		stopTime();
		loadResult(win);
		
		isGameFinished = false;
	}
	
	//Count Result Score
	private void loadResult(boolean win) {
		//Get Username
		Editor editor = getSharedPreferences(ESGIMemoryApp.PREFS_APP, 0).edit();
		editor.putString(ESGIMemoryApp.PREF_USERNAME, "ThundeR");
		editor.commit();
		
		SharedPreferences prefs = getSharedPreferences(ESGIMemoryApp.PREFS_APP, 0);
		String username = prefs.getString(ESGIMemoryApp.PREF_USERNAME, "");
		
		//Count Time
		long timeToFinish = 0;
		String time = "";
		if (win) {
			if (hasTimer) {
				timeToFinish = timeTotal - (timeInMilliseconds/1000);
			} else {		
				timeToFinish = SystemClock.elapsedRealtime() - chrono.getBase();
			}
			time = FormatDate.millisecondFormat(timeToFinish);
		} else {
			time = getResources().getString(R.string.label_times_up);
		}
		
		//Get Move
		String move = getResources().getQuantityString(R.plurals.numberMove, nbMove, nbMove);
		
		//TODO: Count Bonus Level
		int bonus = (level == ESGIMemoryApp.KEY_LEVEL_EASY) ? 1000 : (level == ESGIMemoryApp.KEY_LEVEL_NORMAL) ? 2000 : 3000;
		
		//TODO: Count points
		int points = 0;
		
		//Save new Score
		score = new Score(username, new Date(), win, level, timeToFinish, nbMove, bonus, points);

		displayResult(win, username, time, move, bonus+"", points+"");
	}
	
	//Display Score result
	private void displayResult(boolean win, String username, String time, String move, String bonus, String points) {
		Resources res = getResources();
		LayoutInflater inflater = getLayoutInflater();
		
		if (win)
			Toast.makeText(this, "BRAVO, tu as finis en "+time+" et "+move+" !", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this, "P'TITE CAISSE !", Toast.LENGTH_LONG).show();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(res.getString(win ? R.string.dialog_title_win : R.string.dialog_title_loose))
//			.setIcon(android.R.drawable.ic_dialog_alert)
			.setView(inflater.inflate(R.layout.dialog_game_result, null))
			.setCancelable(false)
			.setPositiveButton(res.getString(R.string.button_retry), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialogInterface,int id) {
					score.setUsername(((EditText)dialog.findViewById(R.id.editUsername)).getText().toString());
					saveScore(score);
					refreshUI();
					loadGame();
				}
			})
			.setNegativeButton(res.getString(R.string.button_leave), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialogInterface,int id) {
					score.setUsername(((EditText)dialog.findViewById(R.id.editUsername)).getText().toString());
					saveScore(score);
					Intent intent = new Intent(GameActivity.this, HomeActivity.class);
					startActivity(intent);
					GameActivity.this.finish();
				}
			});
	 
		//Create alert dialog
		dialog = builder.create();
		//Show it
		dialog.show();
		
		//Display score
		((EditText) dialog.findViewById(R.id.editUsername)).setText(username);
		((TextView) dialog.findViewById(R.id.text_value_time)).setText(time);
		((TextView) dialog.findViewById(R.id.text_value_move)).setText(move);
		((TextView) dialog.findViewById(R.id.text_value_bonus)).setText(bonus);
		((TextView) dialog.findViewById(R.id.text_value_points)).setText(points);
	}
	
	//Save Score in Database
	private void saveScore(Score score) {
		DatabaseHandler db = new DatabaseHandler(this);
		db.addScore(score);
		
		List<Score> scores = db.getAllScores();
		Log.d("SCORES", "Nb="+scores.size());
	}
	
	//Refresh interface
	private void refreshUI() {
		txtMove.setText(getResources().getString(R.string.init_value_move));
		txtTimer.setText(getResources().getString(R.string.init_value_time));
	}
	
	//Click on a card
	OnItemClickListener onGridViewItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        	if (firstCardIndex == position)
        		return;
        	
        	if (firstCardIndex == -1 || (firstCardIndex != -1 && position != firstCardIndex)) {
            	nbMove++;
            	txtMove.setText(getResources().getQuantityString(R.plurals.numberMove, nbMove, nbMove));
            	
            	if (firstCardIndex == -1) {
            		firstCardIndex = position;
//            		v.setEnabled(false);
//            		gridview.getAdapter().isEnabled(position);
//            		v.setFocusable(false);
//            		v.setFocusableInTouchMode(false);
//            		v.setClickable(false);
//            		v.setVisibility(View.INVISIBLE);
//            		v.setOnClickListener(null);
            	} else {
            		if (gridview.getChildAt(firstCardIndex).getTag() == v.getTag()) {
            			//TODO: Same
            			Log.d("CARD", "SAME: "+gridview.getChildAt(firstCardIndex).getTag()+" = "+v.getTag());
            			nbPairFound++;
            			gridview.getChildAt(firstCardIndex).setVisibility(View.INVISIBLE);
            			v.setVisibility(View.INVISIBLE);
            			
            			if (nbPairFound == parent.getCount()/2)
            				gameFinished(true);
            		} else {
            			//TODO: Different
            			Log.d("CARD", "DIFFERENT: "+gridview.getChildAt(firstCardIndex).getTag()+" != "+v.getTag());
            		}
            		
//            		gridview.getChildAt(firstCard).setOnClickListener((OnClickListener) onGridViewItemClickListener);
//            		gridview.getChildAt(firstCard).setEnabled(true);
            		firstCardIndex = -1;
            	}
            	
//                Toast.makeText(GameActivity.this, "" + position, Toast.LENGTH_SHORT).show();
        	} else {
        		if (firstCardIndex != -1)
        			Log.d("CARD", "EXACTLY THE SAME CARD");
        	}
        	Log.d("CARD", "POSITION="+position+" - FIRSTCARD="+firstCardIndex);
        }
        
    };
}
