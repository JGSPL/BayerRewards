package com.bayer.bayerreward.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AboutBayerKnightFetch {

    public List<about_bayer_knight_list> getAbout_bayer_knight_list() {
        return about_bayer_knight_list;
    }

    public void setAbout_bayer_knight_list(List<about_bayer_knight_list> about_bayer_knight_list) {
        this.about_bayer_knight_list = about_bayer_knight_list;
    }

    @SerializedName("about_bayer_knight_list")
    @Expose
    private List<about_bayer_knight_list> about_bayer_knight_list = null;

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

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getCss_editor() {
        return css_editor;
    }

    public void setCss_editor(String css_editor) {
        this.css_editor = css_editor;
    }

    @SerializedName("css_editor")
    @Expose
    private String css_editor;
}
