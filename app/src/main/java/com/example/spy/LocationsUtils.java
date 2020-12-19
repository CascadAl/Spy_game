package com.example.spy;

import com.google.common.primitives.Booleans;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LocationsUtils {

    public static String[] getLocationsName(List<Location> locations) {
        return locations.stream()
                .map(Location::getLocationName)
                .toArray(String[]::new);
    }

    public static boolean[] getCheckedLocations(List<Location> locations) {
        return Booleans.toArray(locations.stream()
                .map(Location::isChecked)
                .collect(Collectors.toList()));
    }

    public static int getMaxLocationID(List<Location> locations) {
        return locations.stream()
                .max(Comparator.comparing(Location::getId))
                .get()
                .getId();
    }

}
