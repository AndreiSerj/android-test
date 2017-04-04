package com.siarzhantau.andrei.locations.mvp;

import com.google.android.gms.maps.model.LatLng;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.siarzhantau.andrei.locations.model.Location;
import com.siarzhantau.andrei.locations.utils.LocationsUtil;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class LocationsMapPresenter extends MvpBasePresenter<LocationsMapView> {

    private Realm mRealm;
    private RealmChangeListener mRealmChangeListener;
    @Override
    public void attachView(LocationsMapView view) {
        super.attachView(view);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        mRealm.removeChangeListener(mRealmChangeListener); // Remove the listener.
        mRealm.close(); // Close the Realm instance.
    }

    public RealmResults<Location> getLocations() {
        RealmResults<Location> locations = mRealm.where(Location.class).findAllSorted("distance", Sort.ASCENDING);

        // set up a Realm change listener
        mRealmChangeListener = (RealmChangeListener<RealmResults<Location>>) results -> {
            // This is called anytime the Realm database changes on any thread.
            // Please note, change listeners only work on Looper threads.
            // For non-looper threads, you manually have to use Realm.waitForChange() instead.
            if (isViewAttached() && getView() != null) { // Update the UI
                getView().showContent(results);
            }
        };
        // Tell Realm to notify our listener when the customers results
        // have changed (items added, removed, updated, anything of the sort).
        locations.addChangeListener(mRealmChangeListener);
        return locations;
    }

    public void createNewLocation(LatLng point, String name, String id) {
        mRealm.executeTransactionAsync(realm -> {
            Location location = realm.createObject(Location.class, id);
            location.name = name;
            location.lat = point.latitude;
            location.lng = point.longitude;
            location.distance = LocationsUtil.calculateDistance(
                    location.name, location.lat, location.lng);

        });
    }

}
