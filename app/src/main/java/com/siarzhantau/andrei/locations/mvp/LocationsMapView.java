package com.siarzhantau.andrei.locations.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.siarzhantau.andrei.locations.model.Location;

import io.realm.RealmResults;

public interface LocationsMapView extends MvpView {
    void showContent(RealmResults<Location> locations);
}
