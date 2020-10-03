package com.raedghazal.thegametask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.raedghazal.thegametask.adapters.GameBoardAdapter;
import com.raedghazal.thegametask.activities.GameActivity;
import com.raedghazal.thegametask.activities.WinActivity;
import com.raedghazal.thegametask.models.Game;
import com.raedghazal.thegametask.models.SnakeLadder;

import java.util.HashMap;

public class Board {
    public interface OnTurnFinish {
        void onTurnFinish();
    }

    public interface OnGameFinish {
        void onGameFinish();
    }

    public Game game;
    private Context context;
    private GameBoardAdapter adapter;
    boolean turn = true;
    private int changePosDelay = 180;
    private OnTurnFinish onTurnFinish;
    private OnGameFinish onGameFinish;

    public Board(Context context, Game game) {
        this.context = context;
        this.game = game;
        adapter = new GameBoardAdapter(context, game);
    }

    public void setDiceResult(int diceResult) {
        movePlayersToPositions(diceResult);
        game.addRoll();
    }

    private void movePlayersToPositions(int diceResult) {
        int player1 = game.getPlayer1Location();
        int player2 = game.getPlayer2Location();
        if (turn) {
            if (player1 + diceResult > 24) {
                finishTurn();
                return;
            }
            player1 += diceResult;
        } else {
            if (player2 + diceResult > 24) {
                finishTurn();
                return;
            }
            player2 += diceResult;
        }

        game.setPlayer1Location(player1);
        game.setPlayer2Location(player2);
        adapter.notifyDataSetChanged();

        managePlayers();


        if (player1 == 24 || player2 == 24) {
            Intent intent = new Intent(context, WinActivity.class);
            intent.putExtra("winner", player1 == 24 ? 1 : 2);
            context.startActivity(intent);
            onGameFinish.onGameFinish();
            ((Activity) context).finish();
        }
    }

    private void managePlayers() {
        if (game.getPlayer1Location() == game.getPlayer2Location() && game.getPlayer1Location() != 1)
            managePlayersMeeting();
        else
            manageIfReachedSnakeOrLadder();
    }

    private void manageIfReachedSnakeOrLadder() {
        HashMap<Integer, Integer> snakesAndLadders = SnakeLadder.toHashMap(GameActivity.getSnakesAndLadders());
        final Integer player1NextMove = snakesAndLadders.get(game.getPlayer1Location());
        if (player1NextMove != null && player1NextMove > 0) {
            new Handler().postDelayed(() -> {
                game.setPlayer1Location(player1NextMove);
                adapter.notifyDataSetChanged();
                managePlayersMeeting();
            }, changePosDelay);
            return;
        }

        final Integer player2NextMove = snakesAndLadders.get(game.getPlayer2Location());
        if (player2NextMove != null && player2NextMove > 0) {
            new Handler().postDelayed(() -> {
                game.setPlayer2Location(player2NextMove);
                adapter.notifyDataSetChanged();
                managePlayersMeeting();
            }, changePosDelay);
            return;
        }
        finishTurn();
    }

    private void managePlayersMeeting() {
        int player1 = game.getPlayer1Location();
        int player2 = game.getPlayer2Location();
        boolean outOfBounds = false;
        if (player1 == player2 && player1 != 1) {
            if (player1 % 2 == 0) {
                if (player2 - 5 > 0)
                    player2 -= 5;
                else
                    outOfBounds = true;
            } else {
                if (player1 - 5 > 0)
                    player1 -= 5;
                else
                    outOfBounds = true;
            }

            if (outOfBounds)
                if (turn)
                    --player1;
                else
                    --player2;


            final int finalPlayer = player1;
            final int finalPlayer1 = player2;
            new Handler().postDelayed(() -> {
                game.setPlayer1Location(finalPlayer);
                game.setPlayer2Location(finalPlayer1);
                adapter.notifyDataSetChanged();
                manageIfReachedSnakeOrLadder();
            }, changePosDelay);
            return;
        }
        finishTurn();
    }

    public int getCurrentPlayer() {
        return turn ? 1 : 2;
    }

    public GameBoardAdapter getAdapter() {
        return adapter;
    }

    private void finishTurn() {
        turn = !turn;
        onTurnFinish.onTurnFinish();
    }

    public void setOnTurnFinish(OnTurnFinish onTurnFinish) {
        this.onTurnFinish = onTurnFinish;
    }

    public void setOnGameFinish(OnGameFinish onGameFinish) {
        this.onGameFinish = onGameFinish;
    }
}
