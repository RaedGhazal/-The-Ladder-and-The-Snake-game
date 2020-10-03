package com.raedghazal.thegametask.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.raedghazal.thegametask.db.DatabaseHelper.*;

import com.raedghazal.thegametask.models.Game;
import com.raedghazal.thegametask.models.Player;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseController {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DatabaseController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public int addPlayer(Player player) {
        db = dbHelper.getWritableDatabase();
        if (checkUsernameIfExist(player.getUsername())) {
            Log.e("addUser: ", "username already exists !");
            return -1;
        }
        db.execSQL("insert into " + TABLE_PLAYERS + " ("
                + COL_USER_NAME + ","
                + COL_PASSWORD + ","
                + COL_FULL_NAME
                + ") values(?,?,?)", new String[]{player.getUsername(), player.getPassword(), player.getFullName()});
        return getPlayerByUsername(player.getUsername()).getPlayerID();
    }

    public Player getPlayerByUsername(String userName) {
        Player user = new Player();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PLAYERS + " where " + COL_USER_NAME + "=?", new String[]{userName});
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.setPlayerID(cursor.getInt(cursor.getColumnIndex(COL_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(COL_USER_NAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COL_PASSWORD)));
            user.setFullName(cursor.getString(cursor.getColumnIndex(COL_FULL_NAME)));
        }
        cursor.close();
        return user;
    }

    public boolean checkUsernameIfExist(String username) {
        boolean exists = false;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PLAYERS + " where " + COL_USER_NAME + "=?", new String[]{username});
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
            exists = true;
        cursor.close();
        return exists;
    }

    public boolean checkPasswordIfCorrect(String username, String password) {
        boolean correct = false;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PLAYERS
                        + " where " + COL_USER_NAME + "=?"
                        + " and " + COL_PASSWORD + "=?"
                , new String[]{username, password});
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
            correct = true;
        cursor.close();
        return correct;
    }

    public void addGame(Game game) {
        String gameDate = SQLITE_FORMAT.format(game.getDate());
        db = dbHelper.getWritableDatabase();
        db.execSQL("insert into " + TABLE_GAMES + " ("
                + COL_PLAYER_ID + ","
                + COL_PLAYER_1_LOCATION + ","
                + COL_PLAYER_2_LOCATION + ","
                + COL_TOTAL_DICE_ROLLS + ","
                + COL_SAVE_DATE
                + ") values(?,?,?,?,?)", new String[]{
                String.valueOf(game.getPlayerId()),
                String.valueOf(game.getPlayer1Location()),
                String.valueOf(game.getPlayer2Location()),
                String.valueOf(game.getTotalDiceRolls()),
                gameDate
        });
    }

    public void updateSavedGame(Game game) {
        String gameDate = SQLITE_FORMAT.format(game.getDate());
        db = dbHelper.getWritableDatabase();
        db.execSQL("update " + TABLE_GAMES + " set "
                        + COL_PLAYER_1_LOCATION + "=?,"
                        + COL_PLAYER_2_LOCATION + "=?,"
                        + COL_TOTAL_DICE_ROLLS + "=?,"
                        + COL_SAVE_DATE + "=?"
                        + " where " + COL_ID + "=?"
                , new String[]{
                        String.valueOf(game.getPlayer1Location()),
                        String.valueOf(game.getPlayer2Location()),
                        String.valueOf(game.getTotalDiceRolls()),
                        gameDate,
                        String.valueOf(game.getGameId())});
    }

    public void removeSavedGame(Game game) {
        db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + TABLE_GAMES + " where " + COL_ID + "=?", new String[]{String.valueOf(game.getGameId())});
    }

    public Game getGameByGameId(int gameId) {
        Game game = new Game();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_GAMES + " where " + COL_ID + "=?", new String[]{String.valueOf(gameId)});
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            game.setGameId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
            game.setPlayerId(cursor.getInt(cursor.getColumnIndex(COL_PLAYER_ID)));
            game.setPlayer1Location(cursor.getInt(cursor.getColumnIndex(COL_PLAYER_1_LOCATION)));
            game.setPlayer2Location(cursor.getInt(cursor.getColumnIndex(COL_PLAYER_2_LOCATION)));
            game.setTotalDiceRolls(cursor.getInt(cursor.getColumnIndex(COL_TOTAL_DICE_ROLLS)));
            String strDate = (cursor.getString(cursor.getColumnIndex(COL_SAVE_DATE)));
            Date gameDate;
            try {
                gameDate = SQLITE_FORMAT.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
                gameDate = new Date();
            }
            game.setDate(gameDate);
        }
        cursor.close();
        return game;
    }

    public ArrayList<Game> getAllGamesByPlayerId(int playerId) {
        ArrayList<Game> games = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor =
                db.rawQuery("select * from " + TABLE_GAMES + " where " + COL_PLAYER_ID + "=?"
                        , new String[]{String.valueOf(playerId)});
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                Game game = new Game();
                game.setGameId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                game.setPlayerId(cursor.getInt(cursor.getColumnIndex(COL_PLAYER_ID)));
                game.setPlayer1Location(cursor.getInt(cursor.getColumnIndex(COL_PLAYER_1_LOCATION)));
                game.setPlayer2Location(cursor.getInt(cursor.getColumnIndex(COL_PLAYER_2_LOCATION)));
                game.setTotalDiceRolls(cursor.getInt(cursor.getColumnIndex(COL_TOTAL_DICE_ROLLS)));
                String strDate = (cursor.getString(cursor.getColumnIndex(COL_SAVE_DATE)));
                Date gameDate;
                try {
                    gameDate = SQLITE_FORMAT.parse(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    gameDate = new Date();
                }
                game.setDate(gameDate);
                games.add(game);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return games;
    }
}
