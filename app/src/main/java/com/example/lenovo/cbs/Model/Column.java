package com.example.lenovo.cbs.Model;

/**
 * Created by LenoVo on 30.04.2018.
 */

public class Column {
    private String mSeat;
    private int mStatus;
    private String mPrice;
    private int mColour;
    private String mId;

    public Column(String id,String seat,int status,String price, int colour){
        mId = id;
        mSeat = seat;
        mStatus = status;
        mPrice = price;
        mColour = colour;


    }

    public String getSeat() {
        return mSeat;
    }

    public void setSeat(String seat) {
        this.mSeat = seat;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        this.mPrice = price;
    }

    public int getColour() {
        return mColour;
    }

    public void setColour(int colour) {
        this.mColour = colour;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
}
