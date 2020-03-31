package com.example.tennisscoretracker.match.match_play;



public class TennisGame {

    private boolean isTieBreak;
    private final int pointsNeededToWin;

    private int pointsWon_Team1;
    private int pointsWon_Team2;

    private int winningTeamNumber;

    private static final int TEAM1_NUMBER = 1;
    private static final int TEAM2_NUMBER = 2;

    private static final int POINTS_FOR_WIN_NORMAL = 4;
    private static final int POINTS_FOR_WIN_TIEBREAK = 7;

    public TennisGame(boolean isTieBreak) {
        this.isTieBreak = isTieBreak;
        this.pointsNeededToWin = isTieBreak ? POINTS_FOR_WIN_TIEBREAK : POINTS_FOR_WIN_NORMAL;
        this.pointsWon_Team1 = 0;
        this.pointsWon_Team2 = 0;
        this.winningTeamNumber = 0;
    }

    /**
     * Adds a point for team1 if the game is not yet over (ie. no team has won yet)
     * @return true if a point was successfully added
     * (if false is returned, that indicates that a team has already won the game)
     */
    public boolean addPointTeam1() {
        if(checkForWinner() == 0) {
            pointsWon_Team1++;
            return true;
        }
        return false;
    }

    /**
     * Adds a point for team2 if the game is not yet over (ie. no team has won yet)
     * @return true if a point was successfully added
     * (if false is returned, that indicates that a team has already won the game)
     */
    public boolean addPointTeam2() {
        if(checkForWinner() == 0) {
            pointsWon_Team2++;
            return true;
        }
        return false;
    }

    /**
     * Check for a winner, and updates this.winningTeamNumber if a team has won.
     * @return Returns 1 if team1 has won, 2 if team2 has won, 0 if neither team has won.
     */
    public int checkForWinner() {
        if (pointsWon_Team1 >= pointsNeededToWin && upByTwo(pointsWon_Team1, pointsWon_Team2)) {
            this.winningTeamNumber = TEAM1_NUMBER;
            return TEAM1_NUMBER;
        } else if (pointsWon_Team2 >= pointsNeededToWin && upByTwo(pointsWon_Team2, pointsWon_Team1)) {
            this.winningTeamNumber = TEAM2_NUMBER;
            return TEAM2_NUMBER;
        } else {
            return 0;
        }
    }

    /**
     * Returns whether this game is a tieBreak game or not.
     * @return true if this game is a tieBreak game, and false if this game is not (ie. a normal game)
     */
    public boolean isTieBreak() {
        return isTieBreak;
    }

    /**
     * Helper Function:
     * Check that a team is leading by two points or more
     * @param scoreToCheck the number of points won by the team to check
     * @param otherScore the number of poitns won by the opposing team
     * @return true if the team we're checking is leading by two, and false otherwise.
     */
    private boolean upByTwo(int scoreToCheck, int otherScore) {
        return (scoreToCheck - otherScore) >= 2;
    }
}
