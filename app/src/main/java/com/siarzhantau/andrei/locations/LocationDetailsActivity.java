package com.siarzhantau.andrei.locations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.siarzhantau.andrei.locations.model.Location;
import com.siarzhantau.andrei.locations.mvp.LocationDetailsPresenter;
import com.siarzhantau.andrei.locations.mvp.LocationDetailsView;

public class LocationDetailsActivity extends MvpActivity<LocationDetailsView, LocationDetailsPresenter>
        implements LocationDetailsView {

    public static String LOCATION_ID_ATTR = "locationId";

    TextView mTextName;
    TextView mTextLocation;
    EditText mTextDescription;
    Button mButtonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String locationId = getIntent().getStringExtra(LOCATION_ID_ATTR);

        mTextName = (TextView) findViewById(R.id.textName);
        mTextLocation = (TextView) findViewById(R.id.textLocation);
        mTextDescription = (EditText) findViewById(R.id.textDescription);
        mButtonSubmit = (Button) findViewById(R.id.buttonOk);
        mButtonSubmit.setOnClickListener(view -> {
            presenter.updateLocationDescription(locationId, mTextDescription.getText().toString());
            finish();
        });

        final Location location = presenter.getLocationById(locationId);
        mTextName.setText(location.name);
        mTextLocation.setText(location.lat + " " + location.lng);
        mTextDescription.setText(location.description);
    }

    @NonNull
    @Override
    public LocationDetailsPresenter createPresenter() {
        return new LocationDetailsPresenter();
    }

}
