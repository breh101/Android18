package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreen extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.superepic);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        configureWelcomeButton();
    }

    private void configureWelcomeButton() {
        Button backButton = (Button) findViewById(R.id.welcomeButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeScreen.this, NameDifficultyScreen.class));
            }
        });
    }

    // For Media
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