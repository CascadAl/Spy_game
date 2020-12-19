package com.example.spy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.common.primitives.Booleans;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MainMenuActivity extends Activity {
    private String locationsFile = "locations.json";

    private ListView lvGameSettings;
    private String playersCntStr, spiesCntStr, spiesCntFromStr, spiesCntToStr, timerStr, isSpySeeEachOtherStr, isSpyParadoxStr, isDiffLocStr, isSecureStr, locationsCntStr;

    private String modeSpyCntName;
    private int playersCnt, spiesCnt, spiesCntFrom, spiesCntTo, timer;
    private boolean isSpySeeEachOther, isSpyParadox, isDiffLoc, isSecure;
    private List<String> checkedLocationsName;
    private boolean[] checkedLocations;
    private List<Location> locations;
    private String[] checkedLocName;

    private int settingsListSize;

    private List<String> itemGameSettings;
    private ArrayAdapter<String> aaGameSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu_layout);

        lvGameSettings = findViewById(R.id.lvGameSettings);
        lvGameSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainMenuActivity.this, "Click", Toast.LENGTH_LONG).show();
            }
        });

        getRes();

        // Заливка данных с файла assets в файл data/data
        /*String JSONFilePath = "locations.json";
        List<Location> locFromAssets = JSONLocationParser.locationsFromJSONAssets(getApplicationContext(), JSONFilePath);
        //List<Location> locFromAssets = JSONLocationParser.locationsFromJSON(getApplicationContext(), JSONFilePath);
        JSONLocationParser.locationsToJSON(getApplicationContext(), locFromAssets, JSONFilePath);*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Получить локации
        locations = JSONLocationParser.locationsFromJSON(this, locationsFile);

        getSettingsGame();
        checkedLocName = getCheckedLocName();
        itemGameSettings = getSettingsList();
        aaGameSettings = new ArrayAdapter<>(this, R.layout.settings_main_menu_list_item, itemGameSettings);
        lvGameSettings.setAdapter(aaGameSettings);
    }

    public void getRes() {
        playersCntStr = getResources().getString(R.string.playersCnt);
        spiesCntStr = getResources().getString(R.string.spiesCnt);
        spiesCntFromStr = getResources().getString(R.string.spiesCntFrom);
        spiesCntToStr = getResources().getString(R.string.spiesCntTo);
        timerStr = getResources().getString(R.string.timer);
        isSpySeeEachOtherStr = getResources().getString(R.string.isSpySeeEachOther);
        isSpyParadoxStr = getResources().getString(R.string.isSpyParadox);
        isDiffLocStr = getResources().getString(R.string.isDiffLoc);
        isSecureStr = getResources().getString(R.string.isSecure);
         locationsCntStr = getString(R.string.locations_cnt_str);
    }

     public void getSettingsGame() {
         SettingsGame.getGameSettings(this, locations.size());

         playersCnt = SettingsGame.getPlayersCnt();

         modeSpyCntName = SettingsGame.getModeSpyCntName();
         spiesCnt = SettingsGame.getSpiesCnt();
         spiesCntFrom = SettingsGame.getSpiesCntFrom();
         spiesCntTo = SettingsGame.getSpiesCntTo();

         timer = SettingsGame.getTimer();

         isSpySeeEachOther = SettingsGame.isSpySeeEachOther();
         isSpyParadox = SettingsGame.isSpyParadox();
         isDiffLoc = SettingsGame.isDiffLoc();
         isSecure = SettingsGame.isSecure();

         checkedLocations = LocationsUtils.getCheckedLocations(locations);

         settingsListSize = SettingsGame.getSize(this);
     }

    public List<String> getSettingsList() {
        List<String> list = new ArrayList<>(settingsListSize);

        list.add(playersCntStr + ": " + playersCnt);

        if(modeSpyCntName.equals("standard")) {
            list.add(spiesCntStr + ": " + spiesCnt);
        } else if (modeSpyCntName.equals("rand")) {
            list.add(spiesCntFromStr + ": " + spiesCntFrom);
            list.add(spiesCntToStr + ": " + spiesCntTo);
        } else {
            list.add("В игре будет предоставлена возможность выбрать себя шпионом или увидеть локацию.");
        }

        list.add(timerStr + ": " + timer);

        list.add(isSpySeeEachOtherStr + ": " + isSpySeeEachOther);
        list.add(isSpyParadoxStr + ": " + isSpyParadox);
        list.add(isDiffLocStr + ": " + isDiffLoc);
        list.add(isSecureStr + ": " + isSecure);

        list.add(locationsCntStr + ": " + checkedLocName.length);

        return list;
    }

    private String[] getCheckedLocName() {
        checkedLocationsName = new ArrayList<>();

        for (int i = 0; i < checkedLocations.length; i++) {
            if (checkedLocations[i]) {
                checkedLocationsName.add(locations.get(i).getLocationName());
            }
        }

        return checkedLocationsName.toArray(new String[0]);
    }

    public void newGameClick(View view) {
        Intent newGameIntent = new Intent(MainMenuActivity.this, PlayerActivity.class);

        newGameIntent.putExtra("playersCnt", playersCnt);

        newGameIntent.putExtra("modeName", modeSpyCntName);
        newGameIntent.putExtra("spiesCnt", spiesCnt);
        newGameIntent.putExtra("spiesCntFrom", spiesCntFrom);
        newGameIntent.putExtra("spiesCntTo", spiesCntTo);

        newGameIntent.putExtra("timer", timer);

        newGameIntent.putExtra("isSpySeeEachOther", isSpySeeEachOther);
        newGameIntent.putExtra("isSpyParadox", isSpyParadox);
        newGameIntent.putExtra("isDiffLoc", isDiffLoc);
        newGameIntent.putExtra("isSecure", isSecure);

        newGameIntent.putExtra("locationsName", checkedLocName);

        startActivity(newGameIntent);
    }

    public void settingsClick(View view) {
        Intent settingsIntent = new Intent(MainMenuActivity.this, SettingsGameActivity.class);
        startActivity(settingsIntent);
    }

    public void locationClick(View view) {
        Intent intent = new Intent(MainMenuActivity.this, LocationViewActivity.class);
        startActivity(intent);
    }


    public void specificClick(View view) {
        Intent intent = new Intent(MainMenuActivity.this, SpecificActivity.class);
        startActivity(intent);
    }


}
