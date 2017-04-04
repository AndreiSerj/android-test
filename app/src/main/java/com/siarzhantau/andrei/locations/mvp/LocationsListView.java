package com.siarzhantau.andrei.locations.mvp;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.siarzhantau.andrei.locations.model.Location;

import java.util.List;

import io.realm.RealmResults;

public interface LocationsListView extends MvpLceView<RealmResults<Location>> {
    // MvpLceView already defines LCE methods:
    //
    // void showLoading(boolean pullToRefresh)
    // void showError(Throwable t, boolean pullToRefresh)
    // void setData(List<Post> data)
    // void showContent()
}

