package com.siarzhantau.andrei.locations.utils;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;

public final class LocationsUtil {

    private static final String TAG = LocationsUtil.class.getSimpleName();

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

    public static String loadLocationsFromAssets(Context context) {
        String json = null;
        InputStream is = null;

        try {
            is = context.getAssets().open("locations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);

            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            Log.e(TAG, "Json loading error: " + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json;
    }

}
