package com.example.tennisscoretracker.match.score_tracker_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tennisscoretracker.R;

//TODO: initialize listener... DONE

public class PlayerActionFragment extends Fragment {

    private PlayerActionListener listener;

    public interface PlayerActionListener {
        void onAceButtonClicked(int teamNumber, int playerNumber);
        void onWinnerButtonClicked(int teamNumber, int playerNumber);
        void onDoubleFaultButtonClicked(int teamNumber, int playerNumber);
        void onForcedErrorButtonClicked(int teamNumber, int playerNumber);
        void onUnforcedErrorButtonClicked(int teamNumber, int playerNumber);
    }

    private static final String TEAM_NUMBER_KEY = "team_number";
    private static final String PLAYER_NUMBER_KEY = "player_number";
    private static final String PLAYER_NAME_KEY = "player_name";

    private Button aceButton;
    private Button winnerButton;
    private Button doubleFaultButton;
    private Button forcedErrorButton;
    private Button unforcedErrorButton;

    //Fragment ID Fields - the team and player for which this PlayerActionFragment is for
    private int teamNumber;
    private int playerNumber;


    /**
     * Factory method to instantiate a new PlayerActionFragment
     * @param teamNumber The team number. (In the context of this app, either 1 for team1 or 2 for team2.)
     * @param playerNumber The player number. (In the context of this app, either 1 for player1 or 2 for player2.)
     * @param playerName
     * @return new instance of PlayerActionFragment
     */
    public static PlayerActionFragment newInstance(int teamNumber, int playerNumber, String playerName) {
        PlayerActionFragment fragment = new PlayerActionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TEAM_NUMBER_KEY, teamNumber);
        bundle.putInt(PLAYER_NUMBER_KEY, playerNumber);
        bundle.putString(PLAYER_NAME_KEY, playerName);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_action, container, false);

        setFragmentIDFields();

        configurePlayerNameTextView(view);
        configureAceButton(view);
        configureWinnerButton(view);
        configureDoubleFaultButton(view);
        configureForcedErrorButton(view);
        configureUnforcedErrorButton(view);

        return view;
    }

    private void setFragmentIDFields() {
        teamNumber = getArguments().getInt(TEAM_NUMBER_KEY);
        playerNumber = getArguments().getInt(PLAYER_NUMBER_KEY);
    }

    private void configurePlayerNameTextView(View parentView) {
        TextView playerName = parentView.findViewById(R.id.fragment_player_action_PLAYER_NAME);
        playerName.setText(getArguments().getString(PLAYER_NAME_KEY, "ERROR"));
    }

    private void configureUnforcedErrorButton(View view) {
        unforcedErrorButton = view.findViewById(R.id.fragment_player_action_UNFORCED_ERROR_BUTTON);
        unforcedErrorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onUnforcedErrorButtonClicked(teamNumber, playerNumber);
            }
        });
    }

    private void configureForcedErrorButton(View view) {
        forcedErrorButton = view.findViewById(R.id.fragment_player_action_FORCED_ERROR_BUTTON);
        forcedErrorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onForcedErrorButtonClicked(teamNumber, playerNumber);
            }
        });
    }

    private void configureDoubleFaultButton(View view) {
        doubleFaultButton = view.findViewById(R.id.fragment_player_action_DOUBLE_FAULT_BUTTON);
        doubleFaultButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onDoubleFaultButtonClicked(teamNumber, playerNumber);
            }
        });
    }

    private void configureWinnerButton(View view) {
        winnerButton = view.findViewById(R.id.fragment_player_action_WINNER_BUTTON);
        winnerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onWinnerButtonClicked(teamNumber, playerNumber);
            }
        });
    }

    private void configureAceButton(View view) {
        aceButton = view.findViewById(R.id.fragment_player_action_ACE_BUTTON);
        aceButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onAceButtonClicked(teamNumber, playerNumber);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PlayerActionListener) {
            listener = (PlayerActionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement PlayerActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
