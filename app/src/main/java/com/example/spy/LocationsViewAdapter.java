 package com.example.spy;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LocationsViewAdapter extends ArrayAdapter<Location> {
    private final String JSONFilePath = "locations.json";

    private Context context;
    private ArrayList<Location> locations;

    public LocationsViewAdapter(@NonNull Context context, ArrayList<Location> locations) {
        super(context, 0, locations);

        this.context = context;
        this.locations = locations;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.location_adapter_layout, parent, false);
        }

        String locationName = locations.get(position).getLocationName();
        TextView tvLocation = view.findViewById(R.id.tvLocationName);
        tvLocation.setText(locationName);

        Button btnDeleteLocation = view.findViewById(R.id.btnDeleteLocation);
        btnDeleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location delLocation = locations.get(position);

                locations.remove(position);
                JSONLocationParser.locationsToJSON(context, locations, JSONFilePath);

                LocationsViewAdapter.this.notifyDataSetChanged();

                View layout = LayoutInflater.from(context).inflate(R.layout.my_toast,
                        (ViewGroup) view.findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("Удалена локация '" + locationName + "'");

                Toast toast = new Toast(context);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 1000);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                Button button = layout.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (toast != null || toast.getView().getWindowVisibility() == View.VISIBLE) {
                            toast.cancel();
                        }

                        locations.add(position, delLocation);
                        JSONLocationParser.locationsToJSON(context, locations, JSONFilePath);

                        LocationsViewAdapter.this.notifyDataSetChanged();
                    }
                });
            }
        });

        return view;
    }
}
