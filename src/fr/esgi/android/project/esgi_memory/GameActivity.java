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
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import fr.esgi.android.project.esgi_memory.business.Score;
import fr.esgi.android.project.esgi_memory.db.DatabaseHandler;
import fr.esgi.android.project.esgi_memory.util.FormatValue;
import fr.esgi.android.project.esgi_memory.util.SoundManager;
import fr.esgi.android.project.esgi_memory.view.CardView;
import fr.esgi.android.project.esgi_memory.view.ImageAdapter;

public class GameActivity extends Activity implements OnClickListener {
	
	//Game
	private int level;
	private int nbMove = 0;
	private boolean isGameFinished = false;
	private Score score;
	private boolean inAnimation = false;
	private String username;
	private final int timeToReturn = 500;
	private boolean gameWon = false;
	
	//Time
	private boolean hasTimer = false;
	private int timeTotal = 0;
	private long timeInMilliseconds = 0;
	private final int delayBetweenEachTick = 500;
	private final long timeBlinkInMilliseconds = 10000; //Start time of start blinking (10sec)
	private boolean blink = false; 	//Controls the blinking, on and off
	
	//Card
	private List<Integer> listImageIDs = new ArrayList<Integer>();
	private CardView cardViewClicked;
	private int nbPairFound = 0;
	private int firstCardIndex = -1, secondCardIndex = -1; //index of cards selected
	private final Integer cardBackId = R.drawable.ic_launcher;
	private boolean sameCard = false;
	
	//Components
	private TextView txtTimer, txtMove;
	private GridView gridview;
	private Chronometer chrono;
	private CountDownTimer countdownTimer;
	private AlertDialog dialog;
//	private MediaPlayer mediaPlayer;
	
	//Sound
	private boolean hasSound = true;
	private SoundManager soundManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		//Get parameters
		Intent intent = getIntent();
		if (intent != null && intent.getExtras() != null) {
			level = intent.getIntExtra(ESGIMemoryApp.KEY_LEVEL, ESGIMemoryApp.KEY_LEVEL_NORMAL);
			hasTimer = intent.getBooleanExtra(ESGIMemoryApp.KEY_TIMER, false);
		} else {
			level = Integer.parseInt(prefs.getString(ESGIMemoryApp.PREF_LEVEL, "2"));
			hasTimer = prefs.getBoolean(ESGIMemoryApp.PREF_TIMER, false);
		}

