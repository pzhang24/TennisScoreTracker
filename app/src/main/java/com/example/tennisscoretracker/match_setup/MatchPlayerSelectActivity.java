package com.example.tennisscoretracker.match_setup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tennisscoretracker.R;

import java.util.HashSet;
import java.util.Set;

public class MatchPlayerSelectActivity extends AppCompatActivity {

    private boolean isDoubles;
    private static final String TEAM_1_NAME = "TennisTeam 1";
    private static final String TEAM_2_NAME = "TennisTeam 2";

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
            doublesSetupFragmentTeam1 = new DoublesSetupFragment(TEAM_1_NAME);
            doublesSetupFragmentTeam2 = new DoublesSetupFragment(TEAM_2_NAME);

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

    private void initializePlayMatchButton() {
        beginMatchButton = findViewById(R.id.activity_match_player_select_BEGIN_MATCH_BUTTON);
        beginMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        //TODO: start match activity
                    }

                } else {
                    //if format is singles...
                    String player1 = singlesSetupFragment.getPlayer1Name();
                    String player2 = singlesSetupFragment.getPlayer2Name();

                    if(player1.equals(player2)) {
                        Toast.makeText(MatchPlayerSelectActivity.this,
                                "The two players selected must be different players!", Toast.LENGTH_SHORT).show();
                    } else {
                        //TODO: start match activity
                    }
                }

            }
        });
    }


}
