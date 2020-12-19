package com.example.spy;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JSONLocationParser {
    private static final Gson gson;
    private static final String TAG = "JSONLocationParser";
    private static final String rootPath = "/data/data/";

    static {
        gson = new Gson();
    }

    // Получение локаций с json
    public static ArrayList<Location> locationsFromJSONAssets(Context context, String filePath) {
        byte[] buffer = getJSONString(context, filePath);

        String jsonStr = null;

        try {
            jsonStr = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(Arrays.asList(gson.fromJson(jsonStr, Location[].class)));
    }

    private static byte[] getJSONString(Context context, String filePath) {
        InputStream is = null;
        byte[] buffer = null;

        try {
            is = context.getAssets().open(filePath);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return buffer;
    }

    // Запись локаций в json
    public static void locationsToJSON(Context context, List<Location> locations, String fileName) {
        String gsonLocationsString = gson.toJson(locations);
        FileWriter file = null;
        try {
            file = new FileWriter(rootPath + context.getPackageName() + "/" + fileName);
            file.write(gsonLocationsString);
        } catch (IOException e) {
            Log.e(TAG, "locationsToJSON");
            e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.flush();
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Получение локаций с json
    public static ArrayList<Location> locationsFromJSON(Context context, String fileName) {
        String jsonLocationsStr = null;
        FileInputStream is = null;
        try {
            File f = new File(rootPath + context.getPackageName() + "/" + fileName);
            is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            jsonLocationsStr = new String(buffer, "UTF-8");
        } catch (IOException e) {
            Log.e(TAG, "locationsFromJSON");
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return new ArrayList<>(Arrays.asList(gson.fromJson(jsonLocationsStr, Location[].class)));
    }
}