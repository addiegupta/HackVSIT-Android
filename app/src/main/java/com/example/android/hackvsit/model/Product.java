package com.example.android.hackvsit.model;

public class Product {

    private String mName, mId,mImageUrl;
    private int mQuantity, mMaxQuantity, mPrice;
    private Nutrition mNutrition;

    public Product(String mName, String mId, String mImageUrl, int mQuantity, int mMaxQuantity, int mPrice, Nutrition mNutrition) {
        this.mName = mName;
        this.mId = mId;
        this.mImageUrl = mImageUrl;
        this.mQuantity = mQuantity;
        this.mMaxQuantity = mMaxQuantity;
        this.mPrice = mPrice;
        this.mNutrition = mNutrition;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public int getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public int getmMaxQuantity() {
        return mMaxQuantity;
    }

    public void setmMaxQuantity(int mMaxQuantity) {
        this.mMaxQuantity = mMaxQuantity;
    }

    public int getmPrice() {
        return mPrice;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public Nutrition getmNutrition() {
        return mNutrition;
    }

    public void setmNutrition(Nutrition mNutrition) {
        this.mNutrition = mNutrition;
    }
}
