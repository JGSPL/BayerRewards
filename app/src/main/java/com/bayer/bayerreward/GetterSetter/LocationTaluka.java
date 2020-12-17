package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationTaluka {

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


    public List<location_taluka_master> getLocation_taluka_master() {
        return location_taluka_master;
    }

    public void setLocation_taluka_master(List<location_taluka_master> location_taluka_master) {
        this.location_taluka_master = location_taluka_master;
    }

    @SerializedName("location_taluka_master")
    @Expose
    private List<location_taluka_master> location_taluka_master = null;
}
