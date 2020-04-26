package com.example.tennisscoretracker.match;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.tennisscoretracker.R;
import com.example.tennisscoretracker.match.score_tracker_fragments.PlayerActionFragment;

//TODO: implement PlayerActionFragment.PlayerActionListener
public class MatchScoreTrackerActivity extends AppCompatActivity {

    //TODO: modify corresponding layout-land file... MOSTLY DONE

    //TODO: Create an instance of match
    //TODO: Take in an intent
    //TODO: Create scorebox fragments
    //TODO: Create player action buttons in landscape xml file
    //TODO: Create logic to change match state and update scorebox fragments when player action buttons are clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Will use solely landscape for the time being
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_match_score_tracker);
    }
}
