package com.procialize.singleevent.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserEventList {

    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("event_start")
    @Expose
    private String eventStart;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("logo")
    @Expose
    private String logo;

//    @SerializedName("logo")
//    @Expose
//    private String logo;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}