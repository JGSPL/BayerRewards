package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class redemption_data {

    @SerializedName("request_id")
    @Expose
    private String request_id ;

    @SerializedName("redemption_date")
    @Expose
    private String redemption_date ;

    @SerializedName("product_name")
    @Expose
    private String product_name ;

    @SerializedName("quantity")
    @Expose
    private String quantity ;

    @SerializedName("points")
    @Expose
    private String points ;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getRedemption_date() {
        return redemption_date;
    }

    public void setRedemption_date(String redemption_date) {
        this.redemption_date = redemption_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getLast_status() {
        return last_status;
    }

    public void setLast_status(String last_status) {
        this.last_status = last_status;
    }

    @SerializedName("last_status")
    @Expose
    private String last_status ;


}
