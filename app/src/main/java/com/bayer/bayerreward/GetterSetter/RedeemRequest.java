package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedeemRequest {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    public String getTotal_available_point() {
        return total_available_point;
    }

    public void setTotal_available_point(String total_available_point) {
        this.total_available_point = total_available_point;
    }

    @SerializedName("total_available_point")
    @Expose
    private String total_available_point;

}
