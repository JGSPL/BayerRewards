package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountHistory {

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

    @SerializedName("total_available_point")
    @Expose
    private String total_available_point;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    public List<account_history_data> getAccount_history_data() {
        return account_history_data;
    }

    public void setAccount_history_data(List<account_history_data> account_history_data) {
        this.account_history_data = account_history_data;
    }

    @SerializedName("account_history_data")
    @Expose
    private List<account_history_data> account_history_data = null;


}
