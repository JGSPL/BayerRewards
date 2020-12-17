package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class product_achived_planned {

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPlanned_volume() {
        return planned_volume;
    }

    public void setPlanned_volume(String planned_volume) {
        this.planned_volume = planned_volume;
    }

    public String getAchived_volume() {
        return achived_volume;
    }

    public void setAchived_volume(String achived_volume) {
        this.achived_volume = achived_volume;
    }

    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("planned_volume")
    @Expose
    private String planned_volume;


    @SerializedName("achived_volume")
    @Expose
    private String achived_volume;



}
