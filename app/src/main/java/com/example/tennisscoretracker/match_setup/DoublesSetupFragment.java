package com.example.tennisscoretracker.match_setup;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tennisscoretracker.R;
import com.example.tennisscoretracker.player_database.PlayerDBHelper;

public class DoublesSetupFragment extends Fragment {

    private String teamName;

    private PlayerDBHelper playerDBHelper;


    public DoublesSetupFragment(String teamName) {
        this.teamName = teamName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doubles_setup, container, false);
        TextView teamNameTextView = view.findViewById(R.id.fragment_doubles_setup_Team_Name_TEXTVIEW);
        teamNameTextView.setText(teamName);

        while(getActivity() == null) {
            //Do nothing
        }

        playerDBHelper = PlayerDBHelper.getInstance(getActivity());

        Spinner spinner1 = view.findViewById(R.id.fragment_singles_setup_Player_1_SPINNER);
        Spinner spinner2 = view.findViewById(R.id.fragment_singles_setup_Player_2_SPINNER);

        setPlayerSpinner(spinner1, getActivity());
        setPlayerSpinner(spinner2, getActivity());
        return view;
    }


    /**
     * Sets up list of players for the spinner to display
     * @param spinner a Spinner
     * @param activity the Activity
     */
    private void setPlayerSpinner(Spinner spinner, Activity activity) {
        ArrayAdapter<String> playerListAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_list_item_1, playerDBHelper.getPlayerNamesList());

        playerListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(playerListAdapter);
    }

}
