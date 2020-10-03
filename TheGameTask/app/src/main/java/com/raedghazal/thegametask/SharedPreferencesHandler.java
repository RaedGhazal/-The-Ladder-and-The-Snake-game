package com.raedghazal.thegametask;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings("unused")
public class SharedPreferencesHandler {

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    public final static String LOGGED_IN = "logged_in";
    public final static String PLAYER_ID = "player_id";
    public final static String PLAYER_USERNAME = "player_username";

    public SharedPreferencesHandler(Context context) {
        sharedpreferences = context.getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public SharedPreferences get() {
        return sharedpreferences;
    }

    public void login(int playerId, String username) {
        editor = sharedpreferences.edit();
        putBoolean(LOGGED_IN, true);
        putInt(PLAYER_ID, playerId);
        putString(PLAYER_USERNAME, username);
        editor.apply();
    }

    public void logOut() {
        editor = sharedpreferences.edit();
        putBoolean(LOGGED_IN, false);
        editor.apply();
    }

}
