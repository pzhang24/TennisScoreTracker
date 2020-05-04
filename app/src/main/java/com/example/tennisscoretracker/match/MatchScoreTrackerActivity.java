package com.example.tennisscoretracker.match;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.tennisscoretracker.MainActivity;
import com.example.tennisscoretracker.R;
import com.example.tennisscoretracker.match.match_play.TennisMatch;
import com.example.tennisscoretracker.match.match_team.TennisTeam;
import com.example.tennisscoretracker.match.score_tracker_fragments.GameScoreBoxFragment;
import com.example.tennisscoretracker.match.score_tracker_fragments.PlayerActionFragment;
import com.example.tennisscoretracker.match.score_tracker_fragments.PlayerNameScoreBoxFragment;
import com.example.tennisscoretracker.match.score_tracker_fragments.SetScoreBoxFragment;
import com.example.tennisscoretracker.match_setup.MatchPlayerSelectActivity;
import com.example.tennisscoretracker.player_database.PlayerDBHelper;
import com.example.tennisscoretracker.player_database.PlayerDoesNotExistException;

import java.util.List;

//TODO: implement PlayerActionFragment.PlayerActionListener
public class MatchScoreTrackerActivity extends AppCompatActivity implements PlayerActionFragment.PlayerActionListener {

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

    //SetScoreBoxFragment fields

    private static final String PLAYER_NAMES_FRAGMENT_TAG = "player_names_fragment_tag";
    private static final String SET_ONE_FRAGMENT_TAG = "set_one_fragment_tag";
    private static final String SET_TWO_FRAGMENT_TAG = "set_two_fragment_tag";
    private static final String SET_THREE_FRAGMENT_TAG = "set_three_fragment_tag";
    private static final String SET_FOUR_FRAGMENT_TAG = "set_four_fragment_tag";
    private static final String SET_FIVE_FRAGMENT_TAG = "set_five_fragment_tag";
    private static final String CURRENT_SET_FRAGMENT_TAG = "current_set_fragment_tag";
    private static final String CURRENT_GAME_FRAGMENT_TAG = "game_fragment_tag";


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
        if (isDoubles) {
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

        /*
        Log.d(MatchScoreTrackerActivity.class.getSimpleName(),
                "This number (completed sets at beginning of match)" +
                " should be zero: " + tennisMatch.getSetsCompleted());
         */

        //----------------------------------------//
        //Configure the UI display
        configureBestOfSetsTextView();

        updateCurrentSetTextView();

        configurePlayerScoreBoxFragments();

        updateMatchScore(); //Displays the score at the beginning of the game

        //UI can now receive inputs, and the state of the match can now be changed
        configurePlayerActionFragments();


    }

    @Override
    public void onBackPressed() {
        displayUnsavedQuitDialog();
    }

    //--------------------------------------------------------------//
    //These methods configure/update the UI

    /**
     * Configures the BestOfSetsTextView based on whether the match is best of 1, 3, or 5 sets.
     */
    private void configureBestOfSetsTextView() {
        TextView bestOfSets = findViewById(R.id.activity_match_score_tracker_BEST_OF_SETS_TEXTVIEW);
        int maxNumberOfSets = tennisMatch.getMaxNumberOfSets();

        switch(maxNumberOfSets) {
            case(1):
                bestOfSets.setText(getResources().getString(
                        R.string.MatchScoreTrackerActivity_BEST_OF_1_SET_TEXTVIEW));
                break;
            case(3):
                bestOfSets.setText(getResources().getString(
                        R.string.MatchScoreTrackerActivity_BEST_OF_3_SETS_TEXTVIEW));
                break;
            case(5):
                bestOfSets.setText(getResources().getString(
                        R.string.MatchScoreTrackerActivity_BEST_OF_5_SETS_TEXTVIEW));
                break;
            default:
                bestOfSets.setText(getResources().getString(
                        R.string.MatchScoreTrackerActivity_ERROR_BEST_OF_SETS_TEXTVIEW));
                Log.d(MatchScoreTrackerActivity.class.getSimpleName(),
                        "Error in BestOfSetsTextView: maxNumberOfSets = " + maxNumberOfSets);
        }
    }

