package com.namib.ghosting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private Button startBtn;
    private Button countdownBtn;


    private int sets;
    private int rest;
    private int shots;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputListner(R.id.setsInput, R.id.setsError);
        inputListner(R.id.restInput, R.id.restError);
        inputListner(R.id.shotsInput, R.id.shotsError);
        inputListner(R.id.timeBetweenShotsInput, R.id.timeError);

        addButtonListener(R.id.setsAdd, R.id.setsInput);
        addButtonListener(R.id.restAdd, R.id.restInput);
        addButtonListener(R.id.shotsAdd, R.id.shotsInput);
        addButtonListener(R.id.timeBetweenShotsAdd, R.id.timeBetweenShotsInput);
        subtractButtonListener(R.id.setsLess, R.id.setsInput);
        subtractButtonListener(R.id.restLess, R.id.restInput);
        subtractButtonListener(R.id.shotsLess, R.id.shotsInput);
        subtractButtonListener(R.id.timeBetweenShotsLess, R.id.timeBetweenShotsInput);

        // TODO onlclick listners for all buttons
        startButtons();
    }


    public void inputListner(int id, int errorBarId) {
        TextView view;
        view = (TextView) findViewById(id);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                changeErrorStatus(errorBarId, "");
                if (s.toString().isEmpty()) {
                    Log.d("TAG", "afterTextChanged: no input");
                    changeErrorStatus(errorBarId, "No input");
                }
                else if (Integer.parseInt(s.toString()) < 1) {
                    Log.d("TAG", "afterTextChanged: invalid number");
                    changeErrorStatus(errorBarId, "invalid number");
                }
            }

            private void changeErrorStatus(int id, String msg) {
                TextView view;
                view = (TextView) findViewById(id);
                view.setText(msg);
            }
        });
    }
    public int getInput(int id) {
        TextView view;
        view = (TextView) findViewById(id);
        return Integer.parseInt(view.getText().toString());
    }

    // suppress Lint checks that case conversions not bugging due to keyboard layouts
    // should not be an issue as we are working with numbers
    public void addButtonListener(int idBtn, int idView) {
        ImageButton btn = (ImageButton) findViewById(idBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("DefaultLocale")
            public void onClick(View v) {
                TextView view = (TextView) findViewById(idView);
                int value = Integer.parseInt(view.getText().toString());
                value += 1;
                view.setText(String.format("%d",value));
            }
        });
    }

    private void subtractButtonListener(int setsLess, int setsInput) {
        ImageButton btn = (ImageButton) findViewById(setsLess);
        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                TextView view = (TextView) findViewById(setsInput);
                int value = Integer.parseInt(view.getText().toString());
                value -= 1;
                view.setText(String.format("%d",value));
            }
        });
    }

    public void startButtons() {
        startBtn = (Button) findViewById(R.id.startBtn);
        countdownBtn = (Button) findViewById(R.id.countdownBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButton(MainActivity.this, GhostSession.class);
            }
        });

        countdownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButton(MainActivity.this, CountDownActivity.class);
            }
        });
    }

    // called in startButtons
    private void handleButton(Context context, Class<?> cls) {
        sets = getInput(R.id.setsInput);
        rest = getInput(R.id.restInput);
        shots = getInput(R.id.shotsInput);
        time = getInput(R.id.timeBetweenShotsInput);
        Bundle extras = new Bundle();

        // TODO start activity
        if (sets >= 1 && rest >= 1 && shots >= 1 && time >= 1) {
            if (sets == 1) {
                rest = 0;
                switchActivity(context, cls, extras);
            }
            else {
                switchActivity(context, cls, extras);
            }
        }
        else {
            Toast.makeText(
                    MainActivity.this,
                    "Oops! Invalid input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // called in handleButton
    private void switchActivity(Context context, Class<?> cls, Bundle extras) {
        extras.putInt("sets", sets);
        extras.putInt("rest", rest);
        extras.putInt("shots", shots);
        extras.putInt("time", time);

        Intent intent = new Intent(context, cls);
        intent.putExtras(extras);

        startActivity(intent);
    }
}