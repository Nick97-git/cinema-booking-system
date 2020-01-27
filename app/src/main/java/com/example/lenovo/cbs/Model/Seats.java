package com.example.lenovo.cbs.Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by LenoVo on 30.04.2018.
 */

public class Seats {
    private int mRow;
    private ArrayList<Column> mSeats;

    public Seats(int row, ArrayList<Column> seats){
       mRow = row;
       mSeats = seats;
    }

    public ArrayList<Column> getSeats() {
        return mSeats;
    }

    public void setSeats(ArrayList<Column> seats) {
        this.mSeats = seats;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int row) {
        this.mRow = row;
    }
}
