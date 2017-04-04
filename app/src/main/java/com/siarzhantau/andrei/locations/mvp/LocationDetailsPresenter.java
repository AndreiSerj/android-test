package com.siarzhantau.andrei.locations.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.siarzhantau.andrei.locations.model.Location;

import io.realm.Realm;

public class LocationDetailsPresenter extends MvpBasePresenter<LocationDetailsView> {

    private Realm mRealm;

    @Override
    public void attachView(LocationDetailsView view) {
        super.attachView(view);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        mRealm.close(); // Close the Realm instance.
    }

    public Location getLocationById(String locationId) {
        return mRealm.where(Location.class).equalTo("id", locationId).findFirst();
    }

    public void updateLocationDescription(String locationId, String description) {
        mRealm.executeTransaction(realm -> {
            Location location = realm.where(Location.class).equalTo("id", locationId).findFirst();
            location.description = description;
        });
    }

}
