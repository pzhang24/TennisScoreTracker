package com.example.tennisscoretracker.match.match_team;

public class TennisPlayer {

    private String playerName;
    private int id;

    public TennisPlayer(String playerName, int id) {
        this.playerName = playerName;
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerId() {
        return id;
    }
}
