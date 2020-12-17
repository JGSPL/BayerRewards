package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class leaderboard_data {

    @SerializedName("territory_data")
    @Expose
    private List<territory_data> territory_data = null;
    @SerializedName("region_data")
    @Expose
    private List<region_data> region_data = null;
    @SerializedName("business_unit_data")
    @Expose
    private List<business_unit_data> business_unit_data = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("total_available_point")
    @Expose
    private String total_available_point;



    @SerializedName("territory_rank")
    @Expose
    private String territory_rank;

    public String getTerritory_rank() {
        return territory_rank;
    }

    public void setTerritory_rank(String territory_rank) {
        this.territory_rank = territory_rank;
    }

    public String getRegion_rank() {
        return region_rank;
    }

    public void setRegion_rank(String region_rank) {
        this.region_rank = region_rank;
    }

    @SerializedName("region_rank")
    @Expose
    private String region_rank;

    public List<territory_data> getTerritory_data() {
        return territory_data;
    }

    public void setTerritory_data(List<territory_data> territory_data) {
        this.territory_data = territory_data;
    }

    public List<region_data> getRegion_data() {
        return region_data;
    }

    public void setRegion_data(List<region_data> region_data) {
        this.region_data = region_data;
    }

    public List<business_unit_data> getBusiness_unit_data() {
        return business_unit_data;
    }

    public void setBusiness_unit_data(List<com.bayer.bayerreward.GetterSetter.business_unit_data> business_unit_data) {
        this.business_unit_data = business_unit_data;
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

    public String getTotal_available_point() {
        return total_available_point;
    }

    public void setTotal_available_point(String total_available_point) {
        this.total_available_point = total_available_point;
    }

    public String getAs_on_date() {
        return as_on_date;
    }

    public void setAs_on_date(String as_on_date) {
        this.as_on_date = as_on_date;
    }

    @SerializedName("as_on_date")
    @Expose
    private String as_on_date;

}
