package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NameDifficultyScreen extends AppCompatActivity {
    private static String playerName = "";
    private static Difficulty difficulty;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_difficulty_screen);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.calm);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        // EASY BUTTON
        Button button0 = findViewById(R.id.easyButton);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty = Difficulty.EASY;
                recordName();
                showDifficulty();
            }
        });
        // NORMAL BUTTON
        Button button1 = findViewById(R.id.normalButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty = Difficulty.NORMAL;
                recordName();
                showDifficulty();
            }
        });
        // HARD BUTTON
        Button button2 = findViewById(R.id.hardButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty = Difficulty.HARD;
                recordName();
                showDifficulty();
            }
        });
        Button button3 = findViewById(R.id.goToWelcomeScreen);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NameDifficultyScreen.this, WelcomeScreen.class));
            }
        });
        Button button4 = findViewById(R.id.goToGamePage);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //here we are storing the input name and sending it
                TextView inputText = findViewById(R.id.name);
                // 02/28/2022 - added .trim() for better input validation.
                String str = inputText.getText().toString().trim();

                if (TextUtils.isEmpty(str)) {
                    inputText.setError("Cannot input null name :(");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), GamePage.class);
                intent.putExtra("playerName", str);
                intent.putExtra("difficulty", difficulty.ordinal());
                startActivity(intent);
            }
        });
    }

    private void showDifficulty() {
        TextView t = findViewById(R.id.gameInfo);
        t.setText(playerName + " has selected: " + difficulty.getDifficulty());
    }

    private void recordName() {
        TextView inputText = findViewById(R.id.name);
        playerName = inputText.getText().toString();
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