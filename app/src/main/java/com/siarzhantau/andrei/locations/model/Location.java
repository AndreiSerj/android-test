package com.siarzhantau.andrei.locations.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Location extends RealmObject {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("lat")
    @Expose
    public double lat;
    @SerializedName("lng")
    @Expose
    public double lng;

    @PrimaryKey
    public String id;
    public String description;
    public float distance;
    public boolean custom;

}
