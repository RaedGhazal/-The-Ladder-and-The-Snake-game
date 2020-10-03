package com.raedghazal.thegametask.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.raedghazal.thegametask.models.Game;
import com.raedghazal.thegametask.models.Player;
import com.raedghazal.thegametask.R;
import com.raedghazal.thegametask.adapters.SavedGamesAdapter;
import com.raedghazal.thegametask.SharedPreferencesHandler;
import com.raedghazal.thegametask.db.DatabaseController;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseController databaseController;
    private SharedPreferencesHandler sharedPreferencesHandler;
    private TextView tvHello;
    private RecyclerView rvSavedGames;
    private Player player = new Player();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        if (!checkPlayerStatues()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        fillRvWithSavedGames();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillRvWithSavedGames();
    }

    private void initialization() {
        databaseController = new DatabaseController(this);
        tvHello = findViewById(R.id.tv_hello);
        rvSavedGames = findViewById(R.id.rv_saved_games);
    }

    private boolean checkPlayerStatues() {
        boolean loggedIn;
        sharedPreferencesHandler = new SharedPreferencesHandler(this);
        loggedIn = sharedPreferencesHandler.get().getBoolean(SharedPreferencesHandler.LOGGED_IN, false);
        //If not logged in yet.
        if (!loggedIn)
            return false;
        String username = sharedPreferencesHandler.get().getString(SharedPreferencesHandler.PLAYER_USERNAME, "");
        player = databaseController.getPlayerByUsername(username);
        setHelloMessage();
        return true;
    }

    private void setHelloMessage() {
        SpannableString strWelcome = new SpannableString(String.format(getResources().getString(R.string.helloMessage), player.getFullName()));
        strWelcome.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvHello.setText(strWelcome);
    }

    private void fillRvWithSavedGames() {
        ArrayList<Game> games = databaseController.getAllGamesByPlayerId(player.getPlayerID());
        SavedGamesAdapter savedGamesAdapter = new SavedGamesAdapter(this, games);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvSavedGames.setItemAnimator(new DefaultItemAnimator());
        rvSavedGames.setLayoutManager(layoutManager);
        rvSavedGames.setAdapter(savedGamesAdapter);
    }

    public void logout(View view) {
        sharedPreferencesHandler.putBoolean(SharedPreferencesHandler.LOGGED_IN, false);
        sharedPreferencesHandler.putInt(SharedPreferencesHandler.PLAYER_ID, -1);
        sharedPreferencesHandler.putString(SharedPreferencesHandler.PLAYER_USERNAME, "");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void clickCreateNewGame(View view) {
        startActivity(new Intent(MainActivity.this, GameActivity.class));
    }
}