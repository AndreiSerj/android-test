package com.siarzhantau.andrei.locations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.siarzhantau.andrei.locations.model.Location;
import com.siarzhantau.andrei.locations.mvp.LocationDetailsPresenter;
import com.siarzhantau.andrei.locations.mvp.LocationDetailsView;

public class LocationDetailsActivity extends MvpActivity<LocationDetailsView, LocationDetailsPresenter>
        implements LocationDetailsView {

    public static String LOCATION_ID_ATTR = "locationId";

    private TextView mTextName;
    private TextView mTextDistance;
    private EditText mTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        final String locationId = getIntent().getStringExtra(LOCATION_ID_ATTR);

        mTextName = (TextView) findViewById(R.id.textName);
        mTextDistance = (TextView) findViewById(R.id.textDistance);
        mTextDescription = (EditText) findViewById(R.id.textDescription);
        findViewById(R.id.buttonOk).setOnClickListener(view -> {
            presenter.updateLocationDescription(locationId, mTextDescription.getText().toString());
            finish();
        });

        final Location location = presenter.getLocationById(locationId);
        updateContent(location);
    }

    private void updateContent(Location location) {
        mTextName.setText(location.name);
        mTextDistance.setText(String.format(getString(R.string.distance_m), location.distance));
        mTextDescription.setText(location.description);
    }

    @NonNull
    @Override
    public LocationDetailsPresenter createPresenter() {
        return new LocationDetailsPresenter();
    }

}
