package com.example.lenovo.cbs.Model;

import io.realm.RealmObject;

/**
 * Created by LenoVo on 14.04.2018.
 */

public class Theater extends RealmObject {
    private String id;
    private String name;
    private double longitude;
    private double latitude;
    private String site;
    private String address;

    @Override
    public String toString() {
        return "Адрес: " + address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
