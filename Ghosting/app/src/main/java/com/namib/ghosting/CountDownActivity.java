package com.namib.ghosting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CountDownActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countdown_layout);
        cntDwnTimer(10, R.id.countdownView);
    }

    public void cntDwnTimer(long seconds, int viewId) {
        long milliseconds = seconds * 1000;
        TextView view = (TextView) findViewById(viewId);
        CountDownTimer timer = new CountDownTimer(milliseconds, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                view.setText(Long.toString(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Bundle extras = getIntent().getExtras();
                Intent intent = new Intent(CountDownActivity.this, GhostSession.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        }.start();
    }
}
