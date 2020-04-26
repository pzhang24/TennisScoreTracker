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
     * Adds a point for the team passed in as a parameter, if the game is not yet over.
     * Checks before and after point is added for a winner, and returns the winning team number if there is one.
     * @param teamNumber Must be either 1 for team1, or 2 for team2.
     * @return Returns 1 if team1 wins, 2 if team2 wins, and 0 if no team has won yet.
     */
    public int addPointForTeam(int teamNumber) {
        int beforeWinner = checkForWinner();
        if (beforeWinner > 0) {
            return beforeWinner;
        }

        switch(teamNumber) {
            case(TEAM1_NUMBER):
                this.pointsWon_Team1++;
                break;
            case(TEAM2_NUMBER):
                this.pointsWon_Team2++;
                break;
        }

        return checkForWinner();
    }

    /**
     * Check for a winner, and updates this.winningTeamNumber if a team has won.
     * @return Returns 1 if team1 has won, 2 if team2 has won, 0 if neither team has won.
     */
    private int checkForWinner() {
        if (pointsWon_Team1 >= pointsNeededToWin && upByTwo(pointsWon_Team1, pointsWon_Team2)) {
            this.winningTeamNumber = TEAM1_NUMBER;
        } else if (pointsWon_Team2 >= pointsNeededToWin && upByTwo(pointsWon_Team2, pointsWon_Team1)) {
            this.winningTeamNumber = TEAM2_NUMBER;
        } else {
            this.winningTeamNumber = 0;
        }

        return this.winningTeamNumber;
    }

    /**
     * Returns whether this game is a tieBreak game or not.
     * @return true if this game is a tieBreak game, and false if this game is not (ie. a normal game)
     */
    public boolean isTieBreak() {
        return isTieBreak;
    }

    /**
     * Returns the current score in the game for the requested team as a String
     * @param teamNumber Either 1 for team1 or 2 for team2. Pass in 0 for both teams score.
     * @return the current score.
     * If both team's score is requested, a String concatenating their scores (with a colon ":" in between) is returned.
     */
    public String getGameScore(int teamNumber) {

        String scoreTeam1;
        String scoreTeam2;

        //Special case for tiebreak game
        if(isTieBreak) {
            scoreTeam1 = Integer.toString(pointsWon_Team1);
            scoreTeam2 = Integer.toString(pointsWon_Team2);

            switch(teamNumber) {
                case(0):
                    return scoreTeam1 + ":" + scoreTeam2;
                case(1):
                    return scoreTeam1;
                case(2):
                    return scoreTeam2;
                default:
                    return null;
            }
        }


        switch(pointsWon_Team1) {
            case(0):
                scoreTeam1 = "0";
                break;
            case(1):
                scoreTeam1 = "15";
                break;
            case(2):
                scoreTeam1 = "30";
                break;
            case(3):
                scoreTeam1 = "40";
                break;
            default:
                if (pointsWon_Team1 > pointsWon_Team2) {
                    scoreTeam1 = "AD";
                } else if (pointsWon_Team1 < pointsWon_Team2) {
                    scoreTeam1 = "-";
                } else {
                    scoreTeam1 = "40";
                }
        }

        switch(pointsWon_Team2) {
            case(0):
                scoreTeam2 = "0";
                break;
            case(1):
                scoreTeam2 = "15";
                break;
            case(2):
                scoreTeam2 = "30";
                break;
            case(3):
                scoreTeam2 = "40";
                break;
            default:
                if (pointsWon_Team2 > pointsWon_Team1) {
                    scoreTeam2 = "AD";
                } else if (pointsWon_Team2 < pointsWon_Team1) {
                    scoreTeam2 = "-";
                } else {
                    scoreTeam2 = "40";
                }
        }

        switch(teamNumber) {
            case(0):
                return scoreTeam1 + ":" + scoreTeam2;
            case(1):
                return scoreTeam1;
            case(2):
                return scoreTeam2;
            default:
                return null;
        }
    }

    public int getWinningTeamNumber() {
        return winningTeamNumber;
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
