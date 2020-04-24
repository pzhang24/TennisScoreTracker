package com.example.tennisscoretracker.match;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.tennisscoretracker.R;

public class MatchScoreTrackerActivity extends AppCompatActivity {

    //TODO: modify corresponding layout-land file

    //TODO: Create an instance of match, take in an intent,
    // create score fragments, create player action buttons in landscape xml file
    // create logic to change match state when player action buttons are clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Will use solely landscape for the time being
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_match_score_tracker);
    }
}
