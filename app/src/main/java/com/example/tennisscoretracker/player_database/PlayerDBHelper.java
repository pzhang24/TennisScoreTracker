package com.example.tennisscoretracker.player_database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

//https://www.youtube.com/watch?v=kDZES1wtKUY&t=4164s
//Only a single instance of PlayerDBHelper can be instantiated
//Done through the method getInstance(Context context)

/**
 * This is the class for our Player Database. The database stores the ID and Name of
 * each player (both must be unique) as well as the number of wins and losses
 * associated with each player.
 */

//TODO: DELETING DATABASE (FOR TESTING PURPOSES)

public class PlayerDBHelper extends SQLiteOpenHelper {

    private static PlayerDBHelper dbStaticInstance;

    private static final String DATABASE_NAME = "player.db";
    private static final String TABLE_NAME = "player";
    private static final String COL_1 = "playerID";
    private static final String COL_2 = "playerName";
    private static final String COL_3 = "wins";
    private static final String COL_4 = "losses";

    private static final int PLAYER_ID_COLUMN = 1;
    private static final int PLAYER_NAME_COLUMN = 2;
    private static final int PLAYER_WINS_COLUMN = 3;
    private static final int PLAYER_LOSSES_COLUMN = 4;

    private static final int DB_INSERT_ERROR = -1;

    private static final int MAX_PLAYER_CAPACITY = 30;

    /**
     * Private constructor - used in getInstance() method
     * @param context the current context
     */
    private PlayerDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    /**
     * Creates a static instance of PlayerDBHelper if it does not already exist.
     * Otherwise returns the existing object.
     * @param context the current context.
     * @return a static reference to an instance of PlayerDBHelper class.
     */
    public static PlayerDBHelper getInstance(Context context) {
        //Uses application context and not activity context -- prevents leaks?
        //https://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
        if(dbStaticInstance == null) {
            dbStaticInstance = new PlayerDBHelper(context.getApplicationContext());
        }
        return dbStaticInstance;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO: CLEAN UP THIS MESS
        db.execSQL("create table " + TABLE_NAME + "(playerID integer primary key, " +
                "playerName text, wins integer, losses integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists player");
        onCreate(db);
    }

    /**
     * Creates a new player in the database.
     * @param playerName name of new player to include.
     * @return true if player was successfully inserted into database, and false otherwise.
     * Throws PlayerNameAlreadyExistsException if player name is already taken (ie in the database).
     */
    public boolean insertNewPlayer(String playerName)
            throws PlayerNameAlreadyExistsException, TooManyPlayersException {

        SQLiteDatabase db = dbStaticInstance.getWritableDatabase();

        if(checkPlayerNameAlreadyExist(playerName)) {
            throw new PlayerNameAlreadyExistsException("Player Name Already Taken!");
        } else if (getNumberOfPlayers() >= MAX_PLAYER_CAPACITY) {
            throw new TooManyPlayersException("Reached maximum limit of players! Limit = " +
                    MAX_PLAYER_CAPACITY);
        }


        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, playerName);

        //New players initially have zero wins and losses
        contentValues.put(COL_3, 0);
        contentValues.put(COL_4, 0);

        long insertResult = db.insert(TABLE_NAME, null, contentValues);

