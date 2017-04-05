package com.siarzhantau.andrei.locations.model;

import com.siarzhantau.andrei.locations.utils.LocationsUtil;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Location extends RealmObject {

    @PrimaryKey
    public String id;
    public String name;
    public String description;
    public double lat;
    public double lng;
    public float distance;
    public boolean isCustom;

    public Location() {}

    public Location(String name, double lat, double lng) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.distance = LocationsUtil.calculateDistance(name, lat, lng);
    }

    public Location(String name, double lat, double lng, boolean isCustom) {
        this(name, lat, lng);
        this.isCustom = isCustom;
    }
}
