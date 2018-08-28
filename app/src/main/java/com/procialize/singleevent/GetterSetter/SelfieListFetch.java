package com.procialize.singleevent.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 1/2/2018.
 */

public class SelfieListFetch {

    @SerializedName("selfie_list")
    @Expose
    private List<SelfieList> selfieList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<SelfieList> getSelfieList() {
        return selfieList;
    }

    public void setSelfieList(List<SelfieList> selfieList) {
        this.selfieList = selfieList;
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
}
