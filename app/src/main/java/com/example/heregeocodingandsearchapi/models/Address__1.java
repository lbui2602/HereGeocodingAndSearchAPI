
package com.example.heregeocodingandsearchapi.models; ;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address__1 {

    @SerializedName("label")
    @Expose
    private List<Object> label;

    public List<Object> getLabel() {
        return label;
    }

    public void setLabel(List<Object> label) {
        this.label = label;
    }

}