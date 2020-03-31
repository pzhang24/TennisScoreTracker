package com.example.tennisscoretracker.match.match_play;

import com.example.tennisscoretracker.match.match_team.TennisTeam;

public class TennisSet {

    private static final int gamesNeededToWin = 6;

    private int winningTeamNumber;
    private static final int TEAM1_NUMBER = 1;
    private static final int TEAM2_NUMBER = 2;

    private int gamesWon_Team1;
    private int gamesWon_Team2;

    private int currentGameNumber;

    private TennisGame currentTennisGame;

    public TennisSet() {
        this.gamesWon_Team1 = 0;
        this.gamesWon_Team2 = 0;
        this.currentGameNumber = 0;

        //Create first game
        this.currentTennisGame = new TennisGame(false);
    }

    //TODO: implement methods to update the state of our TennisSet
    //TODO: implement addPointTeam1(), addPointTeam2(), checkForWinner
    public int addPointTeam1() {
        //TODO: check that we've won set (before and after)
        //TODO: call TennisGame.addPointTeam1() -> if we have a winner, add to their gamesWon tally
        //TODO: also increment the currentGameNumber, and replace currentTennisGame with a new game (note tiebreaker games!)
        return 0;
    }
}
