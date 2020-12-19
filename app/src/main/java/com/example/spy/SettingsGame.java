package com.example.spy;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static android.content.Context.MODE_PRIVATE;

public class SettingsGame {
    private static final String GAME_SETTINGS = "spy_game_settings";
    private static final String PLAYERS_CNT = "players_cnt";
    private static final String MODE_SPY_CNT_NAME = "mode_spy_cnt_name";
    private static final String SPIES_CNT = "spies_cnt";
    private static final String SPIES_CNT_FROM = "spies_cnt_from";
    private static final String SPIES_CNT_FROM_MIN = "spies_cnt_from_min";
    private static final String SPIES_CNT_FROM_MAX = "spies_cnt_from_max";
    private static final String SPIES_CNT_TO = "spies_cnt_to";
    private static final String SPIES_CNT_TO_MIN = "spies_cnt_to_min";
    private static final String SPIES_CNT_TO_MAX = "spies_cnt_to_max";
    private static final String TIMER = "timer";
    private static final String IS_SPY_SEE_EACH_OTHER = "is_spy_see_each_other";
    private static final String IS_SPY_PARADOX = "is_spy_paradox";
    private static final String IS_DIFF_LOC = "is_diff_loc";
    private static final String IS_SECURE = "is_secure";
    /*private static final String CHECKED_LOCATIONS = "checked_locations";
    private static final String CHECKED_LOCATIONS_SIZE = "checked_locations_size";*/

    private static SharedPreferences spSettings;

    //private static boolean[] checkedLocations;
    private static int playersCnt;
    private static String modeSpyCntName = "";
    private static int spiesCnt, spiesCntFrom, spiesCntTo, spiesCntFromMin, spiesCntToMin, spiesCntFromMax, spiesCntToMax;
    private static int timer;
    private static boolean isSpySeeEachOther, isSpyParadox, isDiffLoc, isSecure;


    static {
        playersCnt = 3;

        modeSpyCntName = "standard";
        spiesCnt = 1;
        spiesCntFrom = 1;
        spiesCntTo = 1;
        spiesCntFromMin = 1;
        spiesCntToMin = 1;
        spiesCntFromMax = 1;
        spiesCntToMax = 1;

        timer = 1;

        isSpySeeEachOther = false;
        isSpyParadox = false;
        isDiffLoc = false;
        isSecure = false;
    }

    // Загрузка настроек
    public static void getGameSettings(Context context, int arrSize) {
        /*checkedLocations = new boolean[arrSize];
        Arrays.fill(checkedLocations, true);*/

        spSettings = context.getSharedPreferences(GAME_SETTINGS, MODE_PRIVATE);

        if(spSettings.contains(PLAYERS_CNT)) {
            playersCnt = spSettings.getInt(PLAYERS_CNT, 1);

            modeSpyCntName = spSettings.getString(MODE_SPY_CNT_NAME, "standard");
            spiesCnt = spSettings.getInt(SPIES_CNT, 1);
            spiesCntFrom = spSettings.getInt(SPIES_CNT_FROM, 1);
            spiesCntTo = spSettings.getInt(SPIES_CNT_TO, 1);
            spiesCntFromMin = spSettings.getInt(SPIES_CNT_FROM_MIN, 1);
            spiesCntToMin = spSettings.getInt(SPIES_CNT_TO_MIN, 1);
            spiesCntFromMax = spSettings.getInt(SPIES_CNT_FROM_MAX, 1);
            spiesCntToMax = spSettings.getInt(SPIES_CNT_TO_MAX, 1);

            timer = spSettings.getInt(TIMER, 1);

            isSpySeeEachOther = spSettings.getBoolean(IS_SPY_SEE_EACH_OTHER, false);
            isSpyParadox = spSettings.getBoolean(IS_SPY_PARADOX, false);
            isDiffLoc = spSettings.getBoolean(IS_DIFF_LOC, false);
            isSecure = spSettings.getBoolean(IS_SECURE, false);

            /*int size = spSettings.getInt(CHECKED_LOCATIONS_SIZE, arrSize);
            checkedLocations = new boolean[size];
            for (int i = 0; i < size; i++) {
                checkedLocations[i] = spSettings.getBoolean(CHECKED_LOCATIONS + "_" + i, false);
            }*/
        } else {
            setGameSettings(context, playersCnt, modeSpyCntName, spiesCnt, spiesCntFrom, spiesCntTo, spiesCntFromMin, spiesCntToMin, spiesCntFromMax, spiesCntToMax, timer, isSpySeeEachOther, isSpyParadox, isDiffLoc, isSecure/*, checkedLocations*/);
        }
    }

    public static int getSize(Context context) {
        spSettings = context.getSharedPreferences(GAME_SETTINGS, MODE_PRIVATE);

        return spSettings.getAll().size();
    }

