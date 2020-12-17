package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Catlog {

    public String getTotal_available_point() {
        return total_available_point;
    }

    public void setTotal_available_point(String total_available_point) {
        this.total_available_point = total_available_point;
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

    public List<catalog_list> getCatalog_list() {
        return catalog_list;
    }

    public void setCatalog_list(List<catalog_list> catalog_list) {
        this.catalog_list = catalog_list;
    }

    public List<redemption_data> getRedemption_data() {
        return redemption_data;
    }

    public void setRedemption_data(List<redemption_data> redemption_data) {
        this.redemption_data = redemption_data;
    }

    public List<redemption_status> getRedemption_status() {
        return redemption_status;
    }

    public void setRedemption_status(List<redemption_status> redemption_status) {
        this.redemption_status = redemption_status;
    }

    @SerializedName("total_available_point")
    @Expose
    private String total_available_point;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("catalog_list")
    @Expose
    private List<catalog_list> catalog_list = null;

    @SerializedName("redemption_data")
    @Expose
    private List<redemption_data> redemption_data = null;

    @SerializedName("redemption_status")
    @Expose
    private List<redemption_status> redemption_status = null;

}
