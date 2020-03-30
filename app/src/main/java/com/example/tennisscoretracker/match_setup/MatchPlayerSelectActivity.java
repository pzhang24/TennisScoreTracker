package com.example.tennisscoretracker.match_setup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.tennisscoretracker.R;

public class MatchPlayerSelectActivity extends AppCompatActivity {

    private static boolean isDoubles;
    private static final String TEAM_1_NAME = "Team 1";
    private static final String TEAM_2_NAME = "Team 2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_player_select);

        isDoubles = getIntent().getBooleanExtra(MatchSelectTypeActivity.IS_DOUBLES, false);

        addPlayerSelectFragment();
    }

    private void addPlayerSelectFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isDoubles) {
            DoublesSetupFragment doublesSetupFragmentTeam1 = new DoublesSetupFragment(TEAM_1_NAME);
            DoublesSetupFragment doublesSetupFragmentTeam2 = new DoublesSetupFragment(TEAM_2_NAME);

            fragmentTransaction.add(R.id.fragment_player_select_CONTAINER1, doublesSetupFragmentTeam1);
            fragmentTransaction.add(R.id.fragment_player_select_CONTAINER2, doublesSetupFragmentTeam2);

            fragmentTransaction.commit();


        } else {
            SinglesSetupFragment singlesSetupFragment = new SinglesSetupFragment();
            fragmentTransaction.add(R.id.fragment_player_select_CONTAINER1, singlesSetupFragment);
            fragmentTransaction.commit();
        }


    }
}
