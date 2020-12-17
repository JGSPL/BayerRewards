package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SchemesAndOffers {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

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

    public List<schemes_and_offers_list> getSchemes_and_offers_list() {
        return schemes_and_offers_list;
    }

    public void setSchemes_and_offers_list(List<schemes_and_offers_list> schemes_and_offers_list) {
        this.schemes_and_offers_list = schemes_and_offers_list;
    }

    @SerializedName("schemes_and_offers_list")
    @Expose
    private List<schemes_and_offers_list> schemes_and_offers_list = null;
}
