package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class redemption_status {

    @SerializedName("request_id")
    @Expose
    private String request_id;

    @SerializedName("status_changed_date")
    @Expose
    private String status_changed_date;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getStatus_changed_date() {
        return status_changed_date;
    }

    public void setStatus_changed_date(String status_changed_date) {
        this.status_changed_date = status_changed_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    private String status;

    public redemption_status() {

    }

    public redemption_status(String request_id, String status, String status_changed_date) {
        this.request_id = request_id;
        this.status = status;
        this.status_changed_date = status_changed_date;
    }
}
