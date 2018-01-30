package com.example.android.hackvsit.model;

public class Nutrition {

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
}
