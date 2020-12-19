package com.example.spy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class PlayerActivity extends Activity {

    private Button nextPlayerBtn, newGameBtn, btnCountdown, btnBeSpy;

    private TextView locationTV,
            tvCountdown;

    private String[] playersLocation, locationsName;
    private boolean[] checkedLocations;

    private String andOtherSpies, spyName, player, modeName, divider1, divider2;

    private int index, playersCnt, spiesCnt, spiesCntFrom, spiesCntTo, minutes;

    private boolean isSpySeeEachOther, isSpyParadox, isDiffLoc, isSecure, nextPlayerIsChecked, timerRunning;

    private CountDownTimer countDownTimer;

    private SpyInitial spy;

    public long timer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spy_player_layout);

        // Button
        nextPlayerBtn = findViewById(R.id.btnNextPlayer);
        newGameBtn  = findViewById(R.id.btnNewGame);
        btnCountdown = findViewById(R.id.btnCountdown);
        btnBeSpy = findViewById(R.id.btnBeSpy);

        // TextView
        locationTV = findViewById(R.id.tvLocation);
        tvCountdown = findViewById(R.id.tvCountdown);
        tvCountdown.setText("" + minutes + ":00");

        // Intent
        try {
            playersCnt = getIntent().getIntExtra("playersCnt", 0);

            modeName = getIntent().getStringExtra("modeName");
            spiesCnt = getIntent().getIntExtra("spiesCnt",0);
            spiesCntFrom = getIntent().getIntExtra("spiesCntFrom",0);
            spiesCntTo = getIntent().getIntExtra("spiesCntTo",0);

            minutes = getIntent().getIntExtra("timer", 0);

            isSpySeeEachOther = getIntent().getBooleanExtra("isSpySeeEachOther", false);
            isSpyParadox = getIntent().getBooleanExtra("isSpyParadox", false);
            isDiffLoc = getIntent().getBooleanExtra("isDiffLoc", false);
            isSecure = getIntent().getBooleanExtra("isSecure", false);

            locationsName = getIntent().getStringArrayExtra("locationsName");


        } catch (Exception e) {
            System.out.println();
        }

        if(modeName.equals("sly")) {
            btnBeSpy.setEnabled(true);
        }

        nextPlayerIsChecked = false;
        timer = (minutes * 60000);
        index = 0;

        // Resources
        spyName = getResources().getString(R.string.spy_name);
        andOtherSpies = getResources().getString(R.string.other_spies_and);
        player = getResources().getString(R.string.player);
        divider1 = getResources().getString(R.string.other_spies_and);
        divider2 = getResources().getString(R.string.and);

        if(modeName.equals("standard")) {
            spy = new SpyInitial(playersCnt, spiesCnt, timer, isSpySeeEachOther, isSpyParadox, isDiffLoc, isSecure, locationsName,spyName, player, divider1, divider2);
        } else if(modeName.equals("rand")) {
            spy = new SpyInitial(playersCnt, spiesCntFrom, spiesCntTo, timer, isSpySeeEachOther, isSpyParadox, isDiffLoc, isSecure, locationsName,spyName, player, divider1, divider2);
        } else {
            spy = new SpyInitial(playersCnt, timer, isSpySeeEachOther, isSpyParadox, isDiffLoc, isSecure, locationsName,spyName, player, divider1, divider2);
        }

        playersLocation = spy.getPlayersLocation();

        btnCountdown.setOnClickListener(view -> PlayerActivity.this.startStop());


        /*======================================================================*/
        ListView lvDefence = findViewById(R.id.lvDefence);
        lvDefence.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        final String[] playersName = new String[playersCnt];
        int numb = 1;
        for (int i = 0; i < playersCnt; i++) {
            playersName[i] = "Игрок " + numb;
            numb++;
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, playersName);

        lvDefence.setAdapter(adapter);

        /*lvDefence.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                CheckedTextView check = (CheckedTextView)view;
                check.setChecked(!check.isChecked());

            }
        });*/
    }

    public void nextPlayerChoose(View view) {
        //index++;

        if (index == playersCnt) {
            nextPlayerBtn.setEnabled(false);
            locationTV.setText("Можете играть");
            startStop();
            return;
        }

        if (nextPlayerIsChecked) {
            locationTV.setText("");
            nextPlayerBtn.setText(R.string.choose_player_Btn);
        } else {
            if(view.getId() == btnBeSpy.getId()) {
                playersLocation[index] = spyName;
            }

            String locationName = playersLocation[index];
            locationTV.setText(locationName);
            nextPlayerBtn.setText(R.string.hide_player_Btn);
            index++;
        }

        nextPlayerIsChecked = !nextPlayerIsChecked;

    }

    public void playNewGame(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    public void startStop() {
        if (this.timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(this.timer, 1000) {
            public void onTick(long l) {
                long unused = PlayerActivity.this.timer = l;
                PlayerActivity.this.updateTimer();
            }

            public void onFinish() {
            }
        }.start();
        btnCountdown.setText(getResources().getString(R.string.timer_pause));
        timerRunning = true;
    }

    public void stopTimer() {
        countDownTimer.cancel();
        btnCountdown.setText(getResources().getString(R.string.timer_start));
        timerRunning = false;
    }

    public void updateTimer() {
        long j = this.timer;
        int seconds = (((int) j) % 60000) / 1000;
        String timeLeftText = ("" + (((int) j) / 60000)) + ":";
        if (seconds < 10) {
            timeLeftText = timeLeftText + "0";
        }
        String timeLeftText2 = timeLeftText + seconds;
        this.tvCountdown.setText(timeLeftText2);
        if (timeLeftText2.equals("0:00")) {
            this.btnCountdown.setEnabled(false);
            this.locationTV.setText(getResources().getString(R.string.end_game));
        }
    }
}
