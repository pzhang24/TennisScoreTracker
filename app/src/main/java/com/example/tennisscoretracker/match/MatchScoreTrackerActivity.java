package com.example.tennisscoretracker.match;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.util.Log;

import com.example.tennisscoretracker.R;
import com.example.tennisscoretracker.match.match_play.TennisMatch;
import com.example.tennisscoretracker.match.match_team.TennisTeam;
import com.example.tennisscoretracker.match_setup.MatchPlayerSelectActivity;
import com.example.tennisscoretracker.player_database.PlayerDBHelper;
import com.example.tennisscoretracker.player_database.PlayerDoesNotExistException;

import java.util.HashMap;

//TODO: implement PlayerActionFragment.PlayerActionListener
public class MatchScoreTrackerActivity extends AppCompatActivity {

    private PlayerDBHelper playerDBHelper;

    private TennisTeam team_1;
    private TennisTeam team_2;

    private TennisMatch tennisMatch;


    //TODO: modify corresponding layout-land file... MOSTLY DONE

    //TODO: Create an instance of match... DONE
    //TODO: Take in an intent... DONE
    //TODO: Create scorebox fragments... Need to include in activity
    //TODO: Create player action fragments... Need to include in activity
    //TODO: Create logic to change match state and update scorebox fragments when player action buttons are clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Will use solely landscape for the time being
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_match_score_tracker);

        //Get an instance of the Player Database
        configurePlayerDBHelper();

        //Get the match parameters (extras passed in from calling activity), and set up teams
        Intent intent = getIntent();

        boolean isDoubles = intent.getBooleanExtra(MatchPlayerSelectActivity.IS_DOUBLES_EXTRA, false);
        int numberOfSets = intent.getIntExtra(MatchPlayerSelectActivity.NUM_SETS_EXTRA, 0);

        String team1Player1_name = intent.getStringExtra(MatchPlayerSelectActivity.TEAM_1_PLAYER_1_EXTRA);
        String team2Player1_name = intent.getStringExtra(MatchPlayerSelectActivity.TEAM_2_PLAYER_1_EXTRA);
        int team1Player1_ID = getPlayerIDFromName(playerDBHelper, team1Player1_name);
        int team2Player1_ID = getPlayerIDFromName(playerDBHelper, team2Player1_name);

        //Debugging
        /*
        Log.d(MatchScoreTrackerActivity.class.getSimpleName(), "Team1Player1_ID is " + team1Player1_ID);
        Log.d(MatchScoreTrackerActivity.class.getSimpleName(), "Team2Player1_ID is " + team2Player1_ID);
        */

        String team1Player2_name;
        String team2Player2_name;
        int team1Player2_ID;
        int team2Player2_ID;

        //Create new TennisTeam instances
        if(isDoubles) {
            team1Player2_name = intent.getStringExtra(MatchPlayerSelectActivity.TEAM_1_PLAYER_2_EXTRA);
            team2Player2_name = intent.getStringExtra(MatchPlayerSelectActivity.TEAM_2_PLAYER_2_EXTRA);
            team1Player2_ID = getPlayerIDFromName(playerDBHelper, team1Player2_name);
            team2Player2_ID = getPlayerIDFromName(playerDBHelper, team2Player2_name);

            //Debugging
            /*
            Log.d(MatchScoreTrackerActivity.class.getSimpleName(), "Team1Player2_ID is " + team1Player2_ID);
            Log.d(MatchScoreTrackerActivity.class.getSimpleName(), "Team2Player2_ID is " + team2Player2_ID);
            */

            team_1 = new TennisTeam(team1Player1_name, team1Player1_ID, team1Player2_name, team1Player2_ID);
            team_2 = new TennisTeam(team2Player1_name, team2Player1_ID, team2Player2_name, team2Player2_ID);
        } else {
            team_1 = new TennisTeam(team1Player1_name, team1Player1_ID);
            team_2 = new TennisTeam(team2Player1_name, team2Player1_ID);
        }


        //Configure match details
        int matchFormat = isDoubles ? TennisMatch.FORMAT_DOUBLES : TennisMatch.FORMAT_SINGLES;
        tennisMatch = new TennisMatch(numberOfSets, matchFormat, team_1, team_2);

        //----------------------------------------//
        //TODO: Set up ScoreBox and PlayerActionFragments, methods to change state of match


    }



    /**
     * Tries to get a playerID given a playerName using the PlayerDBHelper
     * @param helper PlayerDBHelper
     * @param playerName name of player to search for ID
     * @return the playerID, or -1 if not found.
     */
    private int getPlayerIDFromName(PlayerDBHelper helper, String playerName) {
        int playerID = -1;

        try {
            playerID = helper.getIDFromName(playerName);
        } catch (PlayerDoesNotExistException e) {
            Log.d(MatchScoreTrackerActivity.class.getSimpleName(), "Could not get an ID for " + playerName);
        }

        return playerID;
    }


    /**
     * Initialize our playerDBHelper
     */
    private void configurePlayerDBHelper() {
        playerDBHelper = PlayerDBHelper.getInstance(this);
    }
}
