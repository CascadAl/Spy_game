package com.example.spy;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("LocationID")
    private int id;

    @SerializedName("LocationName")
    private String locationName;

    @SerializedName("IsChecked")
    private boolean isChecked;

    public Location(int id, String locationName, boolean isChecked) {
        this.id = id;
        this.locationName = locationName;
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", locationName='" + locationName + '\'' +
                ", IsChecked='" + isChecked + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
