package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationDistrict {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("status")
    @Expose
    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<location_district_master> getLocation_district_master() {
        return location_district_master;
    }

    public void setLocation_district_master(List<location_district_master> location_district_master) {
        this.location_district_master = location_district_master;
    }

    @SerializedName("location_district_master")
    @Expose
    private List<location_district_master> location_district_master = null;
}
