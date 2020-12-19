package com.example.spy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class SettingsGameActivity extends FragmentActivity {

    private final String TAG = "ModesActivity";

    private String locationsFile = "locations.json";
    private String neutralBtnDial;
    private String checkLocToast;
    private Boolean isCheckAllLocationsDial, isCanStartGame;

    private Button btnStandardSpyMode, btnRandSpyMode, btnSlySpyMode;

    private Fragment standardSpyModeFragment, randSpyModeFragment, slySpyModeFragment;
    private Drawable selBtnDraw, notSelBtnDraw;

    private NumberPicker npPlayersCnt, npSpiesCnt, npSpiesCntFrom, npSpiesCntTo, npTimer;
    private int colorTextItem, colorTextItemDesc, colorTextItemNotActive, colorTextDescNotActive;
    private final int playersMin = SpyInitialValues.PLAYERS_FROM.getValue(),
            playersMax = SpyInitialValues.PLAYERS_TO.getValue(),
            timerMin = SpyInitialValues.TIMER_FROM.getValue(),
            timerMax = SpyInitialValues.TIMER_TO.getValue();

    private CheckBox cbIsSpySeeEachOther, cbIsSpyParadox, cbIsDiffLoc, cbIsSecure;
    private TextView tvIsSpySeeEachOtherDesc, tvIsSpyParadoxDesc, tvIsDiffLocDesc, tvSelectedLocations;
    private List<Location> locations;

    private String[] locationsName;
    private List<String> checkedLocationsName;
    private boolean[] checkedLocations;

    private FragmentManager fragmentManager;

    private static int playersCnt;
    private static String modeSpyCntName = "";
    private static int spiesCnt, spiesCntFrom, spiesCntTo, spiesCntFromMin, spiesCntToMin, spiesCntFromMax, spiesCntToMax;
    private static int timer;
    private static boolean isSpySeeEachOther, isSpyParadox, isDiffLoc, isSecure;
    private static SharedPreferences spSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_game_layout);

        // Получить локации
        locations = JSONLocationParser.locationsFromJSON(this, locationsFile);

        locationsName = LocationsUtils.getLocationsName(locations);

        getGameSettings();

        neutralBtnDial = getResources().getString(R.string.dialog_check_locations_check_all_btn);
        checkLocToast = getResources().getString(R.string.check_locations_toast);

        checkedLocationsName = new ArrayList<>();

        selBtnDraw = getResources().getDrawable(R.drawable.spy_mode_selected_button);
        notSelBtnDraw = getResources().getDrawable(R.drawable.spy_mode_not_selected_button);

        colorTextItem = getResources().getColor(R.color.colorTextItem);
        colorTextItemDesc = getResources().getColor(R.color.colorTextItemDesc);
        colorTextItemNotActive = getResources().getColor(R.color.colorTextItemNotActive);
        colorTextDescNotActive = getResources().getColor(R.color.colorTextDescNotActive);

        // Кнопки выбора режима шпионов
        btnStandardSpyMode = findViewById(R.id.btnStandardSpyMode);
        btnRandSpyMode = findViewById(R.id.btnRandSpyMode);
        btnSlySpyMode = findViewById(R.id.btnSlySpyMode);

        // Фрагменты
        standardSpyModeFragment = new StandardSpyModeFragment();
        randSpyModeFragment = new RandSpyModeFragment();
        slySpyModeFragment = new SlySpyModeFragment();

        fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().add(R.id.flSpyMode, standardSpyModeFragment).commit();
        /*btnStandardSpyMode.setBackground(selBtnDraw);
        btnRandSpyMode.setBackground(notSelBtnDraw);
        btnSlySpyMode.setBackground(notSelBtnDraw);*/

        switch (modeSpyCntName) {
            case "rand":
                setActiveSpyModeButton(btnRandSpyMode.getId());
                break;
            case "sly":
                setActiveSpyModeButton(btnSlySpyMode.getId());
                break;
            default:
                setActiveSpyModeButton(btnStandardSpyMode.getId());
                break;
        }


        // NumberPicker
        npPlayersCnt = findViewById(R.id.npPlayersCnt);
        npTimer = findViewById(R.id.npTimer);

        /*npSpiesCnt.setValue(spiesCnt);
        npSpiesCntFrom.setValue(spiesCntFrom);
        npSpiesCntTo.setValue(spiesCntTo);*/

        // CheckBox
        cbIsSpySeeEachOther = findViewById(R.id.cbIsSpySeeEachOther);
        cbIsSpyParadox = findViewById(R.id.cbIsSpyParadox);
        cbIsDiffLoc = findViewById(R.id.cbIsDiffLoc);
        cbIsSecure = findViewById(R.id.cbIsSecure);
        cbIsSpySeeEachOther.setChecked(isSpySeeEachOther);
        cbIsSpyParadox.setChecked(isSpyParadox);
        cbIsDiffLoc.setChecked(isDiffLoc);
        cbIsSecure.setChecked(isSecure);

        // TextView
        tvIsSpySeeEachOtherDesc = findViewById(R.id.tvIsSpySeeEachOtherDesc);
        tvIsSpyParadoxDesc = findViewById(R.id.tvIsSpyParadoxDesc);
        tvIsDiffLocDesc = findViewById(R.id.tvIsDiffLocDesc);
        tvSelectedLocations = findViewById(R.id.tvSelectedLocations);

        setTextAndCheckBoxVisibility();
        setLocationsTextView();

        // NumberPicker инициализация
        npPlayersCnt.setMinValue(playersMin);
        npPlayersCnt.setMaxValue(playersMax);
        npPlayersCnt.setValue(playersCnt);
        npPlayersCnt.setWrapSelectorWheel(false);

        npTimer.setMinValue(timerMin);
        npTimer.setMaxValue(timerMax);
        npTimer.setValue(timer);
        //npTimer.setValue(timerMin);
        npTimer.setWrapSelectorWheel(false);

        isCanStartGame = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(modeSpyCntName.equals("standard")) {
            npSpiesCnt = findViewById(R.id.npSpiesCnt);
            npSpiesCnt.setValue(spiesCnt);
        } else if(modeSpyCntName.equals("rand")) {
            npSpiesCntFrom = findViewById(R.id.npSpiesCntFrom);
            npSpiesCntTo = findViewById(R.id.npSpiesCntTo);

            npSpiesCntFrom.setMinValue(spiesCntFromMin);
            npSpiesCntFrom.setMaxValue(spiesCntFromMax);
            npSpiesCntFrom.setValue(spiesCntFrom);
            npSpiesCntTo.setMinValue(spiesCntToMin);
            npSpiesCntTo.setMaxValue(spiesCntToMax);
            npSpiesCntTo.setValue(spiesCntTo);
        }
    }

    // Получить данные настроек
    public void getGameSettings() {
        SettingsGame.getGameSettings(this, locations.size());

        playersCnt = SettingsGame.getPlayersCnt();

        modeSpyCntName = SettingsGame.getModeSpyCntName();
        spiesCnt = SettingsGame.getSpiesCnt();
        spiesCntFrom  = SettingsGame.getSpiesCntFrom();
        spiesCntTo = SettingsGame.getSpiesCntTo();
        spiesCntFromMin  = SettingsGame.getSpiesCntFromMin();
        spiesCntToMin = SettingsGame.getSpiesCntToMin();
        spiesCntFromMax  = SettingsGame.getSpiesCntFromMax();
        spiesCntToMax = SettingsGame.getSpiesCntToMax();

        timer  = SettingsGame.getTimer();

        isSpySeeEachOther  = SettingsGame.isSpySeeEachOther();
        isSpyParadox  = SettingsGame.isSpyParadox();
        isDiffLoc = SettingsGame.isDiffLoc();
        isSecure  = SettingsGame.isSecure();

        checkedLocations = LocationsUtils.getCheckedLocations(locations);
        isCheckAllLocationsDial = checkedLocations[0];
    }

    // Текст выбраных локаций
    public void setLocationsTextView() {
        StringBuilder res = new StringBuilder();
        checkedLocationsName = new ArrayList<>();

        int posID = 1;
        String newLine = "";
        for (int i = 0; i < locations.size(); i++) {
            if (checkedLocations[i]) {
                checkedLocationsName.add(locationsName[i]);
                res.append(newLine).append(posID).append(". ").append(locationsName[i]);
                newLine = "\n";
                posID++;
            }
        }

        if (posID < 3) {
            isCanStartGame = false;
            Toast.makeText(SettingsGameActivity.this, checkLocToast, Toast.LENGTH_SHORT).show();
        }
        tvSelectedLocations.setText(res.toString());
    }

    // Изменение режима шпионов (кнопки)
    public void changeSpyMode(View view) {
        setActiveSpyModeButton(view.getId());
    }

    // Установка режима для кнопок режима шпиона
    public void setActiveSpyModeButton(int id) {
        if (id == btnStandardSpyMode.getId() && btnStandardSpyMode.getBackground() != selBtnDraw) {
            fragmentManager.beginTransaction().remove(standardSpyModeFragment).commit();
            fragmentManager.beginTransaction().remove(randSpyModeFragment).commit();
            fragmentManager.beginTransaction().remove(slySpyModeFragment).commit();

            btnRandSpyMode.setBackground(notSelBtnDraw);
            btnSlySpyMode.setBackground(notSelBtnDraw);

            fragmentManager.beginTransaction().add(R.id.flSpyMode, standardSpyModeFragment).commit();
            btnStandardSpyMode.setBackground(selBtnDraw);

            /*btnStandardSpyMode.setClickable(false);
            btnRandSpyMode.setClickable(true);
            btnSlySpyMode.setClickable(true);*/
        } else if (id == btnRandSpyMode.getId() && btnRandSpyMode.getBackground() != selBtnDraw) {
            fragmentManager.beginTransaction().remove(standardSpyModeFragment).commit();
            fragmentManager.beginTransaction().remove(randSpyModeFragment).commit();
            fragmentManager.beginTransaction().remove(slySpyModeFragment).commit();

            btnStandardSpyMode.setBackground(notSelBtnDraw);
            btnSlySpyMode.setBackground(notSelBtnDraw);

            fragmentManager.beginTransaction().add(R.id.flSpyMode, randSpyModeFragment).commit();
            btnRandSpyMode.setBackground(selBtnDraw);

            /*btnStandardSpyMode.setClickable(true);
            btnRandSpyMode.setClickable(false);
            btnSlySpyMode.setClickable(true);*/
        } else if (id == btnSlySpyMode.getId() && btnSlySpyMode.getBackground() != selBtnDraw) {
            fragmentManager.beginTransaction().remove(standardSpyModeFragment).commit();
            fragmentManager.beginTransaction().remove(randSpyModeFragment).commit();
            fragmentManager.beginTransaction().remove(slySpyModeFragment).commit();

            btnStandardSpyMode.setBackground(notSelBtnDraw);
            btnRandSpyMode.setBackground(notSelBtnDraw);

            fragmentManager.beginTransaction().add(R.id.flSpyMode, slySpyModeFragment).commit();
            btnSlySpyMode.setBackground(selBtnDraw);

            /*btnStandardSpyMode.setClickable(true);
            btnRandSpyMode.setClickable(true);
            btnSlySpyMode.setClickable(false);*/
        }
    }

    // CheckBox
    public void changeSpyVisibility(View view) {
        if (view.getId() == cbIsSpySeeEachOther.getId()) {
            if (cbIsSpySeeEachOther.isChecked()) {
                cbIsSpyParadox.setChecked(false);
                cbIsDiffLoc.setChecked(false);

                cbIsSpyParadox.setEnabled(false);
                cbIsDiffLoc.setEnabled(false);

                cbIsSpyParadox.setTextColor(colorTextItemNotActive);
                cbIsDiffLoc.setTextColor(colorTextItemNotActive);
                tvIsSpyParadoxDesc.setTextColor(colorTextDescNotActive);
                tvIsDiffLocDesc.setTextColor(colorTextDescNotActive);
            } else {
                cbIsSpyParadox.setEnabled(true);
                //cbIsDiffLoc.setEnabled(true);

                cbIsSpyParadox.setTextColor(colorTextItem);
                //cbIsDiffLoc.setTextColor(colorTextItem);

                tvIsSpyParadoxDesc.setTextColor(colorTextItemDesc);
                //tvIsDiffLocDesc.setTextColor(colorTextItemDesc);
            }
        } else if (view.getId() == cbIsSpyParadox.getId()) {
            if (cbIsSpyParadox.isChecked()) {
                cbIsDiffLoc.setEnabled(true);
                cbIsDiffLoc.setTextColor(colorTextItem);
                tvIsDiffLocDesc.setTextColor(colorTextItemDesc);
            } else {
                cbIsDiffLoc.setChecked(false);
                cbIsDiffLoc.setEnabled(false);
                cbIsDiffLoc.setTextColor(colorTextItemNotActive);
                tvIsDiffLocDesc.setTextColor(colorTextDescNotActive);
            }
        }
    }

    // CheckBox метод
    public void setTextAndCheckBoxVisibility() {
        if(isSpySeeEachOther) {
            cbIsSpyParadox.setEnabled(false);
            cbIsDiffLoc.setEnabled(false);

            cbIsSpyParadox.setTextColor(colorTextItemNotActive);
            cbIsDiffLoc.setTextColor(colorTextItemNotActive);
            tvIsSpyParadoxDesc.setTextColor(colorTextDescNotActive);
            tvIsDiffLocDesc.setTextColor(colorTextDescNotActive);
        } else if(isSpyParadox) {
            cbIsDiffLoc.setEnabled(true);
            cbIsDiffLoc.setTextColor(colorTextItem);
            tvIsDiffLocDesc.setTextColor(colorTextItemDesc);
        }
    }

    // Диалоговое окно выбора локаций
    public void checkLocations(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_check_locations_header);

        builder.setMultiChoiceItems(locationsName, checkedLocations, (dialog, which, isChecked) -> {
        });
        builder.setPositiveButton(R.string.dialog_positive, (dialog, which) -> setLocationsTextView());
        builder.setNegativeButton(R.string.dialog_negative, null);
        builder.setNeutralButton(neutralBtnDial, null);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        isCheckAllLocationsDial = !isCheckAllLocationsDial;

                        ListView list = dialog.getListView();

                        Arrays.fill(checkedLocations, isCheckAllLocationsDial);
                        System.out.println(list.getCount());
                        for (int i = 0; i < list.getCount(); i++) {
                            list.setItemChecked(i, isCheckAllLocationsDial);
                            //list.performItemClick(list, i, 1);
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    // Начало игры
    public void startGame(View view) {
        npSpiesCnt = findViewById(R.id.npSpiesCnt);
        npSpiesCntTo = findViewById(R.id.npSpiesCntTo);
        npSpiesCntFrom = findViewById(R.id.npSpiesCntFrom);

        if(!isCanStartGame) {
            Toast.makeText(SettingsGameActivity.this, checkLocToast, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(SettingsGameActivity.this, PlayerActivity.class);

        // Data
        playersCnt = npPlayersCnt.getValue();

        if(btnStandardSpyMode.getBackground() == selBtnDraw) {
            modeSpyCntName = "standard";
            spiesCnt = npSpiesCnt.getValue();
        } else if(btnRandSpyMode.getBackground() == selBtnDraw) {
            modeSpyCntName = "rand";
            spiesCntFrom = npSpiesCntFrom.getValue();
            spiesCntTo = npSpiesCntTo.getValue();
            spiesCntFromMin = npSpiesCntFrom.getMinValue();
            spiesCntToMin = npSpiesCntTo.getMinValue();
            spiesCntFromMax = npSpiesCntFrom.getMaxValue();
            spiesCntToMax = npSpiesCntTo.getMaxValue();
        } else {
            modeSpyCntName = "sly";
        }

        timer = npTimer.getValue();

        isSpySeeEachOther = cbIsSpySeeEachOther.isChecked();
        isSpyParadox = cbIsSpyParadox.isChecked();
        isDiffLoc = cbIsDiffLoc.isChecked();
        isSecure = cbIsSecure.isChecked();

        // Настройки
        SettingsGame.setGameSettings(this, playersCnt, modeSpyCntName, spiesCnt, spiesCntFrom, spiesCntTo, spiesCntFromMin, spiesCntToMin, spiesCntFromMax, spiesCntToMax, timer, isSpySeeEachOther, isSpyParadox, isDiffLoc, isSecure);

        // Intent
        intent.putExtra("playersCnt", playersCnt);

        intent.putExtra("modeName", modeSpyCntName);
        intent.putExtra("spiesCnt", spiesCnt);
        intent.putExtra("spiesCntFrom", spiesCntFrom);
        intent.putExtra("spiesCntTo", spiesCntTo);

        intent.putExtra("timer", timer);

        intent.putExtra("isSpySeeEachOther", isSpySeeEachOther);
        intent.putExtra("isSpyParadox", isSpyParadox);
        intent.putExtra("isDiffLoc", isDiffLoc);
        intent.putExtra("isSecure", isSecure);

        String[] checkedLocName = checkedLocationsName.toArray(new String[0]);

        intent.putExtra("locationsName", checkedLocName);

        startActivity(intent);
    }
}
