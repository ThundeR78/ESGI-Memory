package fr.esgi.android.project.esgi_memory.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.SparseArray;
import fr.esgi.android.project.esgi_memory.R;

@SuppressLint("UseSparseArrays")
//Class to manage sounds to allow to play multiple sound simultaneously
public class SoundManager {

	public static final int SOUND_DEAL_CARDS = 1;
	public static final int SOUND_TURN_CARD = 2;
	public static final int SOUND_WRONG_CARDS = 3;
	public static final int SOUND_SAME_CARDS = 4;
	public static final int SOUND_WIN_GAME = 5;
	public static final int SOUND_LOOSE_GAME = 6;

    private static boolean isSoundTurnedOn = true;
    public static boolean isLoaded = false;

    private static SoundManager mSoundManager;

    private SoundPool mSoundPool; 
    private SparseArray <Integer> mSoundPoolMap; 
    private AudioManager  mAudioManager;

    public static final int maxSounds = 4;

    public static SoundManager getInstance(Context context) {
        if (mSoundManager == null)
            mSoundManager = new SoundManager(context);

        return mSoundManager;
    }

    public SoundManager(Context mContext) {
        mAudioManager = (AudioManager) mContext.getSystemService(mContext.AUDIO_SERVICE);
        mSoundPool = new SoundPool(maxSounds, AudioManager.STREAM_MUSIC, 0);

        mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
            	isLoaded = true;
            }
        });
        
        mSoundPoolMap = new SparseArray<Integer>(); 
        mSoundPoolMap.put(SOUND_DEAL_CARDS, mSoundPool.load(mContext, R.raw.putcards, 1));
        mSoundPoolMap.put(SOUND_TURN_CARD, mSoundPool.load(mContext, R.raw.card, 1));
        mSoundPoolMap.put(SOUND_WRONG_CARDS, mSoundPool.load(mContext, R.raw.fartshort, 1));
        mSoundPoolMap.put(SOUND_SAME_CARDS, mSoundPool.load(mContext, R.raw.wolfwis, 1));
        mSoundPoolMap.put(SOUND_WIN_GAME, mSoundPool.load(mContext, R.raw.applause, 1));
        mSoundPoolMap.put(SOUND_LOOSE_GAME, mSoundPool.load(mContext, R.raw.fartlong, 1));
    } 

    public void playSound(int index) { 
        if (!isSoundTurnedOn())
            return;

         int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
         mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f); 
    }
    
    public static void resume() {
//    	mSoundManager.mSoundPool.autoResume();
    }

    public static void pause() {
//    	mSoundManager.mSoundPool.autoPause();
    }
    
    public static void clear() {
        if (mSoundManager != null) {
        	mSoundManager.mSoundPool.release();
            mSoundManager.mSoundPool = null; 
            mSoundManager.mAudioManager = null;
            mSoundManager.mSoundPoolMap = null;
        }
        mSoundManager = null;
    }

	public static boolean isSoundTurnedOn() {
		return isSoundTurnedOn;
	}

	public static void setSoundTurnedOff(boolean isSoundTurnedOn) {
		SoundManager.isSoundTurnedOn = isSoundTurnedOn;
	}
	
	public static boolean isLoaded() {
		return isLoaded;
	}
}
