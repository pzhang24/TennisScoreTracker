package com.example.tennisscoretracker.player_database;

/**
 * Exception Class for when we have exceeded the maximum number of players
 * in the database we can handle.
 */
public class TooManyPlayersException extends Exception {
    /**
     * Constructor
     * @param message String to display as a message
     */
    public TooManyPlayersException(String message) {
        super(message);
    }
}
