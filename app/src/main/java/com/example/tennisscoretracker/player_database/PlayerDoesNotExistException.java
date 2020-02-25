package com.example.tennisscoretracker.player_database;

/**
 * Exception Class for when a player does not exist in our database
 */
public class PlayerDoesNotExistException extends Exception {

    /**
     * Constructor
     * @param message String to display as a message
     */
    public PlayerDoesNotExistException(String message) {
        super(message);
    }
}
