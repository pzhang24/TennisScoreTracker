package com.example.tennisscoretracker.match_setup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tennisscoretracker.R;
import com.example.tennisscoretracker.match.MatchScoreTrackerActivity;

import java.util.HashSet;
import java.util.Set;

public class MatchPlayerSelectActivity extends AppCompatActivity {

    private boolean isDoubles;
    private static final String TEAM_1_NAME = "Team 1";
    private static final String TEAM_2_NAME = "Team 2";

    //A team can have one or two players, depending on whether we're playing singles or doubles
    public static final String TEAM_1_PLAYER_1_EXTRA = "team_1_player_1";
    public static final String TEAM_1_PLAYER_2_EXTRA = "team_1_player_2";
    public static final String TEAM_2_PLAYER_1_EXTRA = "team_2_player_1";
    public static final String TEAM_2_PLAYER_2_EXTRA = "team_2_player_2";

    public static final String NUM_SETS_EXTRA = "num_sets";
    public static final String IS_DOUBLES_EXTRA = "is_doubles";

    private Button beginMatchButton;
    private DoublesSetupFragment doublesSetupFragmentTeam1;
    private DoublesSetupFragment doublesSetupFragmentTeam2;
    private SinglesSetupFragment singlesSetupFragment;
    private NumberOfSetsFragment numberOfSetsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_player_select);

        isDoubles = getIntent().getBooleanExtra(MatchSelectTypeActivity.IS_DOUBLES, false);

        addUIFragments();
        initializePlayMatchButton();
    }

    /**
     * Set up the UI display -
     * includes the player select fragment(s) and the number of sets select fragment
     */
    private void addUIFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Set up the player select fragment(s)
        if (isDoubles) {

            doublesSetupFragmentTeam1 = DoublesSetupFragment.newInstance(TEAM_1_NAME);
            doublesSetupFragmentTeam2 = DoublesSetupFragment.newInstance(TEAM_2_NAME);

            /*
            doublesSetupFragmentTeam1 = new DoublesSetupFragment(TEAM_1_NAME);
            doublesSetupFragmentTeam2 = new DoublesSetupFragment(TEAM_2_NAME);
               */
            fragmentTransaction.add(R.id.fragment_player_select_CONTAINER1, doublesSetupFragmentTeam1);
            fragmentTransaction.add(R.id.fragment_player_select_CONTAINER2, doublesSetupFragmentTeam2);

        } else {
            singlesSetupFragment = new SinglesSetupFragment();
            fragmentTransaction.add(R.id.fragment_player_select_CONTAINER1, singlesSetupFragment);
        }


        //Set up the number of sets selection fragment
        numberOfSetsFragment = new NumberOfSetsFragment();
        fragmentTransaction.add(R.id.fragment_player_select_CONTAINER_SETS, numberOfSetsFragment);

        fragmentTransaction.commit();
    }

    /**
     * Initialize the button to begin a match.
     */
    private void initializePlayMatchButton() {
        beginMatchButton = findViewById(R.id.activity_match_player_select_BEGIN_MATCH_BUTTON);
        beginMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Requires logical checks to make sure that a valid combination of players is selected
                //ie. A player cannot play with/against themselves

                if(isDoubles) {
                    //If format is doubles...
                    String team1Player1 = doublesSetupFragmentTeam1.getPlayer1Name();
                    String team1Player2 = doublesSetupFragmentTeam1.getPlayer2Name();
                    String team2Player1 = doublesSetupFragmentTeam2.getPlayer1Name();
                    String team2Player2 = doublesSetupFragmentTeam2.getPlayer2Name();

                    Set<String> checkDuplicateSet = new HashSet<>();

                    checkDuplicateSet.add(team1Player1);
                    checkDuplicateSet.add(team1Player2);
                    checkDuplicateSet.add(team2Player1);
                    checkDuplicateSet.add(team2Player2);

                    if(checkDuplicateSet.size() != 4) {
                        Toast.makeText(MatchPlayerSelectActivity.this,
                                "All four players selected must be different players!", Toast.LENGTH_SHORT).show();
                    } else {
                        //Begin MatchScoreTrackerActivity
                        Intent intent = new Intent(
                                MatchPlayerSelectActivity.this, MatchScoreTrackerActivity.class);
                        intent.putExtra(IS_DOUBLES_EXTRA, isDoubles);
                        intent.putExtra(NUM_SETS_EXTRA, numberOfSetsFragment.getNumberOfSets());
                        intent.putExtra(TEAM_1_PLAYER_1_EXTRA, team1Player1);
                        intent.putExtra(TEAM_1_PLAYER_2_EXTRA, team1Player2);
                        intent.putExtra(TEAM_2_PLAYER_1_EXTRA, team2Player1);
                        intent.putExtra(TEAM_2_PLAYER_2_EXTRA, team2Player2);
                        startActivity(intent);

                    }

                } else {
                    //if format is singles...
                    String player1 = singlesSetupFragment.getPlayer1Name();
                    String player2 = singlesSetupFragment.getPlayer2Name();

                    if(player1.equals(player2)) {
                        Toast.makeText(MatchPlayerSelectActivity.this,
                                "The two players selected must be different players!", Toast.LENGTH_SHORT).show();
                    } else {
                        //Begin MatchScoreTrackerActivity
                        Intent intent = new Intent(
                                MatchPlayerSelectActivity.this, MatchScoreTrackerActivity.class);
                        intent.putExtra(IS_DOUBLES_EXTRA, isDoubles);
                        intent.putExtra(NUM_SETS_EXTRA, numberOfSetsFragment.getNumberOfSets());
                        intent.putExtra(TEAM_1_PLAYER_1_EXTRA, player1);
                        intent.putExtra(TEAM_2_PLAYER_1_EXTRA, player2);
                        startActivity(intent);
                    }
                }

            }
        });
    }


}
