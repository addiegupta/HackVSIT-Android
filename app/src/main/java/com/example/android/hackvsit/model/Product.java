package com.example.android.hackvsit.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private String mName,mImageUrl;
    private int mQuantity, mMaxQuantity, mPrice,mId;
    private Nutrition mNutrition;

    public Product() {
    }

    public Product(String mName, int mId, String mImageUrl, int mQuantity, int mMaxQuantity, int mPrice, Nutrition mNutrition) {
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

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mImageUrl);
        dest.writeInt(this.mQuantity);
        dest.writeInt(this.mMaxQuantity);
        dest.writeInt(this.mPrice);
        dest.writeInt(this.mId);
        dest.writeParcelable(this.mNutrition, flags);
    }

    protected Product(Parcel in) {
        this.mName = in.readString();
        this.mImageUrl = in.readString();
        this.mQuantity = in.readInt();
        this.mMaxQuantity = in.readInt();
        this.mPrice = in.readInt();
        this.mId = in.readInt();
        this.mNutrition = in.readParcelable(Nutrition.class.getClassLoader());
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
