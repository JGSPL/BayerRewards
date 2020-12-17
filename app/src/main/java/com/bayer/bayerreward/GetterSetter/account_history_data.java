package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class account_history_data {

    @SerializedName("transaction_date")
    @Expose
    private String transaction_date;

    @SerializedName("m_points_value")
    @Expose
    private String m_points_value;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getM_points_value() {
        return m_points_value;
    }

    public void setM_points_value(String m_points_value) {
        this.m_points_value = m_points_value;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    @SerializedName("pack")
    @Expose

    private String pack;


}
