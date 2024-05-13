package com.example.heregeocodingandsearchapi.models; ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reference {

    @SerializedName("supplier")
    @Expose
    private Supplier supplier;
    @SerializedName("id")
    @Expose
    private String id;

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}