package com.example.heregeocodingandsearchapi.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FieldScore {

    @SerializedName("streets")
    @Expose
    private List<Double> streets;

    public List<Double> getStreets() {
        return streets;
    }

    public void setStreets(List<Double> streets) {
        this.streets = streets;
    }

}
