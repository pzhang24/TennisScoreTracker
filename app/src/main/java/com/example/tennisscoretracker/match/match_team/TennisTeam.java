package com.example.tennisscoretracker.match.match_team;


public class TennisTeam {

    private final int format;
    private static final int FORMAT_SINGLES = 1;
    private static final int FORMAT_DOUBLES = 2;

    private TennisPlayer tennisPlayer1;
    private TennisPlayer tennisPlayer2;

    /**
     * Creates a new doubles team.
     *
     * @param player1Name name of tennisPlayer1
     * @param player1ID   ID of tennisPlayer1
     * @param player2Name name of tennisPlayer2
     * @param player2ID   ID of tennisPlayer2
     */
    public TennisTeam(String player1Name, int player1ID, String player2Name, int player2ID) {
        format = FORMAT_DOUBLES;

        this.tennisPlayer1 = new TennisPlayer(player1Name, player1ID);
        this.tennisPlayer2 = new TennisPlayer(player2Name, player2ID);
    }

    /**
     * Creates a new singles team - ie. a team containing a single player.
     *
     * @param playerName name of player
     * @param playerID   ID of player
     */
    public TennisTeam(String playerName, int playerID) {
        format = FORMAT_SINGLES;

        this.tennisPlayer1 = new TennisPlayer(playerName, playerID);
    }

    /**
     * Gets the name of tennisPlayer1 on this team.
     *
     * @return a String representing tennisPlayer1's name.
     */
    public String getPlayer1Name() {
        return tennisPlayer1.getPlayerName();
    }

    /**
     * Gets the ID of tennisPlayer1 on this team.
     *
     * @return an int representing tennisPlayer1's ID.
     */
    public int getPlayer1ID() {
        return tennisPlayer1.getPlayerId();
    }

    /**
     * Gets the name of tennisPlayer2 on this team, if tennisPlayer2 exists.
     *
     * @return a String representing tennisPlayer2's name.
     * If tennisPlayer2 does not exist (this.isDoublesTeam() returns false), return null.
     */
    public String getPlayer2Name() {
        return (format == FORMAT_DOUBLES) ? tennisPlayer2.getPlayerName() : null;
    }

    /**
     * Gets the ID of tennisPlayer2 on this team, if tennisPlayer2 exists.
     *
     * @return an int representing tennisPlayer2's ID.
     * If tennisPlayer2 does not exist (ie this.isDoublesTeam() returns false), return -1.
     */
    public int getPlayer2ID() {
        return (format == FORMAT_DOUBLES) ? tennisPlayer2.getPlayerId() : -1;
    }

    /**
     * Gets the combined name of both players if this team is a doubles team,
     * or just tennisPlayer1's name if this team is a singles team.
     *
     * @return either both player's names in the format "player1Name/player2Name" if team is a doubles team,
     * or only the name of tennisPlayer1 if this team is a singles team.
     */
    public String getFullTeamName() {
        return (format == FORMAT_DOUBLES) ?
                (tennisPlayer1.getPlayerName() + "/" + tennisPlayer2.getPlayerName())
                : tennisPlayer1.getPlayerName();
    }


    /**
     * Returns whether this team is a doubles or singles team
     *
     * @return true if doubles, false if singles.
     */
    public boolean isDoublesTeam() {
        return format == FORMAT_DOUBLES;
    }
}
