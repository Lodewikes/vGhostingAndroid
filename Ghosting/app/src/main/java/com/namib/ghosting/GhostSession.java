package com.namib.ghosting;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ThreadLocalRandom;

public class GhostSession extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ghost_session_layout);

        Bundle extras = getIntent().getExtras();
        int sets = extras.getInt("sets");
        int rest = extras.getInt("rest");
        int shots = extras.getInt("shots");
        int time = extras.getInt("time");

        if (sets == 1) {
            directionTicker((long) time, R.id.directionTextView, (long) shots, (long) rest);
        }
        else {
            for (int j = 0; j < sets; j++) {
                // BUG Runs all at once
                // every x seconds display y
                directionTicker((long) time, R.id.directionTextView, (long) shots, (long) rest);
            }
        }
    }

    // every x seconds display random direction
    public void directionTicker(long seconds, int viewId, long shots, long rest) {
        long milliseconds = seconds * 1000 * shots;
        TextView view =  (TextView) findViewById(viewId);
        CountDownTimer timer = new CountDownTimer(milliseconds, 1000 * seconds) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("TICK", "onTick: " + millisUntilFinished / 1000);
                view.setText(randomDir());
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFinish() {
                view.setText("Done");
                // restTimer((long) rest, R.id.directionTextView);
            }
        }.start();
    }

    // used in directionTicker()
    // chooses random direction
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String randomDir() {
        String[] directions = {"Front Right", "Front Left",
                "Volley Left", "Volley Right",
                "Back Left", "Back Right"};
        int randomNr = ThreadLocalRandom.current().nextInt(0, 6);
        return directions[randomNr];
    }

    // rest x seconds between sets
    public void restTimer(long seconds, int viewId) {
        long milliseconds = seconds * 1000;
        TextView view =  (TextView) findViewById(viewId);
        TextView restView = (TextView) findViewById(R.id.restView);
        restView.setText("REST DAWG");

        CountDownTimer timer = new CountDownTimer(milliseconds, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("TICK", "onTick: " + millisUntilFinished / 1000);

                view.setText(Long.toString(millisUntilFinished/1000) + "s");
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFinish() {
                restView.setText("");
                view.setText("");
            }
        }.start();
    }
}
