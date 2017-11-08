package com.nanodegree.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marceloguerra on 03/10/2017.
 */
public class Ingredient implements Parcelable {

    private int id;
    private Double quantity;
    private String measure;
    private String description;

    public Ingredient(){};

    public Ingredient(Parcel in){
        id = in.readInt();
        quantity = in.readDouble();
        measure = in.readString();
        description = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeDouble(getQuantity());
        dest.writeString(getMeasure());
        dest.writeString(getDescription());
    }
}
