package com.example.tennisscoretracker.match_setup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Collections;

public class DoublesSetupFragment extends Fragment {

    private static final String TEAM_NAME_KEY = "team_name_key";

    //private String teamName;

    private Spinner spinner1;
    private Spinner spinner2;

    /*
    public DoublesSetupFragment(String teamName) {
        this.teamName = teamName;
    }

     */

    //Static factory method
    public static DoublesSetupFragment newInstance(String teamName) {
        DoublesSetupFragment doublesSetupFragment = new DoublesSetupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TEAM_NAME_KEY, teamName);

        doublesSetupFragment.setArguments(bundle);
        return doublesSetupFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doubles_setup, container, false);
        TextView teamNameTextView = view.findViewById(R.id.fragment_doubles_setup_Team_Name_TEXTVIEW);
        teamNameTextView.setText(getArguments().getString(TEAM_NAME_KEY));

        while(getActivity() == null) {
            Log.d(DoublesSetupFragment.class.getSimpleName(), "Waiting for getActivity() != null");
        }

        PlayerDBHelper playerDBHelper = PlayerDBHelper.getInstance(getActivity());

        spinner1 = view.findViewById(R.id.fragment_singles_setup_Player_1_SPINNER);
        spinner2 = view.findViewById(R.id.fragment_singles_setup_Player_2_SPINNER);

        ArrayList<String> playerList1 = playerDBHelper.getPlayerNamesList();
        Collections.shuffle(playerList1);
        ArrayList<String> playerList2 = playerDBHelper.getPlayerNamesList();
        Collections.shuffle(playerList2);

        setPlayerSpinner(spinner1, playerList1, getActivity());
        setPlayerSpinner(spinner2, playerList2, getActivity());
        return view;
    }


    /**
     * Sets up list of players for the spinner to display
     * @param spinner a Spinner
     * @param activity the Activity
     */
    private void setPlayerSpinner(Spinner spinner, ArrayList<String> playerList, Activity activity) {
        ArrayAdapter<String> playerListAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_list_item_1, playerList);

        playerListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(playerListAdapter);
    }

    /**
     * Gets the currently selected name in the spinner for player 1, if one exists
     * @return the name of the player, or null if no items exist in spinner
     */
    public String getPlayer1Name(){

        return spinner1.getSelectedItem() == null ?
                null : spinner1.getSelectedItem().toString();
    }


    /**
     * Gets the currently selected name in the spinner for player 2, if one exists
     * @return the name of the player, or null if no items exist in spinner
     */
    public String getPlayer2Name(){
        return spinner2.getSelectedItem() == null ?
                null : spinner2.getSelectedItem().toString();
    }

}
