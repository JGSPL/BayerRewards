package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyPerformance {

    public String getTotal_available_point() {
        return total_available_point;
    }

    public void setTotal_available_point(String total_available_point) {
        this.total_available_point = total_available_point;
    }

    public String getTotal_achieved_value() {
        return total_achieved_value;
    }

    public void setTotal_achieved_value(String total_achieved_value) {
        this.total_achieved_value = total_achieved_value;
    }

    public String getTotal_achieved_qpoint_value() {
        return total_achieved_qpoint_value;
    }

    public void setTotal_achieved_qpoint_value(String total_achieved_qpoint_value) {
        this.total_achieved_qpoint_value = total_achieved_qpoint_value;
    }

    public List<product_achived_planned> getProduct_achived_planned() {
        return product_achived_planned;
    }

    public void setProduct_achived_planned(List<product_achived_planned> product_achived_planned) {
        this.product_achived_planned = product_achived_planned;
    }

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

    @SerializedName("total_available_point")
    @Expose
    private String total_available_point;

    @SerializedName("total_achieved_value")
    @Expose
    private String total_achieved_value;

    @SerializedName("total_achieved_qpoint_value")
    @Expose
    private String total_achieved_qpoint_value;

    @SerializedName("product_achived_planned")
    @Expose
    private List<product_achived_planned> product_achived_planned = null;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;
}
