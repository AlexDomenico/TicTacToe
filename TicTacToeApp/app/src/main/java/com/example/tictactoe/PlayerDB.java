package com.example.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

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
        Cursor cursor = db.rawQuery("SELECT name, wins, losses, ties FROM players", null);
        while (((Cursor) cursor).moveToNext()){
            HashMap<String, String> map = new HashMap<String, String>();
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

    HashMap<String, String> getPlayer(int index){
        HashMap<String, String> data =
                new HashMap<String, String>();
        openReadableDB();
        String p_querry = "SELECT id, name, wins, losses, ties FROM players WHERE id = ?";
        Cursor cursor = db.rawQuery(p_querry, new String[] {String.valueOf(index + 1)}, null);

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

    void UpdatePlayer(HashMap<String, String> player) throws Exception{
        openWriteableDB();

        ContentValues content = new ContentValues();

        content.put("name", String.valueOf(player.get("name")));

        String where_querry = "id = ?";
        long nResult = db.update("players", content, where_querry, new String[] {String.valueOf(player.get("id"))});
        if (nResult == -1) {
            throw new Exception("no data");
        }

        closeDB();
    }

    void DeletePlayer(HashMap<String, String> player) throws  Exception{
        openWriteableDB();

        String where_querry = "id = ?";
        long nResult = db.delete("players", where_querry, new String[]{String.valueOf(player.get("id"))});
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
}
