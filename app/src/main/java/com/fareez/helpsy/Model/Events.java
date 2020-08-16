package com.fareez.helpsy.Model;

public class Events
{
    private String eid, eventName, eventImage, eventDate, eventPax, eventDesc, publishDate;

    public Events()
    {

    }

    public Events(String eid, String eventName, String eventImage, String eventDate, String eventPax, String eventDesc, String publishDate) {
        this.eid = eid;
        this.eventName = eventName;
        this.eventImage = eventImage;
        this.eventDate = eventDate;
        this.eventPax = eventPax;
        this.eventDesc = eventDesc;
        this.publishDate = publishDate;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventPax() {
        return eventPax;
    }

    public void setEventPax(String eventPax) {
        this.eventPax = eventPax;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
