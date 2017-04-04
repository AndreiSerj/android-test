package com.siarzhantau.andrei.locations.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Locations {
    @SerializedName("locations")
    @Expose
    public List<Location> locations;
}
