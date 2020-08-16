package com.fareez.helpsy.Model;

public class PostMap {

    public String pid, foodLevel, clothLevel, bookLevel, status, networkSSID, signalStrength, security;
    public Double latitude, longitude;

    public PostMap()
    {

    }

    public PostMap(String pid, String foodLevel, String clothLevel, String bookLevel, String status, String networkSSID, String signalStrength, String security, Double latitude, Double longitude) {
        this.pid = pid;
        this.foodLevel = foodLevel;
        this.clothLevel = clothLevel;
        this.bookLevel = bookLevel;
        this.status = status;
        this.networkSSID = networkSSID;
        this.signalStrength = signalStrength;
        this.security = security;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(String foodLevel) {
        this.foodLevel = foodLevel;
    }

    public String getClothLevel() {
        return clothLevel;
    }

    public void setClothLevel(String clothLevel) {
        this.clothLevel = clothLevel;
    }

    public String getBookLevel() {
        return bookLevel;
    }

    public void setBookLevel(String bookLevel) {
        this.bookLevel = bookLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNetworkSSID() {
        return networkSSID;
    }

    public void setNetworkSSID(String networkSSID) {
        this.networkSSID = networkSSID;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
