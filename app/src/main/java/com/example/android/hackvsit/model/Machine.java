package com.example.android.hackvsit.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Machine implements Parcelable {

    private String mId,mLat,mLong,mVendorId;
    private ArrayList<Product> mProducts;

    public ArrayList<String> getmRecommendations() {
        return mRecommendations;
    }

    public void setmRecommendations(ArrayList<String> mRecommendations) {
        this.mRecommendations = mRecommendations;
    }

    private ArrayList<String> mRecommendations;

    public Machine() {
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mLat);
        dest.writeString(this.mLong);
        dest.writeString(this.mVendorId);
        dest.writeTypedList(this.mProducts);
        dest.writeStringList(this.mRecommendations);
    }

    protected Machine(Parcel in) {
        this.mId = in.readString();
        this.mLat = in.readString();
        this.mLong = in.readString();
        this.mVendorId = in.readString();
        this.mProducts = in.createTypedArrayList(Product.CREATOR);
        this.mRecommendations = in.createStringArrayList();
    }

    public static final Creator<Machine> CREATOR = new Creator<Machine>() {
        @Override
        public Machine createFromParcel(Parcel source) {
            return new Machine(source);
        }

        @Override
        public Machine[] newArray(int size) {
            return new Machine[size];
        }
    };
}
