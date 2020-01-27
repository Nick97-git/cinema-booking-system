package com.example.lenovo.cbs.Model;

import android.graphics.Bitmap;

import com.example.lenovo.cbs.R;
import java.util.ArrayList;

public class Actor {
    private String mID;
    private String mActor;
    private Bitmap mPhoto;
    private String mRole;

    public Actor(String id, String actor, Bitmap photo, String role){
        mActor = actor;
        mPhoto = photo;
        mID = id;
        mRole = role;
    }


    public String getID() {
        return mID;
    }

    public void setID(String id) {
        mID = id;
    }

    public String getActor() {
        return mActor;
    }

    public void setActor(String actor) {
        mActor = actor;
    }

    public Bitmap getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Bitmap photo) {
        mPhoto = photo;
    }

    public String getRole() {
        return mRole;
    }

    public void setRole(String role) {
        this.mRole = role;
    }
}
