package com.nanodegree.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marceloguerra on 03/10/2017.
 */
public class Recipe implements Parcelable {

    public static final String RECIPE_OBJ = "RECIPE-OBJ";

    private int id;
    private String name;
    private int servings;

    public Recipe(){

    }

    public Recipe(Parcel in){
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeInt(getServings());
    }
}