		//Get pref value
		hasSound =  prefs.getBoolean(ESGIMemoryApp.PREF_HAS_SOUND, true);
		username = prefs.getString(ESGIMemoryApp.PREF_USERNAME, "");
		
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
		outState.putBoolean(ESGIMemoryApp.KEY_HAS_SOUND, hasSound);
		outState.putBoolean(ESGIMemoryApp.KEY_GAME_WON, gameWon);
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
		outState.putBoolean(ESGIMemoryApp.KEY_IN_ANIMATION, inAnimation);
		outState.putBoolean(ESGIMemoryApp.KEY_SAME_CARD, sameCard);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		//Game
		level = savedInstanceState.getInt(ESGIMemoryApp.KEY_LEVEL, 2);
		nbMove = savedInstanceState.getInt(ESGIMemoryApp.KEY_MOVE, 0);
		isGameFinished = savedInstanceState.getBoolean(ESGIMemoryApp.KEY_GAME_FINISHED, false);
		hasSound = savedInstanceState.getBoolean(ESGIMemoryApp.KEY_HAS_SOUND, false);
		gameWon = savedInstanceState.getBoolean(ESGIMemoryApp.KEY_GAME_WON, false);
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
		inAnimation = savedInstanceState.getBoolean(ESGIMemoryApp.KEY_IN_ANIMATION, false);
		sameCard = savedInstanceState.getBoolean(ESGIMemoryApp.KEY_SAME_CARD, false);
	}
	
	//Initialize GridView
	private void initGridView() {
		//Init GridView columns
		gridview.setNumColumns((level == ESGIMemoryApp.KEY_LEVEL_EASY) ? 3 : (level == ESGIMemoryApp.KEY_LEVEL_HARD) ? 4 : 3);
		
		//Avoid Scroll
//		gridview.setOnTouchListener(new OnTouchListener(){
//		    @Override
//		    public boolean onTouch(View v, MotionEvent event) {
//		        if (event.getAction() == MotionEvent.ACTION_MOVE)
//		            return true;
//		        return false;
//		    }
//		});
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
//		Log.v("BEFORE SHUFFLE", listImageIDs.toString());
		Collections.shuffle(listImageIDs); 
//		Log.v("AFTER SHUFFLE", listImageIDs.toString());
		
		soundManager.playSound(SoundManager.SOUND_DEAL_CARDS);
		
		loadGridView();
		loadTime();
		
		nbMove = 0;
		nbPairFound = 0;
		blink = false;
		score = null;
		txtTimer.setTextAppearance(this, R.style.text_normal);
		
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
			    @Override
				public void onChronometerTick(Chronometer cArg) {
			        cArg.setText(FormatValue.millisecondFormat(SystemClock.elapsedRealtime() - cArg.getBase()));
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
			@Override
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
			
			@Override
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
		
		//Stop Game
		stopTime();
		loadResult(win);
		
		isGameFinished = false;
	}
	
	//Count Result Score
	private void loadResult(boolean win) {
		long timeToFinish = 0;
		String time = "";
		int points = 0, bonus = 0, pointsTime = 0, pointsMove = 0, bonusTimer = 0, bonusLevel = 0;
		
		//Get Move
		String move = getResources().getQuantityString(R.plurals.numberMove, nbMove, nbMove);
		pointsMove = ((60 - nbMove) > 0) ? (60 - nbMove) * 100 : 0;
			
		//Count Time
		if (win) {
			gameWon = true;
			
			//Count Bonus Level
			bonusLevel = (level == ESGIMemoryApp.KEY_LEVEL_EASY) ? 1000 : (level == ESGIMemoryApp.KEY_LEVEL_NORMAL) ? 2000 : 4000;
			
			if (hasTimer) {
				timeToFinish = timeTotal - (timeInMilliseconds/1000);
				bonusTimer = bonusLevel*2;
			} else {
				timeToFinish = SystemClock.elapsedRealtime() - chrono.getBase();
			}
			time = FormatValue.millisecondFormat(timeToFinish);
		
			pointsTime = (int) (90 - timeToFinish/1000);
			if (pointsTime > 0)
				pointsTime *= 100;

			//Count points
			bonus = bonusTimer + bonusLevel;		
			points = pointsMove + pointsTime + bonus;
		} else {
			time = getResources().getString(R.string.label_times_up);
		}
		
		//Save new Score
		score = new Score(username, new Date(), win, hasTimer, level, timeToFinish, nbMove, bonus, points);

		//Display Dialog with result
		displayResult(win, username, time, move, FormatValue.formatBigNumber(bonus), FormatValue.formatBigNumber(points));
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
			.setPositiveButton(res.getString(R.string.button_retry), null)
			.setNegativeButton(res.getString(R.string.button_leave), null);
		//Create alert dialog
		dialog = builder.create();
		
		//Override show method to decide when I want to hide dialog
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
		    @Override
		    public void onShow(DialogInterface dialogInterface) {
		    	//Add listener on buttons
		        Button btnPos = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
		        btnPos.setId(AlertDialog.BUTTON_POSITIVE);
		        btnPos.setOnClickListener(GameActivity.this);
		        
		        Button btnNeg = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
		        btnNeg.setId(AlertDialog.BUTTON_NEGATIVE);
		        btnNeg.setOnClickListener(GameActivity.this);
		    }
		});
		
		//Show it
		dialog.show();
				
		//Display score
		((EditText) dialog.findViewById(R.id.editUsername)).setText(username);
		((TextView) dialog.findViewById(R.id.text_value_time)).setText(time);
		((TextView) dialog.findViewById(R.id.text_value_move)).setText(move);
		((TextView) dialog.findViewById(R.id.text_value_bonus)).setText(bonus);
		((TextView) dialog.findViewById(R.id.text_value_points)).setText(points);
	}
	
	@Override
	public void onClick(View v) {
		//Click on a result dialog button
		if (v.getId() == AlertDialog.BUTTON_POSITIVE || v.getId() == AlertDialog.BUTTON_NEGATIVE) {
			
			EditText editUsername = (EditText) dialog.findViewById(R.id.editUsername);
			String user = editUsername.getText().toString().trim();
			
			//Check Username
			if (!gameWon || (user != null && user.length() > 0 && user.length() < 20)) {
				if (gameWon) {
					score.setUsername(user);
					//Save Score
					saveScore(score);
				}
				
				//Dismiss once everything is OK.
	            dialog.dismiss();

				if (v.getId() == AlertDialog.BUTTON_POSITIVE) {
					//Reinit Score
					refreshUI();
					//Load new Game
					loadGame();
				} else if (v.getId() == AlertDialog.BUTTON_NEGATIVE) {
					//Go Home
					Intent intent = new Intent(GameActivity.this, HomeActivity.class);
					startActivity(intent);
					GameActivity.this.finish();
				}
			} else {
				editUsername.setError(getResources().getString(R.string.error_username));
			}
		}
	}   
	
	//Save Score in Database
	private void saveScore(Score score) {
		//Don't save if loose with a timer
		if (gameWon) {
			DatabaseHandler db = new DatabaseHandler(this);
			db.addScore(score);
			db.close();
		}
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
//	private void playSound(int soundIndex) {
//		if (hasSound && (mediaPlayer != null && !mediaPlayer.isPlaying())) {
//			mediaPlayer = MediaPlayer.create(this, soundIndex);
//			mediaPlayer.start();
//		}
//	}

	//Stop Sound
	private void resumeSound() {
		SoundManager.resume();
	}
	
	//Stop Sound
	private void pauseSound() {
		SoundManager.pause();
	}
	
	//Stop Sound
	private void stopSound() {
		SoundManager.clear();
	}
	
	//Click on a card
	OnItemClickListener onGridViewItemClickListener = new OnItemClickListener() {
        @Override
		public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
        	if (!inAnimation) {
	        	cardViewClicked = (CardView) v.getTag();
	        	
	        	//Second click on the same card or card since found 
	        	if (position == firstCardIndex || cardViewClicked.isReturned())
	        		return;
	        	
	        	//If first card not clicked or second card different to the first 
	        	if (firstCardIndex == -1 || (firstCardIndex != -1 && position != firstCardIndex)) {
	        		inAnimation = true;
	        		
	            	//First card clicked
	            	if (firstCardIndex == -1) {
	            		firstCardIndex = position;
	            	} else {
	            		nbMove++;
	                	txtMove.setText(getResources().getQuantityString(R.plurals.numberMove, nbMove, nbMove));
	                	secondCardIndex = position;
	                	
	                	CardView firstCardView = (CardView) gridview.getChildAt(firstCardIndex).getTag();
	                	//Compare Resource Id 
	            		if (firstCardView.getTag() == cardViewClicked.getTag()) {
	            			//Pair cards
//	            			Log.d("CARD", "SAME: "+gridview.getChildAt(firstCardIndex).getTag()+" = "+v.getTag());
	            			nbPairFound++;
	            			sameCard = true;
	            		} else {
	            			//Different cards
//	            			Log.d("CARD", "DIFFERENT: "+gridview.getChildAt(firstCardIndex).getTag()+" != "+v.getTag());
	            			sameCard = false;
	            		}
	            	}
	            	
	            	//Call animation
	            	Handler handler = new Handler();
	        		handler.post(runnableTurnCard);
	        	} else {
//	        		if (firstCardIndex != -1)
//	        			Log.d("CARD", "EXACTLY THE SAME CARD");
	        	}
//	        	Log.d("CARD", "POSITION="+position+" - FIRSTCARD="+firstCardIndex);
        	}
        }
    };
    
    Runnable runnableTurnCard = new Runnable() {
		@Override
		public void run() {
//			Log.v("HANDLER", "TURN CARD");
			
			if (firstCardIndex != -1) {
				Integer resId = 0;
				if (secondCardIndex == -1) {
					//First card selected
					resId = (Integer) gridview.getItemAtPosition(firstCardIndex);
				} else {
					//Second card selected
					resId = (Integer) gridview.getItemAtPosition(secondCardIndex);
				}
				
				soundManager.playSound(SoundManager.SOUND_TURN_CARD);
				cardViewClicked.imageview.setImageResource(resId);
				cardViewClicked.toggleSide();
			}
			
			if (secondCardIndex != -1) {
				//Call animation back
				Handler handlerReturn = new Handler();
	    		handlerReturn.postDelayed(runnableReturnCards, timeToReturn); 
			} else {
				inAnimation = false;
			}
		}
	};
	
	Runnable runnableReturnCards = new Runnable() {
		@Override
		public void run() {
//			Log.v("HANDLER", "RETURN CARD");
			if (sameCard) {
				//Is pair
//				gridview.getChildAt(firstCardIndex).setVisibility(View.INVISIBLE);
//				gridview.getChildAt(secondCardIndex).setVisibility(View.INVISIBLE);
				
    			soundManager.playSound(SoundManager.SOUND_SAME_CARDS);
    			
    			//Test if game is finished
    			if (nbPairFound == gridview.getChildCount()/2)
    				gameFinished(true);
			} else {
				//Not the same
				soundManager.playSound(SoundManager.SOUND_WRONG_CARDS);
				CardView cardFirst = (CardView) gridview.getChildAt(firstCardIndex).getTag();
				CardView cardSecond = (CardView) gridview.getChildAt(secondCardIndex).getTag();
				//Change image to display defaultCard 
				cardFirst.imageview.setImageResource(cardBackId);
				cardSecond.imageview.setImageResource(cardBackId);
				//Change value of isReturned
				cardFirst.toggleSide();
				cardSecond.toggleSide();
			}
			
            firstCardIndex = -1;
			secondCardIndex = -1;
			cardViewClicked = null;
			sameCard = false;
			inAnimation = false;
		}
	};
}


	