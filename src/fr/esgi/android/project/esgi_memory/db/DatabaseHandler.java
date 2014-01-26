package fr.esgi.android.project.esgi_memory.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import fr.esgi.android.project.esgi_memory.business.Score;
import fr.esgi.android.project.esgi_memory.db.DatabaseContract.ScoreBase;
import fr.esgi.android.project.esgi_memory.util.FormatValue;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;	//To increment if database schema change
	public static final String DATABASE_NAME = "ESGIMemory.db";
	
	private static final String SQL_CREATE_SCORES =
        "CREATE TABLE "+ ScoreBase.TABLE_NAME +" (" +
        		ScoreBase.COLUMN_NAME_ID +" INTEGER PRIMARY KEY,"+
        		ScoreBase.COLUMN_NAME_USERNAME +" TEXT,"+
        		ScoreBase.COLUMN_NAME_DATE +" DATETIME DEFAULT CURRENT_TIMESTAMP,"+
        		ScoreBase.COLUMN_NAME_LEVEL +" INTEGER,"+
        		ScoreBase.COLUMN_NAME_TIME +" INTEGER,"+
        		ScoreBase.COLUMN_NAME_TIMER +" INTEGER,"+
        		ScoreBase.COLUMN_NAME_MOVE +" INTEGER,"+
        		ScoreBase.COLUMN_NAME_WIN +" INTEGER,"+
        		ScoreBase.COLUMN_NAME_BONUS +" INTEGER,"+
        		ScoreBase.COLUMN_NAME_POINT +" INTEGER"+
        " )";
    private static final String SQL_DELETE_SCORES =
        "DROP TABLE IF EXISTS " + ScoreBase.TABLE_NAME;
	
    //Constructor
	public DatabaseHandler(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL(SQL_CREATE_SCORES);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy is to simply to discard the data and start over
        db.execSQL(SQL_DELETE_SCORES);
        onCreate(db);
    }	
	@Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
    /* SCORE */
	//Adding new score
    public void addScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        //Values
        ContentValues values = new ContentValues();
        values.put(ScoreBase.COLUMN_NAME_USERNAME, score.getUsername());
        values.put(ScoreBase.COLUMN_NAME_DATE, score.getDate().getTime());
        values.put(ScoreBase.COLUMN_NAME_LEVEL, score.getLevel());
        values.put(ScoreBase.COLUMN_NAME_TIMER, score.hasTimer());
        values.put(ScoreBase.COLUMN_NAME_TIME, score.getTime());
        values.put(ScoreBase.COLUMN_NAME_MOVE, score.getMove());
        values.put(ScoreBase.COLUMN_NAME_WIN, score.isWin());
        values.put(ScoreBase.COLUMN_NAME_BONUS, score.getBonus());
        values.put(ScoreBase.COLUMN_NAME_POINT, score.getPoint());
     
        // Inserting Row
        db.insert(ScoreBase.TABLE_NAME, null, values);
        db.close(); 
    }
    
    //Getting single score
    public Score getScore(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        String[] arrayColumns = new String[] { ScoreBase.COLUMN_NAME_ID,
        		ScoreBase.COLUMN_NAME_USERNAME, ScoreBase.COLUMN_NAME_DATE, ScoreBase.COLUMN_NAME_WIN,
        		ScoreBase.COLUMN_NAME_TIMER, ScoreBase.COLUMN_NAME_LEVEL, ScoreBase.COLUMN_NAME_TIME, ScoreBase.COLUMN_NAME_MOVE, 
        		ScoreBase.COLUMN_NAME_BONUS, ScoreBase.COLUMN_NAME_POINT };
        String selection = ScoreBase.COLUMN_NAME_ID + "=?";
        String[] arrayArguments = new String[] { String.valueOf(id) };
        
        //Get results
        Cursor cursor = db.query(ScoreBase.TABLE_NAME, arrayColumns, selection, arrayArguments, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        //Convert result in object Score
        Score score = new Score(cursor.getInt(0),
                cursor.getString(1), new Date(cursor.getLong(2)), (cursor.getInt(3)==1) ? true : false, 
                (cursor.getInt(4)==1) ? true : false, cursor.getInt(5), cursor.getLong(6), 
                cursor.getInt(7), cursor.getInt(8), cursor.getInt(9));
        
        cursor.close();
        db.close();
        
        return score;
    }
    
    //Getting All Scores (id, username, date, point only)
    public List<Score> getAllScores() {
       List<Score> scoreList = new ArrayList<Score>();
       // Select All Query
       String selectQuery = "SELECT "+ScoreBase.COLUMN_NAME_ID+", "+ScoreBase.COLUMN_NAME_USERNAME+", "+ScoreBase.COLUMN_NAME_DATE+", "+ScoreBase.COLUMN_NAME_POINT+
       		" FROM " + ScoreBase.TABLE_NAME + " ORDER BY "+ ScoreBase.COLUMN_NAME_POINT;
    
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
       
       //Looping through all rows and adding to list
       if (cursor.moveToFirst()) {
           do {
               Score score = new Score();
               score.setId(cursor.getInt(0));
               score.setUsername(cursor.getString(1));
               score.setDate(new Date(cursor.getLong(2)));
               score.setPoint(cursor.getInt(3));
               // Adding score to list
               scoreList.add(score);
           } while (cursor.moveToNext());
       }
       cursor.close();
       db.close();
       
       // return score list
       return scoreList;
    }
    
    //Getting All Scores (id, username, date, point only)
    public List<Score> getAllScoresByLevel(int level) {
       List<Score> scoreList = new ArrayList<Score>();
       if (level == 0)
    	   level = 1;
       
       // Select All Query
       String selectQuery = "SELECT "+ScoreBase.COLUMN_NAME_ID+", "+ScoreBase.COLUMN_NAME_USERNAME+", "+ScoreBase.COLUMN_NAME_DATE+", "+ScoreBase.COLUMN_NAME_POINT+
       		" FROM " + ScoreBase.TABLE_NAME + 
       		" WHERE " + ScoreBase.COLUMN_NAME_LEVEL +" = "+ level +
       		" ORDER BY "+ ScoreBase.COLUMN_NAME_POINT;
    
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
      
       //Looping through all rows and adding to list
       if (cursor.moveToFirst()) {
           do {
        	   //Create new Score with values
               Score score = new Score();
               score.setId(cursor.getInt(0));
               score.setUsername(cursor.getString(1));
               score.setDate(new Date(cursor.getLong(2)));
               score.setPoint(cursor.getInt(3));
               //Adding score to list
               scoreList.add(score);
           } while (cursor.moveToNext());
       }
       cursor.close();
       db.close();
       
       // return score list
       return scoreList;
    }
    
    //Getting scores Count
    public int getScoresCount() {
        String countQuery = "SELECT  * FROM " + ScoreBase.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        db.close();
        
        //Return count
        return cursor.getCount();
    }
    
    //Updating single score
    public int updateScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        //Values
        ContentValues values = new ContentValues();
        values.put(ScoreBase.COLUMN_NAME_USERNAME, score.getUsername());
        values.put(ScoreBase.COLUMN_NAME_DATE, score.getDate().getTime());
        values.put(ScoreBase.COLUMN_NAME_LEVEL, score.getLevel());
        values.put(ScoreBase.COLUMN_NAME_TIME, score.hasTimer());
        values.put(ScoreBase.COLUMN_NAME_TIME, score.getTime());
        values.put(ScoreBase.COLUMN_NAME_MOVE, score.getMove());
        values.put(ScoreBase.COLUMN_NAME_WIN, score.isWin());
        values.put(ScoreBase.COLUMN_NAME_BONUS, score.getBonus());
        values.put(ScoreBase.COLUMN_NAME_POINT, score.getPoint());
     
        String selection = ScoreBase.COLUMN_NAME_ID + "=?";
        String[] arrayArguments = new String[] { String.valueOf(score.getId()) };
        
        //Updating row
        int nbRowUpdated = db.update(ScoreBase.TABLE_NAME, values, selection, arrayArguments);
        db.close();
        
        return nbRowUpdated;
    }
    
    //Deleting single score
    public void deleteScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        String selection = ScoreBase.COLUMN_NAME_ID + "=?";
        String[] arrayArguments = new String[] { String.valueOf(score.getId()) };
        
        db.delete(ScoreBase.TABLE_NAME, selection, arrayArguments);
        db.close();
    }
    
    //Deleting single score
    public void deleteAllScore() {
        SQLiteDatabase db = this.getWritableDatabase();     
        
        db.delete(ScoreBase.TABLE_NAME, null, null);
        db.close();
    }
}