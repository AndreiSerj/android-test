package com.siarzhantau.andrei.locations.mvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.siarzhantau.andrei.locations.model.Location;
import com.siarzhantau.andrei.locations.model.Locations;
import com.siarzhantau.andrei.locations.utils.LocationsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import io.realm.Realm;

public class SplashPresenter extends MvpBasePresenter<SplashView> {

    @SuppressLint("ApplySharedPref")
    public void checkFirstRun(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("com.siarzhantau.andrei.locations", Context.MODE_PRIVATE);

        if (prefs.getBoolean("firstRun", true)) {
            prefs.edit().putBoolean("firstRun", false).commit();

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    populateRealm(context);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    if (isViewAttached() && getView() != null) {
                        getView().goToNextScreen();
                    }
                }
            }.execute();
        } else {
            if (isViewAttached() && getView() != null) {
                getView().goToNextScreen();
            }
        }
    }

    private void populateRealm(Context context) {
        Locations locations = null;
        try {
            locations = new Gson().fromJson(new BufferedReader(
                    new InputStreamReader(context.getAssets().open("locations.json"), "UTF-8")), Locations.class);
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
                    location.custom = false;

                    r.copyToRealm(location);
                });
            }
        }
    }
}
