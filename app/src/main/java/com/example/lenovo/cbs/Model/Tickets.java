package com.example.lenovo.cbs.Model;

import io.realm.RealmObject;

public class Tickets extends RealmObject{

    private String title;
    private String row;
    private String seat;
    private String id;
    private String price;
    private String date;
    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRow() {
        return "Ряд: " + row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getSeat() {
        return "Место: " + seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getId() {
        return "ID: " + id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price + " UA";
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
