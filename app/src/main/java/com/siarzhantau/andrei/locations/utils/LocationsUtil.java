package com.siarzhantau.andrei.locations.utils;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

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

    public static CameraUpdate getCameraUpdate() {
        LatLng sydney = new LatLng(-33.88, 151.21);
        return CameraUpdateFactory.newLatLngZoom(sydney, 11);
    }
}
