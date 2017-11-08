package com.nanodegree.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marceloguerra on 08/10/2017.
 */
public class Step implements Parcelable {

    public static final String STEP_INDEX = "STEP-INDEX";
    public static final String STEP_ARRAY = "STEP-ARRAY";

    private int index;
    private int id;
    private String instruction;
    private String description;
    private String videoURL;

    public Step(){};

    public Step(int id, String description, String instruction, String videoURL){
       this.id = id;
       this.description = description;
       this.instruction = instruction;
       this.videoURL = videoURL;
    }

    public Step(Parcel in){
        index = in.readInt();
        id = in.readInt();
        description = in.readString();
        instruction = in.readString();
        videoURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getIndex());
        dest.writeInt(getId());
        dest.writeString(getDescription());
        dest.writeString(getInstruction());
        dest.writeString(getVideoURL());
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
}
