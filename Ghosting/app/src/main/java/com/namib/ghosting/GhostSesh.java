package com.namib.ghosting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GhostSesh extends AppCompatActivity {

    private TextView directionView;
    private TextView restView;

    private Bundle extras;
    private long sets;
    private long rest;
    private long shots;
    private long time;

    private String[] directions = {"Front Right", "Front Left",
            "Volley Left", "Volley Right",
            "Back Left", "Back Right"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ghost_sesh_layout);

        directionView = (TextView) findViewById(R.id.directionTextView2);
        restView = (TextView) findViewById(R.id.restView2);

        extras = getIntent().getExtras();
        sets = (long) extras.getInt("sets");
        rest = (long) extras.getInt("rest");
        shots = (long) extras.getInt("shots");
        time = (long) extras.getInt("time");

        ghostSession();
    }

    private void ghostSession() {
        if (sets == 1) {
            runSet(false);
        }
        else {
            for (int i = 0; i < sets; i++) {
                runSet(true);
            }
        }
    }

    private void runSet(boolean restInBetween) {
        long milliseconds = time * 1000 * shots;
        CountDownTimer timer = new CountDownTimer(milliseconds, 1000 * time) {
            @Override
            public void onTick(long millisUntilFinished) {
                directionView.setText(randomDirection());
            }

            @Override
            public void onFinish() {
                if (restInBetween) {
                    runRest();
                }
                else {
                    directionView.setText("Done");
                }
            }

            private void runRest() {
                restView.setText("Rest Dawg");
                CountDownTimer restTimer = new CountDownTimer(rest * 1000, 1000) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long millisUntilFinished) {
                        directionView.setText(Long.toString(millisUntilFinished/1000) + " s");
                    }

                    @Override
                    public void onFinish() {
                        restView.setText("");
                        directionView.setText("");
                    }
                };
            }

            // to generate a random direction
            private String randomDirection() {
                int randomInt;
                Random random = new Random();
                randomInt = random.nextInt(6);
                return directions[randomInt];
            }
        };
    }
}
