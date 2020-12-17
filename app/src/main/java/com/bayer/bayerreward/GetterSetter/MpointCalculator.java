package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MpointCalculator {

    @SerializedName("m_points_list")
    @Expose
    private List<m_points_list> m_points_list = null;

    @SerializedName("status")
    @Expose
    private String status;

    public List<m_points_list> getM_points_list() {
        return m_points_list;
    }

    public void setM_points_list(List<m_points_list> m_points_list) {
        this.m_points_list = m_points_list;
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

    @SerializedName("msg")
    @Expose
    private String msg;

}
