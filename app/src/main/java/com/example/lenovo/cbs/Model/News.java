package com.example.lenovo.cbs.Model;

import android.graphics.Bitmap;

import io.realm.RealmObject;


public class News extends RealmObject {
    private String title;
    private String text;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
