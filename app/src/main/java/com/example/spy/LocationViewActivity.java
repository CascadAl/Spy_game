package com.example.spy;

import android.app.Activity;

import android.os.Bundle;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;


public class LocationViewActivity extends Activity {
    private final String JSONFilePath = "locations.json";
    private ArrayList<Location> locations;

    private ListView mobile_list;
    private EditText etAddLocation;

    private InputMethodManager inputMethodManager;

    private LocationsViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.location_list_layout);

        locations = new ArrayList(JSONLocationParser.locationsFromJSON(getApplicationContext(), JSONFilePath));

        etAddLocation = findViewById(R.id.etAddLocation);

        inputMethodManager = (InputMethodManager) LocationViewActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        adapter = new LocationsViewAdapter(this, locations);
        mobile_list = findViewById(R.id.mobile_list);
        mobile_list.setAdapter(adapter);

        // Сохранение локации
        etAddLocation.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_ENTER && !etAddLocation.getText().toString().equals("")) {
                        String newLocation = etAddLocation.getText().toString();
                        int maxID = LocationsUtils.getMaxLocationID(locations);

                        locations.add(new Location(maxID + 1, newLocation, true));
                        JSONLocationParser.locationsToJSON(getApplicationContext(), locations, JSONFilePath);

                        inputMethodManager.hideSoftInputFromWindow(
                                LocationViewActivity.this.getCurrentFocus().getWindowToken(), 0);

                        adapter.notifyDataSetChanged();
                        etAddLocation.setText("");

                        Toast.makeText(LocationViewActivity.this, "Добавлена локация '" + newLocation + "'", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                return false;
            }
        });
    }
}

