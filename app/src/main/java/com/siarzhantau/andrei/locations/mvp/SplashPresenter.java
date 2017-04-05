package com.siarzhantau.andrei.locations.mvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.siarzhantau.andrei.locations.model.Location;
import com.siarzhantau.andrei.locations.utils.LocationsParser;
import com.siarzhantau.andrei.locations.utils.LocationsUtil;

import java.util.List;

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
        final String jsonLocations = LocationsUtil.loadLocationsFromAssets(context);

        if (jsonLocations != null) {
            List<Location> locations = LocationsParser.parse(jsonLocations);
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(r -> {
                r.copyToRealm(locations);
            });
            realm.close();
        }
    }
}
