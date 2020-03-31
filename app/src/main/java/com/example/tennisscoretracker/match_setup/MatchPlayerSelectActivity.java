package com.example.tennisscoretracker.match_setup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.tennisscoretracker.R;

public class MatchPlayerSelectActivity extends AppCompatActivity {

    private static boolean isDoubles;
    private static final String TEAM_1_NAME = "TennisTeam 1";
    private static final String TEAM_2_NAME = "TennisTeam 2";
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
            DoublesSetupFragment doublesSetupFragmentTeam1 = new DoublesSetupFragment(TEAM_1_NAME);
            DoublesSetupFragment doublesSetupFragmentTeam2 = new DoublesSetupFragment(TEAM_2_NAME);

            fragmentTransaction.add(R.id.fragment_player_select_CONTAINER1, doublesSetupFragmentTeam1);
            fragmentTransaction.add(R.id.fragment_player_select_CONTAINER2, doublesSetupFragmentTeam2);

        } else {
            SinglesSetupFragment singlesSetupFragment = new SinglesSetupFragment();
            fragmentTransaction.add(R.id.fragment_player_select_CONTAINER1, singlesSetupFragment);
        }


        //Set up the number of sets selection fragment
        NumberOfSetsFragment numberOfSetsFragment = new NumberOfSetsFragment();
        fragmentTransaction.add(R.id.fragment_player_select_CONTAINER_SETS, numberOfSetsFragment);

        fragmentTransaction.commit();
    }


}
