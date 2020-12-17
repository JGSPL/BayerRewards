package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class total_available_point {

    @SerializedName("total_available_point")
    @Expose
    private String total_available_point;

    @SerializedName("as_on_date")
    @Expose
    private String as_on_date;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("business_unit_data")
    @Expose
    private List<business_unit_data> business_unit_data = null;

    @SerializedName("region_data")
    @Expose
    private List<region_data> region_data = null;

 @SerializedName("territory_data")
    @Expose
    private List<territory_data> territory_data = null;


}