    // Сохранение настроек
    public static void setGameSettings(Context context, int playersCnt, String modeSpyCntName, int spiesCnt, int spiesCntFrom, int spiesCntTo, int spiesCntFromMin, int spiesCntToMin, int spiesCntFromMax, int spiesCntToMax,
                                       int timer, boolean isSpySeeEachOther, boolean isSpyParadox, boolean isDiffLoc, boolean isSecure/*, boolean[] checkedLocations*/) {
        /*checkedLocations = new boolean[arrSize];
        Arrays.fill(checkedLocations, true);*/

        spSettings = context.getSharedPreferences(GAME_SETTINGS, MODE_PRIVATE);

        SharedPreferences.Editor spEditor = spSettings.edit();

        spEditor.putInt(PLAYERS_CNT, playersCnt);

        spEditor.putString(MODE_SPY_CNT_NAME, modeSpyCntName);
        spEditor.putInt(SPIES_CNT, spiesCnt);
        spEditor.putInt(SPIES_CNT_FROM, spiesCntFrom);
        spEditor.putInt(SPIES_CNT_TO, spiesCntTo);
        spEditor.putInt(SPIES_CNT_FROM_MIN, spiesCntFromMin);
        spEditor.putInt(SPIES_CNT_TO_MIN, spiesCntToMin);
        spEditor.putInt(SPIES_CNT_FROM_MAX, spiesCntFromMax);
        spEditor.putInt(SPIES_CNT_TO_MAX, spiesCntToMax);

        spEditor.putInt(TIMER, timer);

        spEditor.putBoolean(IS_SPY_SEE_EACH_OTHER, isSpySeeEachOther);
        spEditor.putBoolean(IS_SPY_PARADOX, isSpyParadox);
        spEditor.putBoolean(IS_DIFF_LOC, isDiffLoc);
        spEditor.putBoolean(IS_SECURE, isSecure);

        /*spEditor.putInt(CHECKED_LOCATIONS_SIZE, checkedLocations.length);
        for (int i = 0; i < checkedLocations.length; i++) {
            spEditor.putBoolean(CHECKED_LOCATIONS + "_" + i, checkedLocations[i]);
        }*/

        spEditor.apply();
    }

    /*public static void addCheckedLocation() {
        int locSize = checkedLocations.length;

        SharedPreferences.Editor spEditor = spSettings.edit();
        spEditor.putBoolean(CHECKED_LOCATIONS + "_" + locSize, true);
        spEditor.apply();

        checkedLocations = addCheckedElement();
    }

    public static void addCheckedLocation(int index, boolean isChecked) {
        SharedPreferences.Editor spEditor = spSettings.edit();
        spEditor.putBoolean(CHECKED_LOCATIONS + "_" + index, isChecked);
        spEditor.apply();

        checkedLocations = addCheckedElement(index, isChecked);
    }

    public static void removeCheckedLocation(int id) {
        SharedPreferences.Editor spEditor = spSettings.edit();
        spEditor.remove(CHECKED_LOCATIONS + "_" + id);
        spEditor.apply();

        checkedLocations = removeCheckedElement(id);
    }

    private static boolean[] addCheckedElement() {
        boolean[] tmpCheckedLocation = new boolean[checkedLocations.length + 1];

        for (int i = 0; i < checkedLocations.length; i++) {
            tmpCheckedLocation[i] = checkedLocations[i];
        }
        tmpCheckedLocation[checkedLocations.length] = true;

        return tmpCheckedLocation;
    }

    private static boolean[] addCheckedElement(int index, boolean isChecked) {
        boolean[] tmpCheckedLocation = new boolean[checkedLocations.length + 1];

        for (int i = 0, k = 0; i < tmpCheckedLocation.length; i++) {
            if (i == index) {
                tmpCheckedLocation[i] = isChecked;
                continue;
            }
            tmpCheckedLocation[i] = checkedLocations[k++];
        }

        return tmpCheckedLocation;
    }

    private static boolean[] removeCheckedElement(int id) {
        boolean[] tmpCheckedLocation = new boolean[checkedLocations.length - 1];

        for (int i = 0, k = 0; i < checkedLocations.length; i++) {
            if (i == id) {
                continue;
            }
            tmpCheckedLocation[k++] = checkedLocations[i];
        }

        return tmpCheckedLocation;
    }

    public static boolean[] getCheckedLocations() {
        return checkedLocations;
    }

    public static boolean getCheckedLocations(int index) {
        return checkedLocations[index];
    }*/

    public static int getPlayersCnt() {
        return playersCnt;
    }

    public static String getModeSpyCntName() {
        return modeSpyCntName;
    }

    public static int getSpiesCnt() {
        return spiesCnt;
    }

    public static int getSpiesCntFrom() {
        return spiesCntFrom;
    }

    public static int getSpiesCntTo() {
        return spiesCntTo;
    }

    public static int getSpiesCntFromMin() {
        return spiesCntFromMin;
    }

    public static int getSpiesCntToMin() {
        return spiesCntToMin;
    }

    public static int getSpiesCntFromMax() {
        return spiesCntFromMax;
    }

    public static int getSpiesCntToMax() {
        return spiesCntToMax;
    }

    public static int getTimer() {
        return timer;
    }

    public static boolean isSpySeeEachOther() {
        return isSpySeeEachOther;
    }

    public static boolean isSpyParadox() {
        return isSpyParadox;
    }

    public static boolean isDiffLoc() {
        return isDiffLoc;
    }

    public static boolean isSecure() {
        return isSecure;
    }
}
