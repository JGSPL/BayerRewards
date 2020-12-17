package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationVillage {

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


    public List<location_village_master> getLocation_village_master() {
        return location_village_master;
    }

    public void setLocation_village_master(List<location_village_master> location_village_master) {
        this.location_village_master = location_village_master;
    }

    @SerializedName("location_village_master")
    @Expose
    private List<location_village_master> location_village_master = null;
}
