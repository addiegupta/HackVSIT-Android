package com.example.android.hackvsit.model;


import java.util.ArrayList;

public class Machine {

    private String mId,mLat,mLong,mVendorId;
    private ArrayList<Product> mProducts;

    public Machine(String mId, String mLat, String mLong, String mVendorId, ArrayList<Product> mProducts) {
        this.mId = mId;
        this.mLat = mLat;
        this.mLong = mLong;
        this.mVendorId = mVendorId;
        this.mProducts = mProducts;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmLong() {
        return mLong;
    }

    public void setmLong(String mLong) {
        this.mLong = mLong;
    }

    public String getmVendorId() {
        return mVendorId;
    }

    public void setmVendorId(String mVendorId) {
        this.mVendorId = mVendorId;
    }

    public ArrayList<Product> getmProducts() {
        return mProducts;
    }

    public void setmProducts(ArrayList<Product> mProducts) {
        this.mProducts = mProducts;
    }
}