    /**
     * Update the TextView displaying the current set number
     */
    private void updateCurrentSetTextView() {
        TextView currentSetNum = findViewById(R.id.activity_match_score_tracker_CURRENT_SET_TEXTVIEW);
        if (tennisMatch.getCurrentSetNumber() == -1) {
            currentSetNum.setText(getResources().getString(R.string.MatchScoreTrackerActivity_MatchCompleted));
        } else {
            currentSetNum.setText("Current Set: " + tennisMatch.getCurrentSetNumber());
        }
    }

    /**
     * Configure the SetScoreBoxFragment associated with the player names
     * - Displays the player/team names for both teams on the scoreboard UI
     */
    private void configurePlayerScoreBoxFragments() {
        PlayerNameScoreBoxFragment playerNames_ScoreBoxFragment = PlayerNameScoreBoxFragment.newInstance(tennisMatch.getTennisTeam1().getFullTeamName(),
                tennisMatch.getTennisTeam2().getFullTeamName());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_match_score_tracker_PLAYER_SCOREBOX,
                playerNames_ScoreBoxFragment, PLAYER_NAMES_FRAGMENT_TAG).commitNow();

    }

    /**
     * Configure the PlayerActionFragments - which receive input from the user
     */
    private void configurePlayerActionFragments() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        team1Player1_ActionFragment =
                PlayerActionFragment.newInstance(TEAM_1, PLAYER_1,
                        tennisMatch.getTennisTeam1().getPlayer1Name());
        team2Player1_ActionFragment =
                PlayerActionFragment.newInstance(TEAM_2, PLAYER_1,
                        tennisMatch.getTennisTeam2().getPlayer1Name());

        //Box One is Top Left, Box Two is Bottom Left
        //Box Three is Top Right, Box Four is Bottom Right

        ft.add(R.id.activity_match_score_tracker_PLAYER_ACTION_BOX_ONE,
                team1Player1_ActionFragment);
        ft.add(R.id.activity_match_score_tracker_PLAYER_ACTION_BOX_THREE,
                team2Player1_ActionFragment);


        //Also need to construct fragments for player2 on both teams if the match format is Doubles
        if (tennisMatch.isDoubles()) {
            team1Player2_ActionFragment =
                    PlayerActionFragment.newInstance(TEAM_1, PLAYER_2,
                            tennisMatch.getTennisTeam1().getPlayer2Name());
            team2Player2_ActionFragment =
                    PlayerActionFragment.newInstance(TEAM_2, PLAYER_2,
                            tennisMatch.getTennisTeam2().getPlayer2Name());

            ft.add(R.id.activity_match_score_tracker_PLAYER_ACTION_BOX_TWO,
                    team1Player2_ActionFragment);
            ft.add(R.id.activity_match_score_tracker_PLAYER_ACTION_BOX_FOUR,
                    team2Player2_ActionFragment);
        }

        ft.commitNow();

    }

    /**
     * Helper method -- update the scoreBox UI to reflect the current score
     */
    private void updateMatchScore() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        //size of this list should be equal to number of completed sets
        List<String> prevSetResults = tennisMatch.getAllPreviousSetResults();

        if (prevSetResults.size() >= 1) {
            String setOneResult = prevSetResults.get(0);
            String[] arrSetOneResult = parseScore(setOneResult, "-");
            String[] arrSetOneResultSimple = getSimpleSetResult(arrSetOneResult);

            SetScoreBoxFragment setOneResult_Fragment =
                    SetScoreBoxFragment.newInstance(arrSetOneResultSimple[0], arrSetOneResultSimple[1]);

            //(The score at index 0 will be for team1, and the score at index 1 will be for team2)
            ft.replace(R.id.activity_match_score_tracker_SCOREBOX_ONE, setOneResult_Fragment, SET_ONE_FRAGMENT_TAG);
        }

        if (prevSetResults.size() >= 2) {
            String setTwoResult = prevSetResults.get(1);
            String[] arrSetTwoResult = parseScore(setTwoResult, "-");
            String[] arrSetTwoResultSimple = getSimpleSetResult(arrSetTwoResult);

            //(The score at index 0 will be for team1, and the score at index 1 will be for team2)
            SetScoreBoxFragment setTwoResult_Fragment =
                    SetScoreBoxFragment.newInstance(arrSetTwoResultSimple[0], arrSetTwoResultSimple[1]);

            ft.replace(R.id.activity_match_score_tracker_SCOREBOX_TWO, setTwoResult_Fragment, SET_TWO_FRAGMENT_TAG);
        }

        if (prevSetResults.size() >= 3) {
            String setThreeResult = prevSetResults.get(2);
            String[] arrSetThreeResult = parseScore(setThreeResult, "-");
            String[] arrSetThreeResultSimple = getSimpleSetResult(arrSetThreeResult);

            //(The score at index 0 will be for team1, and the score at index 1 will be for team2)
            SetScoreBoxFragment setThreeResult_Fragment =
                    SetScoreBoxFragment.newInstance(arrSetThreeResultSimple[0], arrSetThreeResultSimple[1]);

            ft.replace(R.id.activity_match_score_tracker_SCOREBOX_THREE, setThreeResult_Fragment, SET_THREE_FRAGMENT_TAG);
        }

        if (prevSetResults.size() >= 4) {
            String setFourResult = prevSetResults.get(3);
            String[] arrSetFourResult = parseScore(setFourResult, "-");
            String[] arrSetFourResultSimple = getSimpleSetResult(arrSetFourResult);

            //(The score at index 0 will be for team1, and the score at index 1 will be for team2)
            SetScoreBoxFragment setFourResult_Fragment =
                    SetScoreBoxFragment.newInstance(arrSetFourResultSimple[0], arrSetFourResultSimple[1]);

            ft.replace(R.id.activity_match_score_tracker_SCOREBOX_FOUR, setFourResult_Fragment, SET_FOUR_FRAGMENT_TAG);
        }

        if (prevSetResults.size() >= 5) {
            String setFiveResult = prevSetResults.get(4);
            String[] arrSetFiveResult = parseScore(setFiveResult, "-");
            String[] arrSetFiveResultSimple = getSimpleSetResult(arrSetFiveResult);

            //(The score at index 0 will be for team1, and the score at index 1 will be for team2)
            SetScoreBoxFragment setFiveResult_Fragment =
                    SetScoreBoxFragment.newInstance(arrSetFiveResultSimple[0], arrSetFiveResultSimple[1]);

            ft.replace(R.id.activity_match_score_tracker_SCOREBOX_FIVE, setFiveResult_Fragment, SET_FIVE_FRAGMENT_TAG);
        }

        //Must also display score for current set and game if match is not yet finished
        if (!tennisMatch.isMatchFinished()) {

            //Get and parse currentSetScore
            String currentSetScore = tennisMatch.getSetScore(0);
            String[] arrCurrentSetScore = parseScore(currentSetScore, "-");

            //Get and parse currentGameScore
            String currentGameScore = tennisMatch.getGameScore(0);
            String[] arrCurrentGameScore = parseScore(currentGameScore, ":");

            SetScoreBoxFragment currentSet_Fragment =
                    SetScoreBoxFragment.newInstance(arrCurrentSetScore[0], arrCurrentSetScore[1]);
            GameScoreBoxFragment currentGame_Fragment =
                    GameScoreBoxFragment.newInstance(arrCurrentGameScore[0], arrCurrentGameScore[1]);


            switch (tennisMatch.getCurrentSetNumber()) {
                case (1):
                    ft.replace(R.id.activity_match_score_tracker_SCOREBOX_ONE,
                            currentSet_Fragment, CURRENT_SET_FRAGMENT_TAG);
                    ft.replace(R.id.activity_match_score_tracker_SCOREBOX_TWO,
                            currentGame_Fragment, CURRENT_GAME_FRAGMENT_TAG);
                    break;
                case (2):
                    ft.replace(R.id.activity_match_score_tracker_SCOREBOX_TWO,
                            currentSet_Fragment, CURRENT_SET_FRAGMENT_TAG);
                    ft.replace(R.id.activity_match_score_tracker_SCOREBOX_THREE,
                            currentGame_Fragment, CURRENT_GAME_FRAGMENT_TAG);
                    break;
                case (3):
                    ft.replace(R.id.activity_match_score_tracker_SCOREBOX_THREE,
                            currentSet_Fragment, CURRENT_SET_FRAGMENT_TAG);
                    ft.replace(R.id.activity_match_score_tracker_SCOREBOX_FOUR,
                            currentGame_Fragment, CURRENT_GAME_FRAGMENT_TAG);
                    break;
                case (4):
                    ft.replace(R.id.activity_match_score_tracker_SCOREBOX_FOUR,
                            currentSet_Fragment, CURRENT_SET_FRAGMENT_TAG);
                    ft.replace(R.id.activity_match_score_tracker_SCOREBOX_FIVE,
                            currentGame_Fragment, CURRENT_GAME_FRAGMENT_TAG);
                    break;
                case (5):
                    ft.replace(R.id.activity_match_score_tracker_SCOREBOX_FIVE,
                            currentSet_Fragment, CURRENT_SET_FRAGMENT_TAG);
                    ft.replace(R.id.activity_match_score_tracker_SCOREBOX_SIX,
                            currentGame_Fragment, CURRENT_GAME_FRAGMENT_TAG);
                    break;
            }
        } else {
            //If match is finished, remove the currSet and currGame fragment if they exist
            Fragment currSet = manager.findFragmentByTag(CURRENT_SET_FRAGMENT_TAG);
            Fragment currGame = manager.findFragmentByTag(CURRENT_GAME_FRAGMENT_TAG);

            if(currSet != null) {
                ft.remove(currSet);
            }
            if(currGame != null) {
                ft.remove(currGame);
            }
        }

        //Commit all changes!
        ft.commitNow();

    }

    private void displayUnsavedQuitDialog() {
        String returnMessage = "Return To Main Menu?\n All Match Data Will Be Lost!";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(returnMessage)
                .setCancelable(true)
                .setPositiveButton("Return To Main Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Go back to main activity
                        Intent intent = new Intent(MatchScoreTrackerActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    /**
     * Helper method -- parses the score (delimiter differs between game score and set score)
     *
     * @param str       the string to parse -- must only contain one instance of the delimiter
     * @param delimiter a delimiter
     * @return a String array containing the parsed score -- will be size 2.
     */
    private String[] parseScore(String str, String delimiter) {
        return str.split(delimiter);
    }

    /**
     * Returns the simplified set result by only returning the first character in each string
     * (removes any tiebreak score information)
     * @param setResult an array of size 2
     * @return a string array arr of size 2
     * where arr[0] = setResult[0].charAt(0} and arr[1] = setResult[1].charAt(0}
     */
    private String[] getSimpleSetResult(String[] setResult) {
        String[] simpleResult = new String[2];
        simpleResult[0] = Character.toString(setResult[0].charAt(0));
        simpleResult[1] = Character.toString(setResult[1].charAt(0));
        return simpleResult;
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
     *
     * @param teamNumber either 1 or 2. Calls addPointForTeam for team2 if 1 is passed in, and vice versa.
     */
    private void addPointAgainstTeam(int teamNumber) {
        if (teamNumber == TEAM_1) {
            addPointForTeam(TEAM_2);
        } else if (teamNumber == TEAM_2) {
            addPointForTeam(TEAM_1);
        }
    }

    /**
     * Helper method -- changes the state of our TennisMatch object,
     * and adjusts the UI to display correct score
     *
     * @param teamNumber either 1 to add a point for team1, or 2 to add a point for team2.
     */
    private void addPointForTeam(int teamNumber) {
        /*
        int completedSetsBefore = tennisMatch.getSetsCompleted();
        int completedSetsAfter;

         */

        //This will update the state of TennisMatch
        int winningTeam = tennisMatch.addPointForTeam(teamNumber);

        /*
        completedSetsAfter = tennisMatch.getSetsCompleted();

        //Adjust the fragment positions in our scoreBox UI if necessary - ie. if we've completed a set.
        if (completedSetsBefore != completedSetsAfter) {
            updateScoreBoxFragments(completedSetsAfter);
            updateCurrentSetTextView();
        }

         */

        //Updates the textView displaying the current set we're on
        updateCurrentSetTextView();
        //Updates our ScoreBoard UI to reflect the current score.
        updateMatchScore();

        //If there is a winningTeam
        if (winningTeam > 0) {
            //TODO: Some code for when the match is finished
            onMatchFinished(winningTeam);
        }
    }

    //------------------------------//
    //Code to execute when match is finished.

    private void onMatchFinished(int winningTeam) {
        String winningTeam_TeamName;
        String losingTeam_TeamName;
        int winningTeamPlayer1_ID, winningTeamPlayer2_ID, losingTeamPlayer1_ID, losingTeamPlayer2_ID;

        boolean team1_WIN = (winningTeam == TEAM_1); //True if team1_wins, False if team2_wins

        winningTeamPlayer1_ID = team1_WIN ?
                tennisMatch.getTennisTeam1().getPlayer1ID() : tennisMatch.getTennisTeam2().getPlayer1ID();
        losingTeamPlayer1_ID = team1_WIN ?
                tennisMatch.getTennisTeam2().getPlayer1ID() : tennisMatch.getTennisTeam1().getPlayer1ID();

        addWinIntoDatabase(winningTeamPlayer1_ID);
        addLossIntoDatabase(losingTeamPlayer1_ID);

        if (tennisMatch.isDoubles()) {
            winningTeamPlayer2_ID = team1_WIN ?
                    tennisMatch.getTennisTeam1().getPlayer2ID() : tennisMatch.getTennisTeam2().getPlayer2ID();
            losingTeamPlayer2_ID = team1_WIN ?
                    tennisMatch.getTennisTeam2().getPlayer2ID() : tennisMatch.getTennisTeam1().getPlayer2ID();

            addWinIntoDatabase(winningTeamPlayer2_ID);
            addLossIntoDatabase(losingTeamPlayer2_ID);
        }

        winningTeam_TeamName = team1_WIN ?
                tennisMatch.getTennisTeam1().getFullTeamName() : tennisMatch.getTennisTeam2().getFullTeamName();
        losingTeam_TeamName = team1_WIN ?
                tennisMatch.getTennisTeam2().getFullTeamName() : tennisMatch.getTennisTeam1().getFullTeamName();

        displayResultAndReturn(winningTeam, winningTeam_TeamName, losingTeam_TeamName);
    }


    /**
     * Display the result and score of the match, and force the user to go back to the main menu
     *
     * @param winningTeam     the number of the winningTeam (either 1 or 2)
     * @param winningTeamName the teamName of the winningTeam
     * @param losingTeamName  the teamName of the losingTeam
     */
    private void displayResultAndReturn(int winningTeam, String winningTeamName, String losingTeamName) {
        StringBuilder result = new StringBuilder();
        result.append(winningTeamName).append(" def. ").append(losingTeamName).append(" \n\nFinal Score: ");

        for (String setResult : tennisMatch.getAllPreviousSetResults()) {
            String[] setResultArray = parseScore(setResult, "-");

            if (winningTeam == TEAM_1) {
                result.append(setResultArray[0]).append("-").append(setResultArray[1]);
            } else if (winningTeam == TEAM_2) {
                result.append(setResultArray[1]).append("-").append(setResultArray[0]);
            }

            result.append(" ");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(result.toString())
                .setCancelable(false)
                .setPositiveButton("Return To Main Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Go back to main activity
                        Intent intent = new Intent(MatchScoreTrackerActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView text = alertDialog.findViewById(android.R.id.message);
        text.setTextSize(20);

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
     *
     * @param helper     PlayerDBHelper
     * @param playerName name of player to search for ID
     * @return the playerID, or -1 if not found.
     */
    private int getPlayerIDFromName(PlayerDBHelper helper, String playerName) {
        int playerID = -1;

        try {
            playerID = helper.getIDFromName(playerName);
        } catch (PlayerDoesNotExistException e) {
            Log.d(MatchScoreTrackerActivity.class.getSimpleName(), "Could not get an ID for playerName: " + playerName);
        }

        return playerID;
    }

    /**
     * Helper method to add a win into the database for the requested player.
     *
     * @param playerID player to add a win for
     * @return true if win successfully added into database, false otherwise
     */
    private boolean addWinIntoDatabase(int playerID) {
        int winsBefore;
        try {
            winsBefore = playerDBHelper.getWinsFromID(playerID);
            return playerDBHelper.updatePlayerWins(playerID, winsBefore + 1);
        } catch (PlayerDoesNotExistException e) {
            Log.d(MatchScoreTrackerActivity.class.getSimpleName(), "Could not get number of wins for playerID: " + playerID);
            return false;
        }
    }

    /**
     * Helper method to add a loss into the database for the requested player.
     *
     * @param playerID player to add a loss for
     * @return true if loss successfully added into database, false otherwise
     */
    private boolean addLossIntoDatabase(int playerID) {
        int lossesBefore;
        try {
            lossesBefore = playerDBHelper.getLossesFromID(playerID);
            return playerDBHelper.updatePlayerLosses(playerID, lossesBefore + 1);
        } catch (PlayerDoesNotExistException e) {
            Log.d(MatchScoreTrackerActivity.class.getSimpleName(), "Could not get number of losses for playerID: " + playerID);
            return false;
        }
    }


    //-----------------------------------------------//


}
