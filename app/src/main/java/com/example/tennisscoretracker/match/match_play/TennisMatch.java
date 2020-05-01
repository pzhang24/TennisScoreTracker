package com.example.tennisscoretracker.match.match_play;

import com.example.tennisscoretracker.match.match_team.TennisTeam;

import java.util.ArrayList;
import java.util.List;

public class TennisMatch {



    public static final int FORMAT_SINGLES = 1;
    public static final int FORMAT_DOUBLES = 2;

    //May need in future ie for saving data about a match in progress
    private int format;
    private TennisTeam tennisTeam1;
    private TennisTeam tennisTeam2;

    private static final int MATCH_IS_FINISHED = -1;
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

    /**
     * Constructor
     * @param bestOfSets either 1, 3, or 5
     * @param format either this.FORMAT_SINGLES for Singles, or this.FORMAT_DOUBLES for Doubles
     * @param team1 a tennis team
     * @param team2 the other tennis team
     */
    public TennisMatch(int bestOfSets, int format, TennisTeam team1, TennisTeam team2) {
        this.format = format;
        this.tennisTeam1 = team1;
        this.tennisTeam2 = team2;
        this.setsWon_Team1 = 0;
        this.setsWon_Team2 = 0;
        this.bestOfSets = bestOfSets;
        this.setsNeededToWin = (bestOfSets / 2) + 1;
        this.setsCompleted = 0;
        this.winningTeamNumber = 0;
        this.previousSetResults = new ArrayList<>();

        this.currentTennisSet = new TennisSet();
    }

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
     * Gets the results for all previous sets.
     * @return a new List of Strings representing the score for all previous sets.
     */
    public List<String> getAllPreviousSetResults() {
        return new ArrayList<>(previousSetResults);
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

    /**
     * Returns the number of sets already completed.
     * @return number of sets already completed.
     */
    public int getSetsCompleted() {
        return setsCompleted;
    }

    /**
     * Returns the current set number if the match is not yet over.
     * @return the current set number, or -1 if the match is over.
     */
    public int getCurrentSetNumber() {

        if(this.winningTeamNumber != 0) {
            return MATCH_IS_FINISHED;
        } else {
            return setsCompleted + 1;
        }
    }

    /**
     * Returns the maximum number of sets to be played, ie. the best of ___ sets
     * @return the max number of sets to be played
     */
    public int getMaxNumberOfSets() {
        return bestOfSets;
    }

    /**
     * Returns whether this match is a doubles or singles match
     * @return true if it is a doubles match, and false if it is a singles match
     */
    public boolean isDoubles() {
        return format == FORMAT_DOUBLES;
    }

    /**
     * Returns a new object representing team1
     * @return a new TennisTeam object representing team1
     */
    public TennisTeam getTennisTeam1() {
        TennisTeam teamCopy;
        if(tennisTeam1.isDoublesTeam()) {
            teamCopy = new TennisTeam(tennisTeam1.getPlayer1Name(), tennisTeam1.getPlayer1ID(),
                    tennisTeam1.getPlayer2Name(), tennisTeam1.getPlayer2ID());
        } else {
            teamCopy = new TennisTeam(tennisTeam1.getPlayer1Name(), tennisTeam1.getPlayer1ID());
        }

        return teamCopy;
    }

    /**
     * Returns a new object representing team2
     * @return a new TennisTeam object representing team2
     */
    public TennisTeam getTennisTeam2() {
        TennisTeam teamCopy;
        if(tennisTeam2.isDoublesTeam()) {
            teamCopy = new TennisTeam(tennisTeam2.getPlayer1Name(), tennisTeam2.getPlayer1ID(),
                    tennisTeam2.getPlayer2Name(), tennisTeam2.getPlayer2ID());
        } else {
            teamCopy = new TennisTeam(tennisTeam2.getPlayer1Name(), tennisTeam2.getPlayer1ID());
        }

        return teamCopy;
    }

    /**
     * Can be used to check if match is finished or not.
     * @return true if match is finished, and false if match is not finished yet.
     */
    public boolean isMatchFinished() {
        return (this.winningTeamNumber != 0);
    }
}
