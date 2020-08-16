package com.fareez.helpsy.Model;

public class JoinedEvent
{
    private String eid, eventName, eventDate, eventImage;

    public JoinedEvent()
    {

    }

    public JoinedEvent(String eid, String eventName, String eventDate, String eventImage) {
        this.eid = eid;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventImage = eventImage;
    }

    public String geteid() {
        return eid;
    }

    public void seteid(String eid) {
        this.eid = eid;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
