package com.siarzhantau.andrei.locations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import com.siarzhantau.andrei.locations.model.Location;
import com.siarzhantau.andrei.locations.mvp.LocationsMapPresenter;
import com.siarzhantau.andrei.locations.mvp.LocationsMapView;

import java.util.UUID;

import io.realm.RealmResults;

public class MapContentFragment extends MvpFragment<LocationsMapView, LocationsMapPresenter>
        implements LocationsMapView, OnMapReadyCallback, OnMapLongClickListener {

    private MapView mMapView;
    private GoogleMap mGoogleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.animateCamera(getCameraUpdateForSydney());
        mGoogleMap.setOnMarkerClickListener(marker -> {
            getActivity().startActivity(new Intent(getActivity(),
                    LocationDetailsActivity.class).putExtra(LocationDetailsActivity.LOCATION_ID_ATTR, marker.getSnippet()));
            return true;
        });

        updateMarkers(presenter.getLocations());
    }

    @NonNull
    @Override
    public LocationsMapPresenter createPresenter() {
        return new LocationsMapPresenter();
    }

    @Override
    public void showContent(RealmResults<Location> locations) {
        mGoogleMap.clear();
        updateMarkers(locations);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        showAddNewLocationDialog(getContext(), point);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private CameraUpdate getCameraUpdateForSydney() {
        LatLng sydney = new LatLng(-33.88, 151.21);
        return CameraUpdateFactory.newLatLngZoom(sydney, 11);
    }

    private void showAddNewLocationDialog(Context context, final LatLng point) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.new_location_name);

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT );

        builder.setView(input);
        builder.setPositiveButton(R.string.button_ok, (dialog, which) -> {
            final String name = input.getText().toString();
            final String id = UUID.randomUUID().toString();

            mGoogleMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title(name)
                    .snippet(id)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            presenter.createNewLocation(point, name, id);
            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.button_cancel, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void updateMarkers(RealmResults<Location> locations) {
        for (Location location : locations) {
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.lat, location.lng))
                    .title(location.name)
                    .snippet(location.id)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }
}

