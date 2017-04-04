package com.siarzhantau.andrei.locations;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.siarzhantau.andrei.locations.model.Location;
import com.siarzhantau.andrei.locations.model.Locations;
import com.siarzhantau.andrei.locations.utils.LocationsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;


import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mPrefs = getSharedPreferences("com.siarzhantau.andrei.locations", MODE_PRIVATE);

        if (mPrefs.getBoolean("firstRun", true)) {
            mPrefs.edit().putBoolean("firstRun", false).commit();

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    populateRealm();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    startLocationsActivity();
                    finish();
                }
            }.execute();
        } else {
            startLocationsActivity();
            finish();
        }
    }

    private void populateRealm() {
        Locations locations = null;
        try {
            locations = new Gson().fromJson(new BufferedReader(
                    new InputStreamReader(getAssets().open("locations.json"), "UTF-8")), Locations.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (locations != null) {
            Realm realm = Realm.getDefaultInstance();
            for (Location location : locations.locations) {
                realm.executeTransaction(r -> {
                    location.id = UUID.randomUUID().toString();
                    location.distance = LocationsUtil.calculateDistance(
                            location.name, location.lat, location.lng);
                    r.copyToRealm(location);
                });
            }
        }
    }

    private void startLocationsActivity() {
        startActivity(new Intent(this, LocationsActivity.class));
    }
}
