package fr.esgi.android.project.esgi_memory.db;

import android.provider.BaseColumns;

public final class DatabaseContract {
	
    // To prevent someone from accidentally instantiating the contract class, give it an empty constructor.
    public DatabaseContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ScoreBase implements BaseColumns {
        public static final String TABLE_NAME = "score";
        public static final String COLUMN_NAME_ID = "scoreid";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_LEVEL = "level";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_MOVE = "move";
        public static final String COLUMN_NAME_WIN = "win";
        public static final String COLUMN_NAME_BONUS = "bonus";
        public static final String COLUMN_NAME_POINT = "point";
    }
    
   
}