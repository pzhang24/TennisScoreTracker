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
 * A Score Box with two TextViews, allowing you to store the set score for two teams.
 */
public class GameScoreBoxFragment extends Fragment {

    private static final String TOPTEXT_KEY = "top_text";
    private static final String BOTTOMTEXT_KEY = "bottom_text";

    //TODO: Add methods/fields to input a score into the fragment... DONE? NOT DONE :(((
    public static GameScoreBoxFragment newInstance(String topText, String bottomText) {
        GameScoreBoxFragment fragment = new GameScoreBoxFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TOPTEXT_KEY, topText);
        bundle.putString(BOTTOMTEXT_KEY, bottomText);
        fragment.setArguments(bundle);

        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_scorebox, container, false);

        setText(v, getArguments().getString(TOPTEXT_KEY), getArguments().getString(BOTTOMTEXT_KEY));

        return v;
    }


    /**
     * Changes the TextViews inside this SetScoreBoxFragment
     * @param topText text to set for top TextView
     * @param bottomText text to set for bottom TextView
     */
    private void setText(View v, String topText, String bottomText) {

        TextView topTextView = v.findViewById(R.id.fragment_game_scorebox_TOP_TEXTVIEW);
        TextView bottomTextView = v.findViewById(R.id.fragment_game_scorebox_BOTTOM_TEXTVIEW);

        topTextView.setText(topText);
        bottomTextView.setText(bottomText);
    }
}

