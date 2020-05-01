package com.example.tennisscoretracker;

import android.content.Intent;
import android.os.Bundle;

import com.example.tennisscoretracker.match_setup.MatchSelectTypeActivity;
import com.example.tennisscoretracker.player_records.PlayerRecordsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

//Background pic can be found at
//https://www.google.com/search?rlz=1C1CHBF_enCA810CA811&sxsrf=ALeKk00OHnMEoRLa1otapeLnbSk8R2v6MQ:1588364138419&q=tennis+court+images+free&tbm=isch&chips=q:tennis+court+images+free,g_1:public+domain:m_RuS2V-FQk%3D&usg=AI4_-kQofITUZ3DF0ZuEcxN-UjWqh_GIXw&sa=X&ved=2ahUKEwjp9vnBvZPpAhWWvZ4KHY_4CfIQgIoDKAF6BAgKEAU&biw=1366&bih=625#imgrc=3YMB3hrPjMtLWM
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        configureNewMatchButton();
        configurePlayerRecordsButton();
        //TODO: Configure other buttons to begin other activities.
    }

    private void configureNewMatchButton() {
        Button newGameButton = findViewById(R.id.home_button_NEW_GAME);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MatchSelectTypeActivity.class));
            }
        });
    }

    private void configurePlayerRecordsButton(){
        Button playerRecordsButton = (Button)findViewById(R.id.home_button_PLAYER_RECORDS);
        playerRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PlayerRecordsActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
