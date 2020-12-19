package com.example.spy;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class ModesActivity extends FragmentActivity {

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

    private CheckBox cbIsSpySeeEachOther, cbIsSpyParadox, cbIsDiffLoc;
    private TextView tvIsSpySeeEachOtherDesc, tvIsSpyParadoxDesc, tvIsDiffLocDesc, tvSelectedLocations;
    private List<Location> locations;

    private String[] locationsName;
    private boolean[] checkedLocations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_game_layout);

        // Получить локации
        locations = JSONLocationParser.locationsFromJSON(this, locationsFile);

        locationsName = locations.stream()
                .map(Location::getLocationName)
                .toArray(String[]::new);

        checkedLocations = new boolean[locations.size()];
        isCheckAllLocationsDial = true;
        Arrays.fill(checkedLocations, isCheckAllLocationsDial);

        neutralBtnDial = getResources().getString(R.string.dialog_check_locations_check_all_btn);
        checkLocToast = getResources().getString(R.string.check_locations_toast);

        // NumberPicker
        npPlayersCnt = findViewById(R.id.npPlayersCnt);
        npSpiesCnt = findViewById(R.id.npSpiesCnt);
        npSpiesCntFrom = findViewById(R.id.npSpiesCntFrom);
        npSpiesCntTo = findViewById(R.id.npSpiesCntTo);
        npTimer = findViewById(R.id.npTimer);

        // CheckBox
        cbIsSpySeeEachOther = findViewById(R.id.cbIsSpySeeEachOther);
        cbIsSpyParadox = findViewById(R.id.cbIsSpyParadox);
        cbIsDiffLoc = findViewById(R.id.cbIsDiffLoc);

        // TextView
        tvIsSpySeeEachOtherDesc = findViewById(R.id.tvIsSpySeeEachOtherDesc);
        tvIsSpyParadoxDesc = findViewById(R.id.tvIsSpyParadoxDesc);
        tvIsDiffLocDesc = findViewById(R.id.tvIsDiffLocDesc);
        tvSelectedLocations = findViewById(R.id.tvSelectedLocations);

        setLocationsTextView();

        // NumberPicker инициализация
        npPlayersCnt.setMinValue(playersMin);
        npPlayersCnt.setMaxValue(playersMax);
        npPlayersCnt.setValue(playersMin);
        npPlayersCnt.setWrapSelectorWheel(false);

        npTimer.setMinValue(timerMin);
        npTimer.setMaxValue(timerMax);
        npTimer.setValue(timerMin);
        npTimer.setWrapSelectorWheel(false);


        npPlayersCnt.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Toast.makeText(ModesActivity.this, "" + newVal, Toast.LENGTH_LONG).show();
            }
        });

        // Кнопки выбора режима шпионов
        btnStandardSpyMode = findViewById(R.id.btnStandardSpyMode);
        btnRandSpyMode = findViewById(R.id.btnRandSpyMode);
        btnSlySpyMode = findViewById(R.id.btnSlySpyMode);

        selBtnDraw = getResources().getDrawable(R.drawable.spy_mode_selected_button);
        notSelBtnDraw = getResources().getDrawable(R.drawable.spy_mode_not_selected_button);

        colorTextItem = getResources().getColor(R.color.colorTextItem);
        colorTextItemDesc = getResources().getColor(R.color.colorTextItemDesc);
        colorTextItemNotActive = getResources().getColor(R.color.colorTextItemNotActive);
        colorTextDescNotActive = getResources().getColor(R.color.colorTextDescNotActive);

        // Фрагменты
        standardSpyModeFragment = new StandardSpyModeFragment();
        randSpyModeFragment = new RandSpyModeFragment();
        slySpyModeFragment = new SlySpyModeFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.flSpyMode, standardSpyModeFragment).commit();
        btnStandardSpyMode.setBackground(selBtnDraw);
        btnRandSpyMode.setBackground(notSelBtnDraw);
        btnSlySpyMode.setBackground(notSelBtnDraw);

        isCanStartGame = true;
        /*btnStandardSpyMode.setClickable(false);
        btnRandSpyMode.setClickable(true);
        btnSlySpyMode.setClickable(true);*/


        /*lvModes = findViewById(R.id.lvModes);
        lvModes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ModesActivity.this, "4564", Toast.LENGTH_LONG).show();
            }
        });

        List<Mode> modes = JSONLocationParser.modesFromJSONAssets(getApplicationContext(), modeFile);
        List<String> modesName = modes.stream()
                .map(Mode::getModeName)
                .collect(Collectors.toList());

        ArrayAdapter<String> aaModes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, modesName);
        lvModes.setAdapter(aaModes);*/
    }

    public void setLocationsTextView() {
        StringBuilder res = new StringBuilder();

        int posID = 1;
        String newLine = "";
        for (int i = 0; i < locations.size(); i++) {
            if (checkedLocations[i]) {
                res.append(newLine).append(posID).append(". ").append(locationsName[i]);
                newLine = "\n";
                posID++;
            }
        }

        if (posID < 3) {
            isCanStartGame = false;
            Toast.makeText(ModesActivity.this, checkLocToast, Toast.LENGTH_SHORT).show();
        }
        tvSelectedLocations.setText(res.toString());
    }

    public void changeSpyMode(View view) {
        if (view.getId() == btnStandardSpyMode.getId() && btnStandardSpyMode.getBackground() != selBtnDraw) {
            getSupportFragmentManager().beginTransaction().remove(standardSpyModeFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(randSpyModeFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(slySpyModeFragment).commit();

            btnRandSpyMode.setBackground(notSelBtnDraw);
            btnSlySpyMode.setBackground(notSelBtnDraw);

            getSupportFragmentManager().beginTransaction().add(R.id.flSpyMode, standardSpyModeFragment).commit();
            btnStandardSpyMode.setBackground(selBtnDraw);

            /*btnStandardSpyMode.setClickable(false);
            btnRandSpyMode.setClickable(true);
            btnSlySpyMode.setClickable(true);*/
        } else if (view.getId() == btnRandSpyMode.getId() && btnRandSpyMode.getBackground() != selBtnDraw) {
            getSupportFragmentManager().beginTransaction().remove(standardSpyModeFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(randSpyModeFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(slySpyModeFragment).commit();

            btnStandardSpyMode.setBackground(notSelBtnDraw);
            btnSlySpyMode.setBackground(notSelBtnDraw);

            getSupportFragmentManager().beginTransaction().add(R.id.flSpyMode, randSpyModeFragment).commit();
            btnRandSpyMode.setBackground(selBtnDraw);

            /*btnStandardSpyMode.setClickable(true);
            btnRandSpyMode.setClickable(false);
            btnSlySpyMode.setClickable(true);*/
        } else if (view.getId() == btnSlySpyMode.getId() && btnSlySpyMode.getBackground() != selBtnDraw) {
            getSupportFragmentManager().beginTransaction().remove(standardSpyModeFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(randSpyModeFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(slySpyModeFragment).commit();

            btnStandardSpyMode.setBackground(notSelBtnDraw);
            btnRandSpyMode.setBackground(notSelBtnDraw);

            getSupportFragmentManager().beginTransaction().add(R.id.flSpyMode, slySpyModeFragment).commit();
            btnSlySpyMode.setBackground(selBtnDraw);

            /*btnStandardSpyMode.setClickable(true);
            btnRandSpyMode.setClickable(true);
            btnSlySpyMode.setClickable(false);*/
        }
    }

    public void changeSpyVisibility(View view) {
        //tvIsSpyParadoxDesc, tvIsDiffLocDesc;
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

    public void startGame(View view) {
        if(!isCanStartGame) {
            Toast.makeText(ModesActivity.this, checkLocToast, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(ModesActivity.this, PlayerActivity.class);
        startActivity(intent);
    }
}
