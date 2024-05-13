package com.example.heregeocodingandsearchapi.models; ;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemResponse {

    @SerializedName("items")
    @Expose
    private List<Item> items;
    @SerializedName("queryTerms")
    @Expose
    private List<Object> queryTerms;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Object> getQueryTerms() {
        return queryTerms;
    }

    public void setQueryTerms(List<Object> queryTerms) {
        this.queryTerms = queryTerms;
    }

}