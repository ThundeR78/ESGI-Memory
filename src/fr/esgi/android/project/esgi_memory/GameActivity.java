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
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import fr.esgi.android.project.esgi_memory.business.Score;
import fr.esgi.android.project.esgi_memory.db.DatabaseHandler;
import fr.esgi.android.project.esgi_memory.util.FormatDate;
import fr.esgi.android.project.esgi_memory.util.SoundManager;
import fr.esgi.android.project.esgi_memory.view.ImageAdapter;

public class GameActivity extends Activity {
	
	//Game
	private int level;
	private int nbMove = 0;
	private boolean isGameFinished = false;
	private Score score;
	
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
	private final Integer cardBackId = R.drawable.ic_launcher;
	
	//Components
	private TextView txtTimer, txtMove;
	private GridView gridview;
	private Chronometer chrono;
	private CountDownTimer countdownTimer;
	private AlertDialog dialog;
	private MediaPlayer mediaPlayer;
	
	//Sound
	private boolean hasSound = true;
	private SoundManager soundManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		//Get parameters
		level = getIntent().getIntExtra(ESGIMemoryApp.KEY_LEVEL, ESGIMemoryApp.KEY_LEVEL_NORMAL);
		hasTimer = getIntent().getBooleanExtra(ESGIMemoryApp.KEY_TIMER, false);

		//Get value sound
		hasSound = getSharedPreferences(ESGIMemoryApp.PREFS_APP, 0).getBoolean(ESGIMemoryApp.PREF_HAS_SOUND, true);
		
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
		resumeSound();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		stopTime();
		pauseSound();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		stopSound();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.game_menu, menu);
	    return true;
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.action_refresh:
	    	stopTime();
	    	refreshUI();
	    	loadGame();
	    	break;
		}
	    return true;
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
		gridview.setAdapter(new ImageAdapter(this, array, cardBackId));
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
		initSound();
	}
	
	//Load Game
	private void loadGame() {
		//Shuffle List imagesId
		Log.v("BEFORE SHUFFLE", listImageIDs.toString());
		Collections.shuffle(listImageIDs); 
		Log.v("AFTER SHUFFLE", listImageIDs.toString());
		
		soundManager.playSound(SoundManager.SOUND_DEAL_CARDS);
		
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
	
	//Game is finished
	private void gameFinished(boolean win) {
		isGameFinished = true;
		
		soundManager.playSound(win ? SoundManager.SOUND_WIN_GAME : SoundManager.SOUND_LOOSE_GAME);
			
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
	
	//Init Sound
	private void initSound() {
		soundManager = SoundManager.getInstance(this);
		SoundManager.setSoundTurnedOff(hasSound);
	}
	
	//Play a Sound
	private void playSound(int soundIndex) {
		if (hasSound && (mediaPlayer != null && !mediaPlayer.isPlaying())) {
			mediaPlayer = MediaPlayer.create(this, soundIndex);
			mediaPlayer.start();
		}
	}
	
	
	//Stop Sound
	private void resumeSound() {
		soundManager.resume();
	}
	
	//Stop Sound
	private void pauseSound() {
		soundManager.pause();
	}
	
	//Stop Sound
	private void stopSound() {
		soundManager.clear();
	}
	
	//Click on a card
	OnItemClickListener onGridViewItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
        	//Second click on the same card 
        	if (firstCardIndex == position)
        		return;
        	
        	//If first card not clicked or second card different to the first 
        	if (firstCardIndex == -1 || (firstCardIndex != -1 && position != firstCardIndex)) {
        		ImageView imageView = (ImageView) v;
        		Integer resId = (Integer) adapter.getItemAtPosition(position);
        		imageView.setImageResource(resId);
        		soundManager.playSound(SoundManager.SOUND_TURN_CARD);
        		
//        		ImageAdapter imgAdapter = (ImageAdapter) gridview.getAdapter();
//        		imgAdapter.toggleItem(position);
//        		imgAdapter.notifyDataSetChanged();
//        		try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
        		
            	//First card clicked
            	if (firstCardIndex == -1) {
            		firstCardIndex = position;
            		
//            		imgAdapter.toggleItem(position);
//            		imgAdapter.notifyDataSetChanged();
            		
            		//Test disable
//            		v.setEnabled(false);
//            		gridview.getAdapter().isEnabled(position);
//            		v.setFocusable(false);
//            		v.setFocusableInTouchMode(false);
//            		v.setClickable(false);
//            		v.setVisibility(View.INVISIBLE);
//            		v.setOnClickListener(null);
            	} else {
            		nbMove++;
                	txtMove.setText(getResources().getQuantityString(R.plurals.numberMove, nbMove, nbMove));
            		
            		//Pair cards
            		if (gridview.getChildAt(firstCardIndex).getTag() == v.getTag()) {
            			//TODO: Same
            			Log.d("CARD", "SAME: "+gridview.getChildAt(firstCardIndex).getTag()+" = "+v.getTag());
            			nbPairFound++;
            			gridview.getChildAt(firstCardIndex).setVisibility(View.INVISIBLE);
            			v.setVisibility(View.INVISIBLE);
            			soundManager.playSound(SoundManager.SOUND_SAME_CARDS);
            			
            			if (nbPairFound == adapter.getCount()/2)
            				gameFinished(true);
            		} else {
            			//Different cards
            			Log.d("CARD", "DIFFERENT: "+gridview.getChildAt(firstCardIndex).getTag()+" != "+v.getTag());
            			((ImageView) gridview.getChildAt(firstCardIndex)).setImageResource(cardBackId);
                        imageView.setImageResource(cardBackId);
            			
                        soundManager.playSound(SoundManager.SOUND_WRONG_CARDS);
                        
//            			imgAdapter.toggleItem(position);
//            			imgAdapter.toggleItem(firstCardIndex);
//                		imgAdapter.notifyDataSetChanged();
            		}
            		
//            		gridview.getChildAt(firstCard).setOnClickListener((OnClickListener) onGridViewItemClickListener);
//            		gridview.getChildAt(firstCard).setEnabled(true);
            		firstCardIndex = -1;
            	}
        	} else {
        		if (firstCardIndex != -1)
        			Log.d("CARD", "EXACTLY THE SAME CARD");
        	}
        	Log.d("CARD", "POSITION="+position+" - FIRSTCARD="+firstCardIndex);
        }
        
    };
}
