package com.example.tennisscoretracker.match;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.tennisscoretracker.R;
import com.example.tennisscoretracker.match.match_play.TennisMatch;
import com.example.tennisscoretracker.match.match_team.TennisTeam;
import com.example.tennisscoretracker.match.score_tracker_fragments.PlayerActionFragment;
import com.example.tennisscoretracker.match.score_tracker_fragments.ScoreBoxFragment;
import com.example.tennisscoretracker.match_setup.MatchPlayerSelectActivity;
import com.example.tennisscoretracker.player_database.PlayerDBHelper;
import com.example.tennisscoretracker.player_database.PlayerDoesNotExistException;

import java.util.List;

//TODO: implement PlayerActionFragment.PlayerActionListener
public class MatchScoreTrackerActivity extends AppCompatActivity implements PlayerActionFragment.PlayerActionListener{

    private static final int TEAM_1 = 1;
    private static final int TEAM_2 = 2;
    private static final int PLAYER_1 = 1;
    private static final int PLAYER_2 = 2;

    private PlayerDBHelper playerDBHelper;

    private TennisMatch tennisMatch;

   //----------------------------//

    //PlayerActionFragment fields

    private PlayerActionFragment team1Player1_ActionFragment;
    private PlayerActionFragment team1Player2_ActionFragment;
    private PlayerActionFragment team2Player1_ActionFragment;
    private PlayerActionFragment team2Player2_ActionFragment;

    //---------------------------//

    //ScoreBoxFragment fields

    private ScoreBoxFragment playerNames_ScoreBoxFragment;
    private ScoreBoxFragment setOne_ScoreBoxFragment;
    private ScoreBoxFragment setTwo_ScoreBoxFragment;
    private ScoreBoxFragment setThree_ScoreBoxFragment;
    private ScoreBoxFragment setFour_ScoreBoxFragment;
    private ScoreBoxFragment setFive_ScoreBoxFragment;
    private ScoreBoxFragment game_ScoreBoxFragment;



    //TODO: modify corresponding layout-land file... MOSTLY DONE

    //TODO: Create an instance of match... DONE
    //TODO: Take in an intent... DONE
    //TODO: Create scorebox fragments... Need to include in activity... DONE
    //TODO: Create player action fragments... Need to include in activity... DONE
    //TODO: Create logic to change match state and update scorebox fragments when player action buttons are clicked... DONE

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
        TennisTeam tennisTeam_1;
        TennisTeam tennisTeam_2;
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

