package com.example.tennisscoretracker.match_setup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tennisscoretracker.R;

//TODO: Setup MatchPlayerSelectActivity
public class MatchSelectTypeActivity extends AppCompatActivity {

    public static final String IS_DOUBLES = "isDouble";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_select_type);
        
        configureSinglesButton();
        configureDoublesButton();
    }

    private void configureDoublesButton() {
        Button doublesButton = findViewById(R.id.match_select_type_DOUBLES_BUTTON);
        doublesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchSelectTypeActivity.this, MatchPlayerSelectActivity.class);
                intent.putExtra(IS_DOUBLES, true);
                startActivity(intent);
            }
        });
    }

    private void configureSinglesButton() {
        Button singlesButton = findViewById(R.id.match_select_type_SINGLES_BUTTON);
        singlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchSelectTypeActivity.this, MatchPlayerSelectActivity.class);
                intent.putExtra(IS_DOUBLES, false);
                startActivity(intent);
            }
        });
    }
}
