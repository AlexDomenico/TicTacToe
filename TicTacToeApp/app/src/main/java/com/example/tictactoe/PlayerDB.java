package com.example.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class PlayerDB {
    //database constants
    public static final String DB_NAME = "player.sqlite";
    public static final int DB_VERSION = 1;

    public static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create tables
            db.execSQL("CREATE TABLE players (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name VARCHAR NOT NULL," +
                    "wins INTEGER NOT NULL DEFAULT 0," +
                    "losses INTEGER NOT NULL DEFAULT 0," +
                    "ties INTEGER NOT NULL DEFAULT 0)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE \"players\"");
            Log.d("Task list",
                    "Upgrading db from version " + oldVersion + " to " + newVersion);
            onCreate(db);
        }
    }

    // database and database helper object
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public PlayerDB(Context context){
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        openWriteableDB();
        closeDB();
    }

    // private methods
    private void openReadableDB(){ db = dbHelper.getReadableDatabase();}

    private void openWriteableDB(){
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB(){
        if (db != null){
            db.close();
        }
    }

    ArrayList<HashMap<String, String>> getPlayers(){
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT name, wins, losses, ties, id FROM players ORDER BY wins DESC", null);
        while (((Cursor) cursor).moveToNext()){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", cursor.getString(4));
            map.put("name", cursor.getString(0));
            map.put("wins", cursor.getString(1));
            map.put("losses", cursor.getString(2));
            map.put("ties", cursor.getString(3));
            data.add(map);
        }
        if (cursor != null){
            cursor.close();
        }
        closeDB();

        return data;
    }

    void insertPlayer(String sName) throws Exception{
        openWriteableDB();
        ContentValues content = new ContentValues();
        content.put("name", sName);
        long nResult = db.insert("players", null, content);
        if (nResult == -1) {
            throw new Exception("no data");
        }
        closeDB();
    }

    HashMap<String, String> getPlayer(long index){
        HashMap<String, String> data =
                new HashMap<String, String>();
        openReadableDB();
        String p_querry = "SELECT id, name, wins, losses, ties FROM players WHERE id = ?";
        Cursor cursor = db.rawQuery(p_querry, new String[] {String.valueOf(index)}, null);

        while (((Cursor) cursor).moveToNext()) {
            data.put("id", cursor.getString(0));
            data.put("name", cursor.getString(1));
            data.put("wins", cursor.getString(2));
            data.put("losses", cursor.getString(3));
            data.put("ties", cursor.getString(4));
        }

        if (cursor != null){
            cursor.close();
        }
        closeDB();
        return data;
    }

    void UpdatePlayer(String player, String newUserName) throws Exception{
        openReadableDB();

        ContentValues content = new ContentValues();
        content.put("name", newUserName);

        String p_querry = "SELECT id, name, wins, losses, ties FROM players WHERE name = ?";

        Cursor cursor = db.rawQuery(p_querry, new String[] {player}, null);

        ContentValues values = new ContentValues();
        while (((Cursor) cursor).moveToNext()) {
            values.put("name", newUserName);
            values.put("wins", cursor.getString(2));
            values.put("losses", cursor.getString(3));
            values.put("ties", cursor.getString(4));
        }

        closeDB();
        openWriteableDB();

        String where_querry = "name = ?";

        long nResult = db.update("players", values, where_querry, new String[] {player});
        if (nResult == -1) {
            throw new Exception("no data");
        }

        closeDB();
    }

    void DeletePlayer(String player) throws  Exception{
        openWriteableDB();

        String where_querry = "name = ?";
        long nResult = db.delete("players", where_querry, new String[]{player});
        if (nResult == -1){
            throw new Exception("no data");
        }

        closeDB();
    }

    ArrayList<String> getNames(){
        ArrayList<String> namesList = new ArrayList<>();
        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT name FROM players", null);
        while (((Cursor) cursor).moveToNext()){
            namesList.add(cursor.getString(0));
        }
        if (cursor != null){
            cursor.close();
        }

        closeDB();
        return namesList;
    }

    void UpdateScores(String winnerName, String loserName, boolean tie) throws Exception {
        openReadableDB();

        String p_querry = "SELECT id, name, wins, losses, ties FROM players WHERE name = ?";

        //Winner values
        Cursor cursor = db.rawQuery(p_querry, new String[] {winnerName}, null);
        ContentValues winnerValues = new ContentValues();
        while (((Cursor) cursor).moveToNext()) {
            winnerValues.put("name", cursor.getString(1));
            winnerValues.put("wins", cursor.getString(2));
            winnerValues.put("losses", cursor.getString(3));
            winnerValues.put("ties", cursor.getString(4));
        }

        //Loser values
        cursor = db.rawQuery(p_querry, new String[] {loserName}, null);
        ContentValues loserValues = new ContentValues();
        while (((Cursor) cursor).moveToNext()) {
            loserValues.put("name", cursor.getString(1));
            loserValues.put("wins", cursor.getString(2));
            loserValues.put("losses", cursor.getString(3));
            loserValues.put("ties", cursor.getString(4));
        }

        closeDB();

        if (!tie){
            int winHolder = parseInt((String) winnerValues.get("wins")) + 1;
            winnerValues.put("wins", winHolder);
            int lossHolder = parseInt((String) loserValues.get("losses")) + 1;
            loserValues.put("losses", lossHolder);
        }
        else{
            int winnerTieHolder = parseInt((String) winnerValues.get("ties")) + 1;
            winnerValues.put("ties", winnerTieHolder);
            int loserTieHolder = parseInt((String) loserValues.get("ties")) + 1;
            loserValues.put("ties", loserTieHolder);
        }

        openWriteableDB();
        String where_querry = "name = ?";

        // Winner update
        long nResult = db.update("players", winnerValues, where_querry, new String[] {winnerName});
        if (nResult == -1) {
            throw new Exception("no data");
        }

        //Loser update
        nResult = db.update("players", loserValues, where_querry, new String[]{loserName});
        if (nResult == -1) {
            throw new Exception("no data");
        }

        closeDB();
    }
}
