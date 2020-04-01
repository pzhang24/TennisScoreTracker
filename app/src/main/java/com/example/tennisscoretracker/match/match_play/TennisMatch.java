package com.example.tennisscoretracker.match.match_play;

import com.example.tennisscoretracker.match.match_team.TennisTeam;

import java.util.ArrayList;
import java.util.List;

public class TennisMatch {

    private static final int FORMAT_SINGLES = 1;
    private static final int FORMAT_DOUBLES = 2;

    //May need in future ie for saving data about a match in progress
    //private int format;
    //private TennisTeam tennisTeam1;
    //private TennisTeam tennisTeam2;

    private int winningTeamNumber;
    private static final int TEAM1_NUMBER = 1;
    private static final int TEAM2_NUMBER = 2;
    private static final int BOTH_TEAMS_NUMBER = 0;

    private int bestOfSets; //May need in future ie for saving data about a match in progress
    private final int setsNeededToWin;

    private int setsWon_Team1;
    private int setsWon_Team2;


    private int setsCompleted;

    private List<String> previousSetResults;

    private TennisSet currentTennisSet;

    public TennisMatch(int bestOfSets) {
        this.setsWon_Team1 = 0;
        this.setsWon_Team2 = 0;
        this.bestOfSets = bestOfSets;
        this.setsNeededToWin = (bestOfSets / 2) + 1;
        this.setsCompleted = 0;
        this.winningTeamNumber = 0;
        this.previousSetResults = new ArrayList<>();
    }

    //TODO: implement methods to update the state of our TennisMatch
    /**
     * Adds a point for the team passed in as a parameter, if the match is not yet over.
     * Checks before and after point is added for a winner of this match, and returns the winning team number if there is one.
     * @param teamNumber Must be either 1 for team1, or 2 for team2.
     * @return Returns 1 if team1 wins this match, 2 if team2 wins this match, and 0 if no team has won this match yet.
     */
    public int addPointForTeam(int teamNumber) {
        int beforeMatchWinner = checkForWinner();
        if (beforeMatchWinner > 0) {
            return beforeMatchWinner;
        }

        int tennisSetResult = this.currentTennisSet.addPointForTeam(teamNumber);

        switch(tennisSetResult) {
            //If neither team wins a set after winning a point, nothing here should be executed
            case(TEAM1_NUMBER):
                this.setsWon_Team1++;
                this.setsCompleted++;
                this.previousSetResults.add(currentTennisSet.getSetScore(BOTH_TEAMS_NUMBER));
                this.currentTennisSet = new TennisSet();
                break;
            case(TEAM2_NUMBER):
                this.setsWon_Team2++;
                this.setsCompleted++;
                this.previousSetResults.add(currentTennisSet.getSetScore(BOTH_TEAMS_NUMBER));
                this.currentTennisSet = new TennisSet();
                break;
        }

        return checkForWinner();
    }

    private int checkForWinner() {
        if(setsWon_Team1 >= setsNeededToWin) {
            this.winningTeamNumber = TEAM1_NUMBER;
        } else if (setsWon_Team2 >= setsNeededToWin) {
            this.winningTeamNumber = TEAM2_NUMBER;
        } else {
            this.winningTeamNumber = 0;
        }

        return this.winningTeamNumber;
    }

    /**
     * Gets the result for a previous set.
     * @param setNumber the number of the set we want to retrieve. Must be less than or equal to this.getSetsCompleted().
     * @return the result as a string in the format gamesWon_team1-gamesWon_team2.
     */
    public String getPreviousSetResult(int setNumber) {
        if(setNumber > previousSetResults.size()) {
            return null;
        } else {
            return previousSetResults.get(setNumber - 1);
        }
    }


    /**
     * Returns the score for the current set for the requested team.
     * @param teamNumber Must be either 1 for team1, 2 for team2, or 0 for both teams.
     * @return the games won by the requested team in the current set.
     * If both teams' score requested, returns the result as a string in the format gamesWon_team1-gamesWon_team2.
     */
    public String getSetScore(int teamNumber) {
        return this.currentTennisSet.getSetScore(teamNumber);
    }

    /**
     * Returns the score for the current game for the requested team.
     * @param teamNumber Must be either 1 for team1, 2 for team2, or 0 for both teams.
     * @return the score of the requested team in the current game.
     * If both teams' score requested, returns the result as a string in the format score_team1:score_team2.
     */
    public String getGameScore(int teamNumber) {
        return this.currentTennisSet.getGameScore(teamNumber);
    }

    public int getSetsCompleted() {
        return setsCompleted;
    }
}
