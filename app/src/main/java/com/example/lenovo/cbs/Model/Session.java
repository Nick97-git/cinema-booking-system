package com.example.lenovo.cbs.Model;

import android.support.annotation.NonNull;

/**
 * Created by LenoVo on 29.04.2018.
 */

public class Session{

    private String mId;
    private String mSession;
    private String m3D;

    public Session(String id,String session, String d){
        mId = id;
        mSession = session;
        m3D = d;
    }

    @Override
    public String toString() {
        return "" + mSession + " " + m3D;
    }



    public String get3D() {
        return m3D;
    }

    public void set3D(String D) {
        this.m3D = D;
    }


    public String getSession() {
        return mSession;
    }

    public void setSession(String session) {
        this.mSession = session;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
}
