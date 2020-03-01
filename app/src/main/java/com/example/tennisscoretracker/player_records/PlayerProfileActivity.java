package com.example.tennisscoretracker.player_records;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tennisscoretracker.MainActivity;
import com.example.tennisscoretracker.R;
import com.example.tennisscoretracker.player_database.PlayerDBHelper;
import com.example.tennisscoretracker.player_database.PlayerDoesNotExistException;
import com.example.tennisscoretracker.player_database.PlayerNameAlreadyExistsException;

//TODO: add update name and delete player buttons (already included in layout) -- use AlertDialog?
//TODO: figure out how to update TextView values when player name is changed
//TODO: figure out how to end activity when player is deleted (their profile dies with them)
public class PlayerProfileActivity extends AppCompatActivity {

    PlayerDBHelper playerDBHelper;
    private int playerID;
    private String playerName;
    private int playerWins;
    private int playerLosses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        playerID = getIntent().getIntExtra("playerID", -1);

        playerDBHelper = PlayerDBHelper.getInstance(getApplicationContext());

        try {
            playerName = playerDBHelper.getNameFromID(playerID);
            configureNameTextField(playerName);

            playerWins = playerDBHelper.getWinsFromID(playerID);
            configureWinsTextField(playerWins);

            playerLosses = playerDBHelper.getLossesFromID(playerID);
            configureLossesTextField(playerLosses);


        } catch (PlayerDoesNotExistException e) {
            //Should never be executed in normal operation
            Log.e(PlayerProfileActivity.class.getSimpleName(), "ERROR: We could not " +
                    "get player information for playerID: " + playerID);
        }

        configureChangePlayerNameButton();
        configureDeletePlayerButton();

    }

    /** Configures the ChangePlayerName Button, which displays a popup that allows the user
     * to change the name of the player whose profile is currently on display.
     */
    private void configureChangePlayerNameButton() {
        Button changeNameButton = findViewById(R.id.player_profile_CHANGE_PLAYER_NAME_BUTTON);
        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(PlayerProfileActivity.this);

                View view = getLayoutInflater().inflate(
                        R.layout.dialog_update_player_name, null);

                final EditText newName = view.findViewById(R.id.dialog_update_player_name_NEW_NAME_EDITTEXT);

                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                Button cancelButton = view.findViewById(R.id.dialog_update_player_name_CANCEL_BUTTON);
                Button updateButton = view.findViewById(R.id.dialog_update_player_name_UPDATE_BUTTON);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String newPlayerName = newName.getText().toString();
                            playerDBHelper.updatePlayerName(playerID, newPlayerName);

                            // If player name was successfully updated, we must change the
                            // name displayed in the player profile
                            configureNameTextField(newPlayerName);

                            dialog.dismiss();

                        } catch (PlayerNameAlreadyExistsException e) {

                            Toast.makeText(PlayerProfileActivity.this,
                                    e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
    }

    private void configureDeletePlayerButton() {
        Button deletePlayerButton = findViewById(R.id.player_profile_DELETE_PLAYER_BUTTON);
        deletePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(PlayerProfileActivity.this);

                View view = getLayoutInflater().inflate(
                        R.layout.dialog_delete_player, null);



                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                Button cancelButton = view.findViewById(R.id.dialog_delete_player_CANCEL_BUTTON);
                Button deleteButton = view.findViewById(R.id.dialog_delete_player_DELETE_BUTTON);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playerDBHelper.deletePlayer(playerID);
                        dialog.dismiss();

                        //player has been deleted, so end the PlayerProfileActivity
                        finish();
                    }
                });
            }
        });
    }


    private void configureNameTextField(String name){
        TextView nameTextView = findViewById(R.id.player_profile_PLAYER_NAME_DISPLAY_TEXTVIEW);
        nameTextView.setText(name);
    }

    private void configureWinsTextField(int wins){
        TextView nameTextView = findViewById(R.id.player_profile_PLAYER_WINS_DISPLAY_TEXTVIEW);
        nameTextView.setText(String.valueOf(wins));
    }

    private void configureLossesTextField(int losses) {
        TextView nameTextView = findViewById(R.id.player_profile_PLAYER_LOSSES_DISPLAY_TEXTVIEW);
        nameTextView.setText(String.valueOf(losses));
    }
}
