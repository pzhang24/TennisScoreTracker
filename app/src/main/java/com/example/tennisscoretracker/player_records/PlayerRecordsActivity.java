package com.example.tennisscoretracker.player_records;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.example.tennisscoretracker.R;
import com.example.tennisscoretracker.player_database.PlayerDBHelper;
import com.example.tennisscoretracker.player_database.PlayerDoesNotExistException;


public class PlayerRecordsActivity extends AppCompatActivity {

    //TODO: Add new activity to view player profiles when their name is clicked
    //TODO: Add functionality to edit player info and delete players from database

    PlayerDBHelper playerDBHelper;
    ListView playerListView;
    Button newPlayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_records);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        playerDBHelper = PlayerDBHelper.getInstance(getApplicationContext());

        configurePlayerListView();

        configureNewPlayerButton();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Reflect any changes that may have been made to the list of players stored in the database
        configurePlayerListView();
    }

    private void configurePlayerListView() {
        //Can replace our current adapter later if we're feeling ambitious
        final ArrayAdapter<String> playerListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, playerDBHelper.getPlayerNamesList());

        playerListView = findViewById(R.id.player_list_LISTVIEW);
        playerListView.setAdapter(playerListAdapter);

        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String playerName = playerListAdapter.getItem(position);
                int playerID;

                try {
                    playerID = playerDBHelper.getIDFromName(playerName);

                    Intent intent = new Intent(PlayerRecordsActivity.this, PlayerProfileActivity.class);
                    intent.putExtra("playerID", playerID);
                    startActivity(intent);

                } catch (PlayerDoesNotExistException e ){
                    //Should never be executed in normal operation
                    Log.e(PlayerRecordsActivity.class.getSimpleName(), "ERROR: We could not " +
                            "get a playerID for " + playerName);
                }
            }
        });

        //TODO: (Noted above) Add functionality to view player profiles when their name is clicked

    }

    //configures the New Player Button -> when clicked, takes user to NewPlayerActivity
    private void configureNewPlayerButton(){
        newPlayerButton = (Button)findViewById(R.id.player_records_button_NEW_PLAYER);
        newPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayerRecordsActivity.this, NewPlayerActivity.class));
            }
        });
    }

}
