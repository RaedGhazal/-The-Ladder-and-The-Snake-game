package com.raedghazal.thegametask.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raedghazal.thegametask.Board;
import com.raedghazal.thegametask.R;
import com.raedghazal.thegametask.SaveGameDialog;
import com.raedghazal.thegametask.SharedPreferencesHandler;
import com.raedghazal.thegametask.adapters.SnakeAndLadderAdapter;
import com.raedghazal.thegametask.db.DatabaseController;
import com.raedghazal.thegametask.models.Game;
import com.raedghazal.thegametask.models.SnakeLadder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    public static ArrayList<SnakeLadder> getSnakesAndLadders() {
        ArrayList<SnakeLadder> snakeLadders = new ArrayList<>();
        snakeLadders.add(new SnakeLadder(11, 2, SnakeLadder.Type.SNAKE));
        snakeLadders.add(new SnakeLadder(23, 5, SnakeLadder.Type.SNAKE));
        snakeLadders.add(new SnakeLadder(16, 8, SnakeLadder.Type.SNAKE));
        snakeLadders.add(new SnakeLadder(13, 22, SnakeLadder.Type.LADDER));
        snakeLadders.add(new SnakeLadder(7, 14, SnakeLadder.Type.LADDER));
        snakeLadders.add(new SnakeLadder(3, 10, SnakeLadder.Type.LADDER));
        return snakeLadders;
    }

    private TextView tvTotalRolls, tvDiceResult, tvCurrentPlayer;
    private GridView gvGame;
    private RecyclerView rvInstructions;
    private Game game;
    private DatabaseController databaseController;
    private Board board;
    private SaveGameDialog saveGameDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initialization();
    }

    private void initialization() {
        tvTotalRolls = findViewById(R.id.tv_total_rolls);
        tvDiceResult = findViewById(R.id.tv_dice_result);
        tvCurrentPlayer = findViewById(R.id.tv_current_player);
        rvInstructions = findViewById(R.id.rv_instructions);
        gvGame = findViewById(R.id.gv_game);

        game = new Game();
        databaseController = new DatabaseController(this);
        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(this);

        Bundle bundle = getIntent().getExtras();
        boolean isLoaded = false;
        if (bundle != null)
            isLoaded = bundle.getBoolean("isLoaded", false);
        if (isLoaded) {
            int gameId = bundle.getInt("gameId", -1);
            game = databaseController.getGameByGameId(gameId);
        } else {
            game.setPlayerId(sharedPreferencesHandler.get().getInt(SharedPreferencesHandler.PLAYER_ID, 0));
        }

        fillGameBoard();
    }

    private void fillGameBoard() {
        board = new Board(this, game);

        //set board gridView
        gvGame.setAdapter(board.getAdapter());
        tvTotalRolls.setText(String.valueOf(game.getTotalDiceRolls()));
        board.setOnTurnFinish(() -> tvCurrentPlayer.setText(String.format(getResources().getString(R.string.currentPlayerNumber), board.getCurrentPlayer())));

        board.setOnGameFinish(() -> {
            if (game.getGameId() > -1) {
                databaseController.removeSavedGame(game);
            }
        });

        //set snake and ladder recyclerView
        SnakeAndLadderAdapter snakeAndLadderAdapter = new SnakeAndLadderAdapter(this, getSnakesAndLadders());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvInstructions.setLayoutManager(layoutManager);
        rvInstructions.setItemAnimator(new DefaultItemAnimator());
        rvInstructions.setAdapter(snakeAndLadderAdapter);

        //set current player text and total dice rolls
        tvCurrentPlayer.setText(String.format(getResources().getString(R.string.currentPlayerNumber), 1));
        tvDiceResult.setText(String.valueOf(game.getTotalDiceRolls()));
    }

    private void askToSaveGame() {
        saveGameDialog = new SaveGameDialog(this);
        saveGameDialog.show();
    }

    private int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    private void saveGame() {
        if (game.getGameId() > -1) {
            game.setDate(Calendar.getInstance().getTime());
            databaseController.updateSavedGame(game);
        } else {
            game.setDate(Calendar.getInstance().getTime());
            databaseController.addGame(game);
        }
    }

    public void clickNewGame(View view) {
        askToSaveGame();
        saveGameDialog.setOnDismissListener(dialog -> {
            if (((SaveGameDialog) dialog).getAction() == SaveGameDialog.Action.SAVE) {
                saveGame();
            }
            if (((SaveGameDialog) dialog).getAction() != SaveGameDialog.Action.CANCEL) {
                game = new Game();
                getIntent().putExtra("isLoaded", false);
                recreate();
            }
        });
    }

    public void clickSaveGame(View view) {
        saveGame();
        finish();
    }

    public void clickRollDice(View view) {
        final int random = getRandomNumber();
        board.setDiceResult(random);
        tvDiceResult.setText(String.valueOf(random));
        tvTotalRolls.setText(String.valueOf(game.getTotalDiceRolls()));
    }

    @Override
    public void onBackPressed() {
        askToSaveGame();
        saveGameDialog.setOnDismissListener(dialog -> {
            if (((SaveGameDialog) dialog).getAction() == SaveGameDialog.Action.SAVE) {
                saveGame();
            }
            if (((SaveGameDialog) dialog).getAction() != SaveGameDialog.Action.CANCEL)
                finish();
        });
    }
}