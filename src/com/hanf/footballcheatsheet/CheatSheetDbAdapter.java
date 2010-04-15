package com.hanf.footballcheatsheet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CheatSheetDbAdapter {
    
	// Database and table names
    private static final String DATABASE_NAME = "CheatSheetDb";
    private static final String DRAFT_TABLE = "DraftTb";
    private static final String MANAGER_TABLE = "ManagerTb";
    private static final String DRAFTMANAGER_TABLE = "DraftManagerTb";
    private static final String DRAFTMANAGERPLAYER_TABLE = "DraftManagerPickTb";
    private static final String PLAYER_TABLE = "PlayerTb";
    private static final int DATABASE_VERSION = 2;
	
	// DraftTB field names
	public static final String KEY_DRAFT_ROWID = "_id";
    public static final String KEY_DRAFT_NAME = "name";
    
    // ManagerTB field names
	public static final String KEY_MANAGER_ROWID = "_id";
    public static final String KEY_MANAGER_NAME = "name";
    
    // DraftManagerTB field names
	public static final String KEY_DRAFTMANAGER_ROWID = "_id";
    public static final String KEY_DRAFTMANAGER_DRAFTID = "draftId";
    public static final String KEY_DRAFTMANAGER_MANAGERID = "managerId";
    
    // DraftManagerPlayerTB field names
	public static final String KEY_DRAFTMANAGERPLAYER_ROWID = "_id";
    public static final String KEY_DRAFTMANAGERPLAYER_DRAFTMANAGERID = "draftManagerId";
    public static final String KEY_DRAFTMANAGERPLAYER_PLAYERID = "playerId";    
	
	// PlayerTB field names
    public static final String KEY_PLAYER_ROWID = "_id";
    public static final String KEY_PLAYER_NAME = "name";
    public static final String KEY_PLAYER_RANK = "rank";
    public static final String KEY_PLAYER_TEAM = "team";
    public static final String KEY_PLAYER_POSITION = "position";
    public static final String KEY_PLAYER_BYEWEEK = "byeweek";

    private static final String TAG = "CheatSheetDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Draft table creation SQL statement
     */
    private static final String DRAFT_TABLE_CREATE =
            "create table DraftTb (" +
            	"_id integer primary key autoincrement, " +
            	"name text not null);";
    
    /**
     * Manager table creation SQL statement
     */
    private static final String MANAGER_TABLE_CREATE =
            "create table ManagerTb (" +
            	"_id integer primary key autoincrement, " +
            	"name text not null);";
    
    /**
     * Draft Manager table creation SQL statement - links DraftTb to ManagerTb
     */
    private static final String DRAFTMANAGER_TABLE_CREATE =
            "create table DraftManagerTb (" +
            	"_id integer primary key autoincrement, " +
            	"draftId integer not null, " +
            	"managerId integer not null);";
    
    /**
     * Draft Manager Player table creation SQL statement - links DraftManagerTb to PlayerTb
     */
    private static final String DRAFTMANAGERPLAYER_TABLE_CREATE =
            "create table DraftManagerPlayerTb (" +
            	"_id integer primary key autoincrement, " +
            	"draftManagerId integer not null, " +
            	"playerId integer not null);";
    
    /**
     * Player table creation SQL statement
     */
    private static final String PLAYER_TABLE_CREATE =
            "create table PlayerTb (" +
            	"_id integer primary key autoincrement, " +
            	"name text not null, " +
            	"rank text not null, " +
            	"team text not null, " +
            	"position text not null, " +
            	"byeweek text not null);";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DRAFT_TABLE_CREATE);
            db.execSQL(MANAGER_TABLE_CREATE);
            db.execSQL(DRAFTMANAGER_TABLE_CREATE);
            db.execSQL(DRAFTMANAGERPLAYER_TABLE_CREATE);
            db.execSQL(PLAYER_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DRAFT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + MANAGER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DRAFTMANAGER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DRAFTMANAGERPLAYER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PLAYER_TABLE);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public CheatSheetDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the cheat sheet database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public CheatSheetDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }

    /**
     * Create a new player using the parameters provided. If the player is
     * successfully created return the new rowId for that player, otherwise
     * return a -1 to indicate failure.
     * 
     * @param Name the name of the player
     * @param Rank the projected ranking of the player
     * @param Team the team the player plays for
     * @param Position the position the player plays
     * @param ByeWeek the week the player will not be playing
     * @return rowId or -1 if failed
     */
    public long createPlayer(String Name, String Rank, String Team, String Position, String ByeWeek) {        
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PLAYER_NAME, Name);
        initialValues.put(KEY_PLAYER_RANK, Rank);
        initialValues.put(KEY_PLAYER_TEAM, Team);
        initialValues.put(KEY_PLAYER_POSITION, Position);
        initialValues.put(KEY_PLAYER_BYEWEEK, ByeWeek);

        return mDb.insert(PLAYER_TABLE, null, initialValues);
    }

    /**
     * Delete the players to start over
     * 
     * @return true if deleted, false otherwise
     */
    public boolean deleteAllPlayers() {

        return mDb.delete(PLAYER_TABLE, null, null) > 0;
    }

    /**
     * Return a Cursor containing the list of all players in the database
     * 
     * @return Cursor of all players
     */
    public Cursor fetchAllPlayers() {
        return mDb.query(
        	PLAYER_TABLE, 
        	new String[] {KEY_PLAYER_ROWID, KEY_PLAYER_NAME, KEY_PLAYER_RANK, KEY_PLAYER_TEAM, KEY_PLAYER_POSITION, KEY_PLAYER_BYEWEEK}, 
        	null, 
        	null, 
        	null, 
        	null, 
        	null);
    }

    /**
     * Return a Cursor with player detail for the player that matches the given rowId
     * 
     * @param rowId id of player to retrieve
     * @return Cursor positioned to matching player, if found
     * @throws SQLException if player could not be found/retrieved
     */
    public Cursor fetchPlayerDetail(long rowId) throws SQLException {
    	/* TODO: enhance to join player non-existent player detail table or add more info to player table */
        Cursor mCursor = mDb.query(
        			PLAYER_TABLE, 
                	new String[] {KEY_PLAYER_ROWID, KEY_PLAYER_NAME, KEY_PLAYER_RANK, KEY_PLAYER_TEAM, KEY_PLAYER_POSITION, KEY_PLAYER_BYEWEEK}, 
                	KEY_PLAYER_ROWID + "=" + rowId, 
                	null, 
                	null, 
                	null, 
                	null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
