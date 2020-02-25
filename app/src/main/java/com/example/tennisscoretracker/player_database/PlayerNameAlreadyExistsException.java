package com.example.tennisscoretracker.player_database;

/**
 * Exception Class for when a player name already exists in the database
 */
public class PlayerNameAlreadyExistsException extends Exception {

    /**
     * Constructor
     * @param message String to display as a message
     */
    public PlayerNameAlreadyExistsException(String message) {
        super(message);
    }
}