        return insertResult != DB_INSERT_ERROR;
    }

    /**
     * Gets all data for all players in the database at the time the method is called.
     * @return an instance of Cursor containing all player data.
     */
    public Cursor getAllPlayerData() {
        SQLiteDatabase db = dbStaticInstance.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    /**
     * Provides a mapping of playerID to playerName for each player in the database
     * at the time the method is called.
     * @return a new HashMap of unique ID-Name pairings.
     * All ID's should be between 1 and getNumberOfPlayers() - 1
     * [When called before any mutation methods are called].
     */
    public HashMap<Integer, String> getPlayerIDToNameMapping() {
        Cursor cursor = getAllPlayerData();

        @SuppressLint("UseSparseArrays")
        HashMap<Integer, String> playerIDNameMap = new HashMap<>();

        while(cursor.moveToNext()) {
            playerIDNameMap.put(cursor.getInt(PLAYER_ID_COLUMN), cursor.getString(PLAYER_NAME_COLUMN));
        }

        return playerIDNameMap;
    }

    /**
     * Provides a mapping of playerName to playerID for each player in the database
     * at the time the method is called.
     * @return a new HashMap of unique Name-ID pairings.
     * All ID's should be between 1 and getNumberOfPlayers() - 1
     * [When called before any mutation methods are called].
     */
    public HashMap<String, Integer> getPlayerNameToIDMapping() {
        Cursor cursor = getAllPlayerData();
        HashMap<String, Integer> playerNameIDMap = new HashMap<>();
        while(cursor.moveToNext()) {
            playerNameIDMap.put(cursor.getString(PLAYER_NAME_COLUMN), cursor.getInt(PLAYER_ID_COLUMN));
        }

        return playerNameIDMap;
    }

    /**
     * Gets the names of all players in the database at the time the method is called.
     * @return a new ArrayList containing strings of the names of all players in the database.
     */
    public ArrayList<String> getPlayerNamesList() {
        Cursor cursor = getAllPlayerData();
        ArrayList<String> playerList = new ArrayList<>();
        while(cursor.moveToNext()) {
            playerList.add(cursor.getString(PLAYER_NAME_COLUMN));
        }

        return playerList;
    }

    /**
     * Gets the number of players in the database at the time the method is called.
     * @return the number of players in the database. Should return an integer greater than or equal to 0.
     */
    public int getNumberOfPlayers() {
        Cursor cursor = getAllPlayerData();
        return cursor.getCount();
    }

    /**
     * Updates the name of a player in the database.
     * Calling this method mutates our database.
     * @param playerID the ID of the player whose name we want to change.
     *                 Must be between 0 and number of rows in database - 1
     *                 (ie ID is in the database).
     * @param newPlayerName the new name of the player.
     * @return true if player's name was successfully updated, and false otherwise.
     * throws PlayerNameAlreadyExistsException if the player name is already taken (ie in the database).
     */
    public boolean updatePlayerName(int playerID, String newPlayerName) throws PlayerNameAlreadyExistsException {
        SQLiteDatabase db = dbStaticInstance.getWritableDatabase();
        if(checkPlayerNameAlreadyExist(newPlayerName)) {
            throw new PlayerNameAlreadyExistsException("Player name already exists!");
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, newPlayerName);
        int isSuccess = db.update(TABLE_NAME, contentValues, COL_1 + " =? ",
                new String[] {String.valueOf(playerID)});

        //Checks whether any rows were updated or not (By design only one row should be updated --
        //consider testing)
        return isSuccess > 0;

        /* SQLQuery we want to execute
        String query = "UPDATE " +  TABLE_NAME + " SET " + COL_2 + " = " + newName +
                 " WHERE " + COL_1 + " = " + playerID;
        */
    }

    /**
     * Deletes a player from the database given their playerID.
     * Calling this method mutates the database
     * @param playerID the ID of the player whose name we want to change.
     *      Must be between 0 and number of rows in database - 1 (ie ID is in the database).
     * @return true if player was successfully deleted, and false otherwise.
     */
    public boolean deletePlayer(int playerID) {
        SQLiteDatabase db = dbStaticInstance.getWritableDatabase();
        int isSuccess = db.delete(TABLE_NAME, "ID = ?",
                new String[] {String.valueOf(playerID)});

        //Checks whether any rows were deleted or not (By design only one row should be updated --
        //consider testing)
        return isSuccess > 0;
    }

    /**
     * Deletes all players from the database.
     * Calling this method mutates the database.
     */
    public void deleteAllPlayers() {
        SQLiteDatabase db = dbStaticInstance.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }


    /**
     * Checks if a player's name is already in the database
     * @param playerName name to search
     * @return true if player's name is already in the database, and false if not
     */
    private boolean checkPlayerNameAlreadyExist(String playerName) {
        SQLiteDatabase db = dbStaticInstance.getWritableDatabase();
        String query = "SELECT " + COL_2 + " FROM " + TABLE_NAME + " WHERE " + COL_2 + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{playerName});
        int count = cursor.getCount();
        cursor.close();
        return count <= 0;
    }

    //Inexpensive Getter methods

    /**
     * Gets the playerID of a player in our database given their playerName.
     * @param playerName name of player.
     * @return the unique ID of the player stored in the database.
     * @throws PlayerDoesNotExistException if no player with name playerName
     *  in our database is found.
     */
    public int getIDFromName(String playerName) throws PlayerDoesNotExistException {
        SQLiteDatabase db = dbStaticInstance.getWritableDatabase();
        Cursor cursor = executeSimpleSelectQuery(db, TABLE_NAME, COL_1, COL_2, playerName);

        int count = cursor.getCount();
        if(count <= 0) {
            throw new PlayerDoesNotExistException("No player with name: " + playerName);
        }

        //Only one row should be contained in the cursor since all player names are unique
        int id = getFirstIntQueryResult(cursor);
        cursor.close();

        return id;

    }

    /**
     * Gets the playerName of a player in our database given their playerID.
     * @param playerID unique ID of player.
     * @return the unique playerName of the player stored in the database.
     * @throws PlayerDoesNotExistException if no player with id number playerID
     *  in our database is found.
     */
    public String getNameFromID(int playerID) throws PlayerDoesNotExistException {
        SQLiteDatabase db = dbStaticInstance.getWritableDatabase();
        Cursor cursor = executeSimpleSelectQuery(db, TABLE_NAME,
                COL_2, COL_1, String.valueOf(playerID));

        int count = cursor.getCount();
        if(count <= 0) {
            throw new PlayerDoesNotExistException("No player with id: " + playerID);
        }

        //Only one row should be contained in the cursor since all player names are unique
        String name = getFirstStringQueryResult(cursor);
        cursor.close();

        return name;
    }

    /**
     * Gets the number of wins of a player in our database given their playerID.
     * @param playerID unique ID of player.
     * @return the number of wins associated with the player in our database
     * @throws PlayerDoesNotExistException if no player with id number playerID
     *  in our database is found.
     */
    public int getWinsFromID(int playerID) throws PlayerDoesNotExistException {
        SQLiteDatabase db = dbStaticInstance.getWritableDatabase();
        Cursor cursor = executeSimpleSelectQuery(db, TABLE_NAME,
                COL_3, COL_1, String.valueOf(playerID));

        int count = cursor.getCount();
        if(count <= 0) {
            throw new PlayerDoesNotExistException("No player with id: " + playerID);
        }

        //Only one row should be contained in the cursor since all player names are unique

        int wins = getFirstIntQueryResult(cursor);
        cursor.close();

        return wins;
    }

    /**
     * Gets the number of wins of a player in our database given their playerID.
     * @param playerID unique ID of player.
     * @return the number of wins associated with the player in our database
     * @throws PlayerDoesNotExistException if no player with id number playerID
     *  in our database is found.
     */
    public int getLossesFromID(int playerID) throws PlayerDoesNotExistException {
        SQLiteDatabase db = dbStaticInstance.getWritableDatabase();
        Cursor cursor = executeSimpleSelectQuery(db, TABLE_NAME,
                COL_4, COL_1, String.valueOf(playerID));

        int count = cursor.getCount();
        if(count <= 0) {
            throw new PlayerDoesNotExistException("No player with id: " + playerID);
        }

        //Only one row should be contained in the cursor since all player names are unique

        int losses = getFirstIntQueryResult(cursor);
        cursor.close();

        return losses;
    }


    /**
     * Executes a simple SQLIte search query and returns a new Cursor containing the results.
     * @param db the SQLite database.
     * @param tableName the table where we wish to obtain values from.
     * @param targetColumn the database column where we wish to obtain values from.
     *                     Must exist in the table.
     * @param predicateColumn the database column where we wish to check our predicate condition.
     *                        Must exist in the table.
     * @param predicateValue the predicate value to check.
     * @return a new Cursor object containing the results of the query.
     */
    private Cursor executeSimpleSelectQuery(
            SQLiteDatabase db, String tableName, String targetColumn, String predicateColumn,
            String predicateValue) {

        String query = "SELECT " + targetColumn + " FROM " + tableName + " WHERE "
                + predicateColumn + "=?";
        return db.rawQuery(query, new String[]{predicateValue});

    }

    /**Helper method.
     * Obtains the integer value in the first row and column of a cursor.
     * The position of the cursor is moved to the first row when this method is called.
     * @param cursor a cursor object
     * @return the int value in the first row and column of our cursor object.
     */
    private int getFirstIntQueryResult(Cursor cursor) {
        cursor.moveToFirst();
        return cursor.getInt(0); //zero-index since only one column in cursor
    }

    /**Helper method
     * Obtains the String value in the first row and column of a cursor.
     * The position of the cursor is moved to the first row when this method is called.
     * @param cursor a cursor object
     * @return the String value in the first row and column of our cursor object.
     */
    private String getFirstStringQueryResult(Cursor cursor) {
        cursor.moveToFirst();
        return cursor.getString(0); //zero-index since only one column in cursor
    }
}
