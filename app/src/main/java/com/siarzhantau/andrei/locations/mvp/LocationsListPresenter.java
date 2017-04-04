package com.siarzhantau.andrei.locations.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.siarzhantau.andrei.locations.model.Location;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class LocationsListPresenter extends MvpBasePresenter<LocationsListView> {

    private Realm mRealm;
    private RealmChangeListener mRealmChangeListener;
    private static final String SORT_BY_DISTANCE = "distance";

    @Override
    public void attachView(LocationsListView view) {
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
        RealmResults<Location> locations = mRealm.where(Location.class).findAllSorted(SORT_BY_DISTANCE, Sort.ASCENDING);

        // set up a Realm change listener
        mRealmChangeListener = (RealmChangeListener<RealmResults<Location>>) results -> {
            // This is called anytime the Realm database changes on any thread.
            // Please note, change listeners only work on Looper threads.
            // For non-looper threads, you manually have to use Realm.waitForChange() instead.
            if (isViewAttached() && getView() != null) { // Update the UI
                getView().showContent();
            }
        };
        // Tell Realm to notify our listener when the customers results
        // have changed (items added, removed, updated, anything of the sort).
        locations.addChangeListener(mRealmChangeListener);
        return locations;
    }

    public void loadLocations(final boolean pullToRefresh) {
        if (isViewAttached() && getView() != null) {
            getView().showLoading(pullToRefresh);
        }

        RealmResults<Location> locations = mRealm.where(Location.class).findAllSorted(SORT_BY_DISTANCE, Sort.ASCENDING);

        if (isViewAttached() && getView() != null) { // Update the UI
            getView().setData(locations);
            getView().showContent();
        }
    }

}
