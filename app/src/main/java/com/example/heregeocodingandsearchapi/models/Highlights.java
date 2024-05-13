package com.example.heregeocodingandsearchapi.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Highlights {

    @SerializedName("title")
    @Expose
    private List<Title> title;
    @SerializedName("address")
    @Expose
    private Address__1 address;

    public List<Title> getTitle() {
        return title;
    }

    public void setTitle(List<Title> title) {
        this.title = title;
    }

    public Address__1 getAddress() {
        return address;
    }

    public void setAddress(Address__1 address) {
        this.address = address;
    }

}