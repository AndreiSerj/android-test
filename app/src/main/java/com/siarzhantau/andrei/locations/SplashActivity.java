package com.siarzhantau.andrei.locations;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.siarzhantau.andrei.locations.mvp.SplashPresenter;
import com.siarzhantau.andrei.locations.mvp.SplashView;

public class SplashActivity extends MvpActivity<SplashView, SplashPresenter>
        implements SplashView  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        presenter.populateRealmOnFirstRun(getApplicationContext());
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        return new SplashPresenter();
    }

    @Override
    public void goToNextScreen() {
        startActivity(new Intent(this, LocationsActivity.class));
        finish();
    }
}
