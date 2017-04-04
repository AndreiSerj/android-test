package com.siarzhantau.andrei.locations.utils;

import android.location.Location;

public class LocationsUtil {
    private static Location sLocationSydney = new Location("Sydney");

    static {
        sLocationSydney.setLatitude(-33.88);
        sLocationSydney.setLongitude(151.21);
    }

    private LocationsUtil() {}

    public static float calculateDistance(String name, double lat, double lng) {
        final Location locationCurrent = new Location(name);
        locationCurrent.setLatitude(lat);
        locationCurrent.setLongitude(lng);

        return sLocationSydney.distanceTo(locationCurrent);
    }

}
