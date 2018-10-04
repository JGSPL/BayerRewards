package com.procialize.singleevent.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrencyDropDown {

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

    public List<DropDownList> getDropDownList() {
        return dropDownList;
    }

    public void setDropDownList(List<DropDownList> dropDownList) {
        this.dropDownList = dropDownList;
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("currency_dropdown")
    @Expose
    private List<DropDownList> dropDownList = null;
}
