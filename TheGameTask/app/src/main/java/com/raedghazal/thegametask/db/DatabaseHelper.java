package com.raedghazal.thegametask.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_GAMES = "games";
    public static final String TABLE_PLAYERS = "players";
    public static final String COL_ID = "id";

    public static final String COL_PLAYER_ID = "player_id";
    public static final String COL_PLAYER_1_LOCATION = "player_1_location";
    public static final String COL_PLAYER_2_LOCATION = "player_2_location";
    public static final String COL_TOTAL_DICE_ROLLS = "total_dice_rolls";
    public static final String COL_SAVE_DATE = "save_date";

    public static final String COL_USER_NAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FULL_NAME = "full_name";

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat SQLITE_FORMAT = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    public DatabaseHelper(Context context) {
        super(context, "the_game.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // date format yyyy/MM/dd hh:mm:ss
        db.execSQL("CREATE TABLE " + TABLE_GAMES + " ("
                + COL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + COL_PLAYER_ID + " INTEGER(20) NOT NULL,"
                + COL_PLAYER_1_LOCATION + " INTEGER(2) NOT NULL,"
                + COL_PLAYER_2_LOCATION + " INTEGER(2) NOT NULL,"
                + COL_TOTAL_DICE_ROLLS + " INTEGER(14) NOT NULL DEFAULT 0,"
                + COL_SAVE_DATE + " DATETIME NOT NULL DEFAULT current_timestamp" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_PLAYERS + " ("
                + COL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + COL_USER_NAME + " VARCHAR(32) NOT NULL UNIQUE,"
                + COL_PASSWORD + " VARCHAR(256) NOT NULL,"
                + COL_FULL_NAME + " VARCHAR(300) NOT NULL" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