            tennisTeam_1 = new TennisTeam(team1Player1_name, team1Player1_ID, team1Player2_name, team1Player2_ID);
            tennisTeam_2 = new TennisTeam(team2Player1_name, team2Player1_ID, team2Player2_name, team2Player2_ID);
        } else {
            tennisTeam_1 = new TennisTeam(team1Player1_name, team1Player1_ID);
            tennisTeam_2 = new TennisTeam(team2Player1_name, team2Player1_ID);
        }


        //Configure match details
        int matchFormat = isDoubles ? TennisMatch.FORMAT_DOUBLES : TennisMatch.FORMAT_SINGLES;
        tennisMatch = new TennisMatch(numberOfSets, matchFormat, tennisTeam_1, tennisTeam_2);

        //----------------------------------------//
        //Configure the UI display
        TextView bestOfSets = findViewById(R.id.activity_match_score_tracker_BEST_OF_SETS_TEXTVIEW);
        bestOfSets.setText("Best Of " + numberOfSets + " Sets");

        updateCurrentSetTextView();

        configurePlayerScoreBoxFragments();

        configurePlayerActionFragments();

        /*
        Log.d(MatchScoreTrackerActivity.class.getSimpleName(),
                "This number (completed sets at beginning of match)" +
                " should be zero: " + tennisMatch.getSetsCompleted());
         */

        //Must add setOne, and game scoreBox fragments to our UI at the start of the match.
        updateScoreBoxFragments(tennisMatch.getSetsCompleted()); //Should be passing in zero.
        updateMatchScore(); //Displays the score at the beginning of the game

    }

    //--------------------------------------------------------------//
    //These methods configure/update the UI
    /**
     * Update the TextView displaying the current set number
     */
    private void updateCurrentSetTextView() {
        TextView currentSetNum = findViewById(R.id.activity_match_score_tracker_CURRENT_SET_TEXTVIEW);
        if(tennisMatch.getCurrentSetNumber() == -1) {
            currentSetNum.setText("");
        } else {
            currentSetNum.setText("Current Set: " + tennisMatch.getCurrentSetNumber());
        }
    }

    /**
     * Configure the ScoreBoxFragment associated with the player names
     * - Displays the player/team names for both teams on the scoreboard UI
     */
    private void configurePlayerScoreBoxFragments() {
        playerNames_ScoreBoxFragment = new ScoreBoxFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_match_score_tracker_PLAYER_SCOREBOX,
                playerNames_ScoreBoxFragment).commit();

        //Set the names for both teams in the ScoreBoxFragment for player names
        playerNames_ScoreBoxFragment.setText(tennisMatch.getTennisTeam1().getFullTeamName(),
                tennisMatch.getTennisTeam2().getFullTeamName());
    }

    /**
     * Updates the ScoreBoxFragments based on the number of sets completed
     * - call whenever the set number changes
     * @param completedSets the number of sets completed at this point
     */
    private void updateScoreBoxFragments(int completedSets) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //At the start of the match
        if (completedSets == 0) {
            setOne_ScoreBoxFragment = new ScoreBoxFragment();
            game_ScoreBoxFragment = new ScoreBoxFragment();

            fragmentTransaction.replace(R.id.activity_match_score_tracker_SCOREBOX_ONE,
                    setOne_ScoreBoxFragment);
            fragmentTransaction.replace(R.id.activity_match_score_tracker_SCOREBOX_TWO,
                    game_ScoreBoxFragment);
        }

        //If the match is completed, remove the game ScoreBox
        if (tennisMatch.isMatchFinished()) {
            fragmentTransaction.remove(game_ScoreBoxFragment);
        } else {
            //Otherwise update the fragment positions within the activity layout
            switch (completedSets) {
                case (1):
                    setTwo_ScoreBoxFragment = new ScoreBoxFragment();
                    fragmentTransaction.replace(R.id.activity_match_score_tracker_SCOREBOX_TWO,
                            setTwo_ScoreBoxFragment);
                    fragmentTransaction.replace(R.id.activity_match_score_tracker_SCOREBOX_THREE,
                            game_ScoreBoxFragment);
                    break;
                case (2):
                    setThree_ScoreBoxFragment = new ScoreBoxFragment();
                    fragmentTransaction.replace(R.id.activity_match_score_tracker_SCOREBOX_THREE,
                            setThree_ScoreBoxFragment);
                    fragmentTransaction.replace(R.id.activity_match_score_tracker_SCOREBOX_FOUR,
                            game_ScoreBoxFragment);
                    break;
                case (3):
                    setFour_ScoreBoxFragment = new ScoreBoxFragment();
                    fragmentTransaction.replace(R.id.activity_match_score_tracker_SCOREBOX_FOUR,
                            setFour_ScoreBoxFragment);
                    fragmentTransaction.replace(R.id.activity_match_score_tracker_SCOREBOX_FIVE,
                            game_ScoreBoxFragment);
                    break;
                case (4):
                    setFive_ScoreBoxFragment = new ScoreBoxFragment();
                    fragmentTransaction.replace(R.id.activity_match_score_tracker_SCOREBOX_FIVE,
                            setFive_ScoreBoxFragment);
                    fragmentTransaction.replace(R.id.activity_match_score_tracker_SCOREBOX_SIX,
                            game_ScoreBoxFragment);
            }
        }

        fragmentTransaction.commit();
    }

    /**
     * Configure the PlayerActionFragments - which receive input from the user
     */
    private void configurePlayerActionFragments() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        team1Player1_ActionFragment = PlayerActionFragment.newInstance(TEAM_1, PLAYER_1);
        team2Player1_ActionFragment = PlayerActionFragment.newInstance(TEAM_2, PLAYER_1);

        //Box One is Top Left, Box Two is Bottom Left
        //Box Three is Top Right, Box Four is Bottom Right

        fragmentTransaction.add(R.id.activity_match_score_tracker_PLAYER_ACTION_BOX_ONE,
                team1Player1_ActionFragment);
        fragmentTransaction.add(R.id.activity_match_score_tracker_PLAYER_ACTION_BOX_THREE,
                team2Player1_ActionFragment);


        //Also need to construct fragments for player2 on both teams if the match format is Doubles
        if(tennisMatch.isDoubles()) {
            team1Player2_ActionFragment = PlayerActionFragment.newInstance(TEAM_1, PLAYER_2);
            team2Player2_ActionFragment = PlayerActionFragment.newInstance(TEAM_2, PLAYER_2);

            fragmentTransaction.add(R.id.activity_match_score_tracker_PLAYER_ACTION_BOX_TWO,
                    team1Player2_ActionFragment);
            fragmentTransaction.add(R.id.activity_match_score_tracker_PLAYER_ACTION_BOX_FOUR,
                    team2Player2_ActionFragment);
        }

        fragmentTransaction.commit();

    }

    /**
     * Helper method -- update the scoreBox UI to reflect the current score
     */
    private void updateMatchScore() {

        //size of this list should be equal to number of completed sets
        List<String> previousSetResults = tennisMatch.getAllPreviousSetResults();

        if(tennisMatch.getSetsCompleted() >= 1) {
            String setOneResult = previousSetResults.get(0);
            String[] arrSetOneResult = parseScore(setOneResult, "-");

            //(The score at index 0 will be for team1, and the score at index 1 will be for team2)
            setOne_ScoreBoxFragment.setText(arrSetOneResult[0], arrSetOneResult[1]);
        }

        if(tennisMatch.getSetsCompleted() >= 2) {
            String setTwoResult = previousSetResults.get(1);
            String[] arrSetTwoResult = parseScore(setTwoResult, "-");

            //(The score at index 0 will be for team1, and the score at index 1 will be for team2)
            setTwo_ScoreBoxFragment.setText(arrSetTwoResult[0], arrSetTwoResult[1]);
        }

        if(tennisMatch.getSetsCompleted() >= 3) {
            String setThreeResult = previousSetResults.get(2);
            String[] arrSetThreeResult = parseScore(setThreeResult, "-");

            //(The score at index 0 will be for team1, and the score at index 1 will be for team2)
            setThree_ScoreBoxFragment.setText(arrSetThreeResult[0], arrSetThreeResult[1]);
        }

        if(tennisMatch.getSetsCompleted() >= 4) {
            String setFourResult = previousSetResults.get(3);
            String[] arrSetFourResult = parseScore(setFourResult, "-");

            //(The score at index 0 will be for team1, and the score at index 1 will be for team2)
            setFour_ScoreBoxFragment.setText(arrSetFourResult[0], arrSetFourResult[1]);
        }

        if(tennisMatch.getSetsCompleted() >= 5) {
            String setFiveResult = previousSetResults.get(4);
            String[] arrSetFiveResult = parseScore(setFiveResult, "-");

            //(The score at index 0 will be for team1, and the score at index 1 will be for team2)
            setFive_ScoreBoxFragment.setText(arrSetFiveResult[0], arrSetFiveResult[1]);
        }

        //Must also display score for current set and game if match is not yet finished
        if(!tennisMatch.isMatchFinished()) {
            String currentSetScore = tennisMatch.getSetScore(0);
            String[] arrCurrentSetScore = parseScore(currentSetScore, "-");

            //Display the current set score
            switch(tennisMatch.getCurrentSetNumber()) {
                case(1):
                    setOne_ScoreBoxFragment.setText(arrCurrentSetScore[0], arrCurrentSetScore[1]);
                    break;
                case(2):
                    setTwo_ScoreBoxFragment.setText(arrCurrentSetScore[0], arrCurrentSetScore[1]);
                    break;
                case(3):
                    setThree_ScoreBoxFragment.setText(arrCurrentSetScore[0], arrCurrentSetScore[1]);
                    break;
                case(4):
                    setFour_ScoreBoxFragment.setText(arrCurrentSetScore[0], arrCurrentSetScore[1]);
                    break;
                case(5):
                    setFive_ScoreBoxFragment.setText(arrCurrentSetScore[0], arrCurrentSetScore[1]);
                    break;
            }

            //Display the current game score
            String currentGameScore = tennisMatch.getGameScore(0);
            String[] arrCurrentGameScore = parseScore(currentGameScore, ":");
            game_ScoreBoxFragment.setText(arrCurrentGameScore[0], arrCurrentGameScore[1]);
        }
    }

    /**
     * Helper method -- parses the score (delimiter differs between game score and set score)
     * @param str the string to parse -- must only contain one instance of the delimiter
     * @param delimiter a delimiter
     * @return a String array containing the parsed score -- will be length 2.
     *
     */
    private String[] parseScore(String str, String delimiter) {
        return str.split(delimiter);
    }




    //----------------------------------------------------//
    //These methods implement the PlayerActionListener interface, executing when the user presses
    //any button in PlayerActionFragment

    //The following two methods should result in a point FOR the teamNumber passed in as a parameter

    @Override
    public void onAceButtonClicked(int teamNumber, int playerNumber) {
        addPointForTeam(teamNumber);
    }

    @Override
    public void onWinnerButtonClicked(int teamNumber, int playerNumber) {
        addPointForTeam(teamNumber);
    }

    //The following three methods should result in a point AGAINST the teamNumber passed in as a parameter
    @Override
    public void onDoubleFaultButtonClicked(int teamNumber, int playerNumber) {
        addPointAgainstTeam(teamNumber);
    }

    @Override
    public void onForcedErrorButtonClicked(int teamNumber, int playerNumber) {
        addPointAgainstTeam(teamNumber);
    }

    @Override
    public void onUnforcedErrorButtonClicked(int teamNumber, int playerNumber) {
        addPointAgainstTeam(teamNumber);
    }

    //---------------------------------------------//
    //Methods that change the state of tennisMatch
    /**
     * Helper method -- calls addPointForTeam for the opposing team
     * @param teamNumber either 1 or 2. Calls addPointForTeam for team2 if 1 is passed in, and vice versa.
     */
    private void addPointAgainstTeam(int teamNumber) {
        if(teamNumber == TEAM_1) {addPointForTeam(TEAM_2);}
        else if (teamNumber == TEAM_2) {addPointForTeam(TEAM_1);}
    }

    /**
     * Helper method -- changes the state of our TennisMatch object,
     * and adjusts the UI to display correct score
     * @param teamNumber either 1 to add a point for team1, or 2 to add a point for team2.
     */
    private void addPointForTeam(int teamNumber) {
        int completedSetsBefore = tennisMatch.getSetsCompleted();
        int completedSetsAfter;

        //This will update the state of TennisMatch
        int winningTeam = tennisMatch.addPointForTeam(teamNumber);

        completedSetsAfter = tennisMatch.getSetsCompleted();

        //Adjust the fragment positions in our scoreBox UI if necessary - ie. if we've completed a set.
        if(completedSetsBefore != completedSetsAfter) {
            updateScoreBoxFragments(completedSetsAfter);
            updateCurrentSetTextView();
        }

        //Update the text in our scoreBox UI to reflect the current score.
        updateMatchScore();

        //If there is a winningTeam
        if(winningTeam > 0) {
            //TODO: Some code for when the match is finished
        }
    }



    //-------------------------//
    //Methods involving PlayerDBHelper in this activity
    /**
     * Initialize our playerDBHelper
     */
    private void configurePlayerDBHelper() {
        playerDBHelper = PlayerDBHelper.getInstance(this);
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

    //-----------------------------------------------//


}
