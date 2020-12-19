package com.example.spy;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class SpyInitial {
    private final int playersCnt, spiesCnt;
    private final long timer;
    private final boolean isCanSpySeeEachOther, isKnowSpy, isDiffLoc, isHaveDefense;
    private String[] locations, playersLocation;

    private final String spyName, playerName, divider1, divider2;

    private int[] spyIndex;


    // standard
    public SpyInitial(int playersCnt, int spiesCnt, long timer, boolean isCanSpySeeEachOther, boolean isKnowSpy, boolean isDiffLoc, boolean isHaveDefense, String[] locations,
                      String spyName, String playerName, String divider1, String divider2) {
        this.playersCnt = playersCnt;
        this.spiesCnt = spiesCnt;
        this.timer = timer;
        this.isCanSpySeeEachOther = isCanSpySeeEachOther;
        this.isKnowSpy = isKnowSpy;
        this.isDiffLoc = isDiffLoc;
        this.isHaveDefense = isHaveDefense;
        this.locations = locations;
        this.spyName = spyName;
        this.playerName = playerName;
        this.divider1 = divider1;
        this.divider2 = divider2;
    }

    // rand
    public SpyInitial(int playersCnt, int spiesCntFrom, int spiesCntTo, long timer, boolean isCanSpySeeEachOther, boolean isKnowSpy, boolean isDiffLoc, boolean isHaveDefense, String[] locations,
                      String spyName, String playerName, String divider1, String divider2) {
        this.playersCnt = playersCnt;

        Random random = new Random();
        this.spiesCnt = random.nextInt(spiesCntTo - spiesCntFrom + 1) + spiesCntFrom;

        this.timer = timer;
        this.isCanSpySeeEachOther = isCanSpySeeEachOther;
        this.isKnowSpy = isKnowSpy;
        this.isDiffLoc = isDiffLoc;
        this.isHaveDefense = isHaveDefense;
        this.locations = locations;
        this.spyName = spyName;
        this.playerName = playerName;
        this.divider1 = divider1;
        this.divider2 = divider2;
    }

    // sly
    public SpyInitial(int playersCnt, long timer, boolean isCanSpySeeEachOther, boolean isKnowSpy, boolean isDiffLoc, boolean isHaveDefense, String[] locations,
                      String spyName, String playerName, String divider1, String divider2) {
        this.playersCnt = playersCnt;
        this.spiesCnt = -1;
        this.timer = timer;
        this.isCanSpySeeEachOther = isCanSpySeeEachOther;
        this.isKnowSpy = isKnowSpy;
        this.isDiffLoc = isDiffLoc;
        this.isHaveDefense = isHaveDefense;
        this.locations = locations;
        this.spyName = spyName;
        this.playerName = playerName;
        this.divider1 = divider1;
        this.divider2 = divider2;
    }

    // Получить локацию для текущей игры
    private String[] getLocations() {
        int locCnt = 1;
        Random rand = new Random();
        int startPoint = 0;

        if (isKnowSpy && isDiffLoc) {
            locCnt = spiesCnt + 1;
        } else if (isKnowSpy) {
            locCnt = 2;
        }

        ArrayList<Integer> locs = new ArrayList<>(locations.length);
        for (int i = 0; i < locations.length; i++) {
            locs.add(i);
        }

        int index = 0;
        String[] resLoc = new String[locCnt];
        for (int i = 0; i < locCnt; i++) {
            index = rand.nextInt((locs.size() - startPoint)) + startPoint;
            resLoc[i] = locations[locs.get(index)];
            locs.remove(index);
        }

        return resLoc;
    }

    // Получить список индексов шпионов
    private int[] getSpyIndex() {
        int startPoint = 0;
        int[] spyArr = new int[spiesCnt];
        ArrayList<Integer> arr = new ArrayList(playersCnt);
        Random rand = new Random();

        for (int i = 0; i < playersCnt; i++) {
            arr.add(i);
        }

        int spyIndex = 0;

        for (int i = 0; i < spyArr.length; i++) {
            spyIndex = rand.nextInt(playersCnt - i - startPoint) + startPoint;
            spyArr[i] = arr.get(spyIndex);
            arr.remove(spyIndex);
        }

        Arrays.sort(spyArr);

        return spyArr;
    }

    // Получить список локаций со шпионами
    public String[] getPlayersLocation() {
        String[] locations = getLocations();
        playersLocation = new String[playersCnt];

        Arrays.fill(playersLocation, locations[0]);

        if(spiesCnt != -1) {
            spyIndex = getSpyIndex();

            for (int index : spyIndex) {
                playersLocation[index] = spyName;
            }

            if (isKnowSpy && isDiffLoc) {
                for (int i = 0; i < spyIndex.length; i++) {
                    playersLocation[spyIndex[i]] = locations[i + 1];
                }
            } else if (isKnowSpy) {
                for (int index : spyIndex) {
                    playersLocation[index] = locations[1];
                }
            } else {
                if (isCanSpySeeEachOther) {
                    String[] spyInfo = getSpyInfo();
                    for (int i = 0; i < spyIndex.length; i++) {
                        playersLocation[spyIndex[i]] = spyName + spyInfo[i];
                    }
                } /*else {
                for (int index : spyIndex) {
                    playersLocation[index] = spyName;
                }
            }*/
            }
        }

        return playersLocation;
    }

    // Получить описание, которое будет использоваться для шпионов
    private String[] getSpyInfo() {
        String dividerV1 = playerName + " ";
        String dividerV2 = " " + divider2 + " ";

        String[] resArr = new String[spiesCnt];
        int index = 0;

        int[] spyNumber = new int[spyIndex.length];
        for(int i = 0; i < spyIndex.length; i++) {
            spyNumber[i] = spyIndex[i] + 1;
        }

        for (int item : spyNumber) {
            resArr[index] = divider1 + dividerV1 +
                    Arrays.stream(spyNumber)
                            .filter(x -> x != item)
                            .mapToObj(String::valueOf)
                            .collect(Collectors.joining(dividerV2 + dividerV1));
            index++;
        }

        return resArr;
    }


    public boolean isHaveDefense() {
        return isHaveDefense;
    }

    public long getTimer() {
        return timer;
    }
}
