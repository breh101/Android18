package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WinScreen extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    // for stats
    private int totalDamage = 0;
    private int numTurrets = 0;
    private int numUpgrades = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_screen);
        getInfo();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.win);
        mediaPlayer.start();
        TextView damage = findViewById(R.id.damage_in);
        damage.setText(String.valueOf(totalDamage));
        TextView turrets = findViewById(R.id.turrets_in);
        turrets.setText(String.valueOf(numTurrets));
        TextView upgrades = findViewById(R.id.upgrades_in);
        upgrades.setText(String.valueOf(numUpgrades));

        Button playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WinScreen.this, WelcomeScreen.class));
            }
        });
    }

    private void getInfo() {
        Intent intent = getIntent();
        totalDamage = intent.getIntExtra("totalDamage", 0);
        numTurrets = intent.getIntExtra("numTurrets", 0);
        numUpgrades = intent.getIntExtra("numUpgrades", 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }
}