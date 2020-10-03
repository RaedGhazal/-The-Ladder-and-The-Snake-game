package com.raedghazal.thegametask.models;

import java.util.Date;

public class Game {
    private int gameId=-1;
    private int playerId=-1;
    private int player1Location = 1;
    private int player2Location = 1;
    private int totalDiceRolls;
    private Date Date;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayer1Location() {
        return player1Location;
    }

    public void setPlayer1Location(int player1Location) {
        this.player1Location = player1Location;
    }

    public int getPlayer2Location() {
        return player2Location;
    }

    public void setPlayer2Location(int player2Location) {
        this.player2Location = player2Location;
    }

    public int getTotalDiceRolls() {
        return totalDiceRolls;
    }

    public void setTotalDiceRolls(int totalDiceRolls) {
        this.totalDiceRolls = totalDiceRolls;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public void addRoll() {
        totalDiceRolls++;
    }
}
