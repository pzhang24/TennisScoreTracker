package com.example.tennisscoretracker.match.score_tracker_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tennisscoretracker.R;

/**
 * A Score Box with two TextViews, allowing you to store playerNames/teamNames/score for two teams.
 */
public class ScoreBoxFragment extends Fragment {

    //TODO: Add methods/fields to input a score into the fragment... DONE?



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scorebox, container, false);
    }


    /**
     * Changes the TextViews inside this ScoreBoxFragment
     * @param topText text to set for top TextView
     * @param bottomText text to set for bottom TextView
     */
    public void setText(String topText, String bottomText) {
        TextView topTextView = this.getView().findViewById(R.id.fragment_scorebox_TOP_TEXTVIEW);
        TextView bottomTextView = this.getView().findViewById(R.id.fragment_scorebox_BOTTOM_TEXTVIEW);

        topTextView.setText(topText);
        bottomTextView.setText(bottomText);
    }
}
