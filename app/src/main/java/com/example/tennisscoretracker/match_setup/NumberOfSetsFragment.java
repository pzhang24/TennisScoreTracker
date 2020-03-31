package com.example.tennisscoretracker.match_setup;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tennisscoretracker.R;

public class NumberOfSetsFragment extends Fragment {

    private static final String[] SET_OPTIONS_ARRAY = {"1", "3", "5"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_number_of_sets, container, false);

        while(getActivity() == null) {
            //Do nothing
        }

        Spinner setsSpinner = view.findViewById(R.id.fragment_number_of_sets_SPINNER);

        setSetsSpinner(setsSpinner, getActivity());

        return view;
    }

    /**
     * Sets up list of set number options for the spinner to display
     * @param spinner a Spinner
     * @param activity the Activity
     */
    private void setSetsSpinner(Spinner spinner, Activity activity) {
        if(getActivity() != null) {
            ArrayAdapter<String> setListAdapter = new ArrayAdapter<String>(activity,
                    android.R.layout.simple_list_item_1, SET_OPTIONS_ARRAY);

            setListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(setListAdapter);
        }

    }
}
