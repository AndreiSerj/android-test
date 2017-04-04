package com.siarzhantau.andrei.locations;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.siarzhantau.andrei.locations.adapter.LocationAdapter;
import com.siarzhantau.andrei.locations.model.Location;
import com.siarzhantau.andrei.locations.mvp.LocationsListPresenter;
import com.siarzhantau.andrei.locations.mvp.LocationsListView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Provides UI for the view with list of locations.
 */
public class ListContentFragment extends MvpLceFragment<SwipeRefreshLayout, RealmResults<Location>, LocationsListView, LocationsListPresenter>
        implements LocationsListView, SwipeRefreshLayout.OnRefreshListener {

    private LocationAdapter mLocationAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_content, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        // Setup contentView == SwipeRefreshView
        contentView.setOnRefreshListener(this);

        mLocationAdapter = new LocationAdapter(getContext(), presenter.getLocations(), mLocationClicklistener);

        final List<Location> locations = presenter.getLocations();
        Log.d("AAAA", "list: " + locations.size());

        mRecyclerView.setAdapter(mLocationAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        loadData(false);
    }

    @NonNull
    @Override
    public LocationsListPresenter createPresenter() {
        return new LocationsListPresenter();
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public void setData(RealmResults<Location> data) {
        mLocationAdapter = new LocationAdapter(getContext(), data, mLocationClicklistener);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadLocations(pullToRefresh);
    }

    @Override
    public void showContent() {
        super.showContent();

        contentView.setRefreshing(false);
        mLocationAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
    }

    private LocationAdapter.LocationClickListener mLocationClicklistener = locationId -> {
        startActivity(new Intent(getActivity(),
                LocationDetailsActivity.class).putExtra(LocationDetailsActivity.LOCATION_ID_ATTR, locationId));
    };
}