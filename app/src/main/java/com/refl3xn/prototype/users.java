package com.refl3xn.prototype;

public class users {
    String name, phone;
    double longitude, latitue;

    public users(){
        this.name = "";
        this.phone = "";
        this.latitue = 0.0;
        this.longitude = 0.0;
    }

    public users(double longitude, double latitue) {
        this.longitude = longitude;
        this.latitue = latitue;
    }

    public users(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public users(String name, String phone, double longitude, double latitue) {
        this.name = name;
        this.phone = phone;
        this.longitude = longitude;
        this.latitue = latitue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitue() {
        return latitue;
    }

    public void setLatitue(double latitue) {
        this.latitue = latitue;
    }
}
