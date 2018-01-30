package com.example.android.hackvsit.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Nutrition implements Parcelable {
    public Nutrition() {
    }

    private String mCalcium,mCalories,mCholesterol,mIron,mProtein,mFat;

    public Nutrition(String mCalcium, String mCalories, String mCholesterol, String mIron, String mProtein, String mFat) {
        this.mCalcium = mCalcium;
        this.mCalories = mCalories;
        this.mCholesterol = mCholesterol;
        this.mIron = mIron;
        this.mProtein = mProtein;
        this.mFat = mFat;
    }

    public String getmCalcium() {
        return mCalcium;
    }

    public void setmCalcium(String mCalcium) {
        this.mCalcium = mCalcium;
    }

    public String getmCalories() {
        return mCalories;
    }

    public void setmCalories(String mCalories) {
        this.mCalories = mCalories;
    }

    public String getmCholesterol() {
        return mCholesterol;
    }

    public void setmCholesterol(String mCholesterol) {
        this.mCholesterol = mCholesterol;
    }

    public String getmIron() {
        return mIron;
    }

    public void setmIron(String mIron) {
        this.mIron = mIron;
    }

    public String getmProtein() {
        return mProtein;
    }

    public void setmProtein(String mProtein) {
        this.mProtein = mProtein;
    }

    public String getmFat() {
        return mFat;
    }

    public void setmFat(String mFat) {
        this.mFat = mFat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCalcium);
        dest.writeString(this.mCalories);
        dest.writeString(this.mCholesterol);
        dest.writeString(this.mIron);
        dest.writeString(this.mProtein);
        dest.writeString(this.mFat);
    }

    protected Nutrition(Parcel in) {
        this.mCalcium = in.readString();
        this.mCalories = in.readString();
        this.mCholesterol = in.readString();
        this.mIron = in.readString();
        this.mProtein = in.readString();
        this.mFat = in.readString();
    }

    public static final Parcelable.Creator<Nutrition> CREATOR = new Parcelable.Creator<Nutrition>() {
        @Override
        public Nutrition createFromParcel(Parcel source) {
            return new Nutrition(source);
        }

        @Override
        public Nutrition[] newArray(int size) {
            return new Nutrition[size];
        }
    };
}
