package com.siarzhantau.andrei.locations.utils;

import android.util.Log;

import com.siarzhantau.andrei.locations.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class LocationsParser {

    private static final String TAG = LocationsParser.class.getSimpleName();

    private LocationsParser() {}

    public static List<Location> parse(String jsonStr) {
        List<Location> locations = new ArrayList<>();

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray array = jsonObj.getJSONArray("locations");

                for (int i = 0, length = array.length(); i < length; i++) {
                    JSONObject l = array.getJSONObject(i);

                    String name = l.getString("name");
                    double lat = l.getDouble("lat");
                    double lng = l.getDouble("lng");

                    locations.add(new Location(name, lat, lng));
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        return locations;
    }
}
