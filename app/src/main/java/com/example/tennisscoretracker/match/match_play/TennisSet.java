package com.example.tennisscoretracker.match.match_play;

public class TennisSet {

    private static final int gamesNeededToWin = 6;

    private int winningTeamNumber;
    private static final int TEAM1_NUMBER = 1;
    private static final int TEAM2_NUMBER = 2;

    private int gamesWon_Team1;
    private int gamesWon_Team2;

    private static final int MAX_REGULAR_GAME_NUMBER = 12;
    private static final int TIEBREAK_GAME_NUMBER = 13;
    private int gamesCompleted;

    private TennisGame currentTennisGame;

    public TennisSet() {
        this.gamesWon_Team1 = 0;
        this.gamesWon_Team2 = 0;
        this.gamesCompleted = 0;
        this.winningTeamNumber = 0;

        //Create first game
        this.currentTennisGame = new TennisGame(false);
    }

    //TODO: implement methods to update the state of our TennisSet
    //TODO: implement addPointForTeam(), checkForWinner
    /**
     * Adds a point for the team passed in as a parameter, if the set is not yet over.
     * Checks before and after point is added for a winner of this set, and returns the winning team number if there is one.
     * @param teamNumber Must be either 1 for team1, or 2 for team2.
     * @return Returns 1 if team1 wins this set, 2 if team2 wins this set, and 0 if no team has won this set yet.
     */
    public int addPointForTeam(int teamNumber) {

        //check that we've won set (before and after)
        //call TennisGame.addPointForTeam() -> if we have a winner, add to their gamesWon tally
        //also increment the gamesCompleted, and replace currentTennisGame with a new game (note tiebreaker games!)

        int beforeSetWinner = checkForWinner();
        if (beforeSetWinner > 0) {
            return beforeSetWinner;
        }

        int tennisGameResult = this.currentTennisGame.addPointForTeam(teamNumber);

        switch(tennisGameResult) {
            //If neither team wins a game after winning a point, nothing here should be executed
            case(TEAM1_NUMBER):
                this.gamesWon_Team1++;
                this.gamesCompleted++;
                this.currentTennisGame = (this.gamesCompleted == MAX_REGULAR_GAME_NUMBER) ?
                        new TennisGame(true) : new TennisGame(false);
                break;
            case(TEAM2_NUMBER):
                this.gamesWon_Team2++;
                this.gamesCompleted++;
                this.currentTennisGame = (this.gamesCompleted == MAX_REGULAR_GAME_NUMBER) ?
                        new TennisGame(true) : new TennisGame(false);
                break;
        }



        return checkForWinner();
    }

    /**
     * Returns the current score in the set for the requested team as a String
     * @param teamNumber Either 1 for team1 or 2 for team2. Pass in 0 for both teams score.
     * @return the current score.
     * If both team's score is requested, a String concatenating their scores (with a hyphen "-" in between) is returned.
     */
    public String getSetScore(int teamNumber) {
        switch(teamNumber) {
            case(1):
                return Integer.toString(gamesWon_Team1);
            case(2):
                return Integer.toString(gamesWon_Team2);
            default:
                return gamesWon_Team1 + "-" + gamesWon_Team2;
        }
    }

    /**
     * Returns the current score in the current game for the requested team as a String
     * @param teamNumber Either 1 for team1 or 2 for team2. Pass in 0 for both teams score.
     * @return the current score.
     * If both team's score is requested, a String concatenating their scores (with a colon ":" in between) is returned.
     */
    public String getGameScore(int teamNumber) {
        return this.currentTennisGame.getGameScore(teamNumber);
    }

    /**
     * Check for a winner, and updates this.winningTeamNumber if a team has won.
     * @return Returns 1 if team1 has won, 2 if team2 has won, 0 if neither team has won.
     */
    private int checkForWinner() {
        if(gamesWon_Team1 >= gamesNeededToWin && upByTwo(gamesWon_Team1, gamesWon_Team2)) {
            this.winningTeamNumber = TEAM1_NUMBER;
        } else if (gamesWon_Team2 >= gamesNeededToWin && upByTwo(gamesWon_Team2, gamesWon_Team1)) {
            this.winningTeamNumber = TEAM2_NUMBER;
        } else if (gamesCompleted == TIEBREAK_GAME_NUMBER) {
            this.winningTeamNumber = (gamesWon_Team1 > gamesWon_Team2) ? TEAM1_NUMBER : TEAM2_NUMBER;
        } else {
            this.winningTeamNumber = 0;
        }
        return this.winningTeamNumber;
    }

    /**
     * Helper Function:
     * Check that a team is leading by two games or more
     * @param scoreToCheck the number of games won by the team to check
     * @param otherScore the number of games won by the opposing team
     * @return true if the team we're checking is leading by two, and false otherwise.
     */
    private boolean upByTwo(int scoreToCheck, int otherScore) {
        return (scoreToCheck - otherScore) >= 2;
    }
}
