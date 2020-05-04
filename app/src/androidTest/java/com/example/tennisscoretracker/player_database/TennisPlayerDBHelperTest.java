package com.example.tennisscoretracker.player_database;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

//Do not use!
public class TennisPlayerDBHelperTest {

    private PlayerDBHelper playerDBHelper;
    private ArrayList<String> playerNameList;

    @Before
    public void setUp() throws Exception {
        playerDBHelper = PlayerDBHelper.getInstance(ApplicationProvider.getApplicationContext());

        playerNameList = new ArrayList<>();
        for (int i = 0; i < playerDBHelper.getDatabaseCapacity(); i++) {
            playerNameList.add("TennisPlayer" + i);
        }
    }

    @After
    public void tearDown() throws Exception {
        playerDBHelper.deleteAllPlayers();
    }

    //Must include this under run -> Configuration
    //This test cannot be found right now
    @Test
    public void insertAndRemovePlayers() {
        HashMap<String, Integer> playerNameToIDMap;

        for (String name : playerNameList) {
            try {
                playerDBHelper.insertNewPlayer(name);
            } catch (TooManyPlayersException e) {
                fail("Incorrectly threw TooManyPlayersException!");
            } catch (PlayerNameAlreadyExistsException e) {
                fail("Incorrectly threw PlayerNameAlreadyExistsException!");
            }
        }

        try {
            playerDBHelper.insertNewPlayer("ExtraPlayer");
            fail("Expected TooManyPlayersException when inserting extra player");
        } catch (TooManyPlayersException e) {
            //TestPasses
        } catch (PlayerNameAlreadyExistsException e) {
            fail("Incorrectly threw PlayerNameAlreadyExistsException when inserting extra player!");
        }

        try {
            playerDBHelper.insertNewPlayer(playerNameList.get(playerNameList.size()-1));
        } catch (TooManyPlayersException e) {
            fail("Incorrectly threw TooManyPlayersException when inserting already existing player");
        } catch (PlayerNameAlreadyExistsException e) {
            //TestPasses;
        }

        playerNameToIDMap = playerDBHelper.getPlayerNameToIDMapping();

        for(String name: playerNameList) {
            assertTrue(name + " was not deleted!", playerDBHelper.deletePlayer(playerNameToIDMap.get(name)) );
        }


    }

}