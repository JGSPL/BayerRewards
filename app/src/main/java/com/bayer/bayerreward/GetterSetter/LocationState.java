package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationState {

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

    public List<location_state_master> getLocation_state_master() {
        return location_state_master;
    }

    public void setLocation_state_master(List<location_state_master> location_state_master) {
        this.location_state_master = location_state_master;
    }

    @SerializedName("location_state_master")
    @Expose
    private List<location_state_master> location_state_master = null;

}
