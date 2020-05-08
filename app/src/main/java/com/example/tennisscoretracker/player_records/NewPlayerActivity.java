package com.example.tennisscoretracker.player_records;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tennisscoretracker.R;
import com.example.tennisscoretracker.player_database.PlayerNameAlreadyExistsException;
import com.example.tennisscoretracker.player_database.PlayerDBHelper;
import com.example.tennisscoretracker.player_database.TooManyPlayersException;

public class NewPlayerActivity extends AppCompatActivity {

    //TODO:SUPPORT BACK BUTTON

    PlayerDBHelper playerDB;
    EditText editPlayerName;
    Button addPlayer;

    private static final int ADD_PLAYER_SUCCESS = 1;
    private static final int ADD_PLAYER_FAILURE = 0;
    private static final int ADD_PLAYER_PLAYER_ALREADY_EXISTS = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        playerDB = PlayerDBHelper.getInstance(getApplicationContext());

        editPlayerName = findViewById(R.id.new_player_PLAYER_NAME_EDITTEXT);
        addPlayer = findViewById(R.id.new_player_ADD_PLAYER_BUTTON);

        configureAddPlayerButton();

    }

    /**
     * Configures the ADD PLAYER button to add the user-created player
     * to our database when clicked.
     */
    public void configureAddPlayerButton() {
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String playerName = editPlayerName.getText().toString();

                if (playerName.trim().isEmpty()) {

                    //Prompts user to enter a name if editText box is all whitespace
                    Toast.makeText(NewPlayerActivity.this,
                            "Enter TennisPlayer Name",
                            Toast.LENGTH_LONG).show();

                    //clear text in playerName editText widget
                    //must be done even if editText box is all whitespace (must clear out whitespace!)
                    editPlayerName.setText("");
                    return;
                }

                try {
                    boolean isInserted;
                    isInserted = playerDB.insertNewPlayer
                            (editPlayerName.getText().toString());

                    if (isInserted) {
                        String message = "New TennisPlayer Successfully Created";
                        Toast.makeText(NewPlayerActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        String message = "Unable to Create New TennisPlayer";
                        Toast.makeText(NewPlayerActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                } catch (PlayerNameAlreadyExistsException exception) {

                    String message = exception.getMessage();
                    Toast.makeText(NewPlayerActivity.this, message, Toast.LENGTH_LONG).show();

                } catch (TooManyPlayersException exception) {

                    String message = exception.getMessage();
                    Toast.makeText(NewPlayerActivity.this, message, Toast.LENGTH_LONG).show();
                } finally {

                    editPlayerName.setText("");

                }

            }
        });
    }

}
