package com.namib.ghosting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Random;

public class GhostSession extends AppCompatActivity {

    private TextView directionView;
    private TextView restView;
    private CountDownTimer timer;
    private CountDownTimer restTimer;
    private Button quitButton;

    private Bundle extras;
    private long sets;
    private long rest;
    private long shots;
    private long time;
    private boolean restInBetween;

    private MediaPlayer soundBite;

    private String[] directions = {"Front Right", "Front Left",
            "Volley Left", "Volley Right",
            "Back Left", "Back Right"};
    private HashMap<String, Integer> dirs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ghost_session_layout);

        directionView = (TextView) findViewById(R.id.directionTextView2);
        restView = (TextView) findViewById(R.id.restView2);

        extras = getIntent().getExtras();
        sets = (long) extras.getInt("sets");
        rest = (long) extras.getInt("rest");
        shots = (long) extras.getInt("shots");
        time = (long) extras.getInt("time");
        Log.d("input", "onCreate: " + getSets());

        dirs = new HashMap<>();
        dirs.put("Front Right", R.raw.frontright);
        dirs.put("Front Left", R.raw.frontleft);
        dirs.put("Volley Left", R.raw.volleyleft);
        dirs.put("Volley Right", R.raw.volleyright);
        dirs.put("Back Left", R.raw.backleft);
        dirs.put("Back Right", R.raw.backright);

        quitButton = (Button) findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                restTimer.cancel();
                soundBite.stop();
                Intent intent = new Intent(GhostSession.this, MainActivity.class);
                startActivity(intent);
            }
        });

        runSet().start();
    }

    private void reduceSetsByOne() {
        this.sets -= 1;
    }

    private long getSets() {
        return this.sets;
    }

    public boolean isRestInBetween() {
        return restInBetween;
    }

    public void setRestInBetween(boolean restInBetween) {
        this.restInBetween = restInBetween;
    }

    private void rerunSet() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runSet().start();
            }
        });
    }
     private void rerunRest() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runRest().start();
            }
        });
     }

    private CountDownTimer runSet() {
        Log.d("sets", "runSet: run");
        if (getSets() == 1) {
            setRestInBetween(false);
        }
        restView.setText("");
        long milliseconds = time * 1000 * shots;
        timer = new CountDownTimer(milliseconds, 1000 * time) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("TICK", "onTick: " + millisUntilFinished / 1000);
                String dir = randomDirection();
                soundBite = MediaPlayer.create(GhostSession.this, getSoundId(dir));
                soundBite.start();
                directionView.setText(dir);
            }

            @Override
            public void onFinish() {
                if (getSets() > 1) {
                    rerunRest();
                    reduceSetsByOne();
                }
                else {
                    directionView.setText("done mate");
                }
            }
        };
        return timer;
    }

    private CountDownTimer runRest() {
        restView.setText("Rest Dawg");
        restTimer = new CountDownTimer(rest * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                directionView.setText(Long.toString(millisUntilFinished/1000) + " s");
            }

            @Override
            public void onFinish() {
                if (getSets() >= 1) {
                    rerunSet();
                }
                else {
                    directionView.setText("Done mate");
                }
            }
        };
        return restTimer;
    }

    // to generate a random direction
    private String randomDirection() {
        int randomInt;
        Random random = new Random();
        randomInt = random.nextInt(6);
        return directions[randomInt];
    }

    private int getSoundId(String direction) {
        return dirs.get(direction);
    }
}
