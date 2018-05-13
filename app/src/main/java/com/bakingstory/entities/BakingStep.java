package com.bakingstory.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by emil.ivanov on 3/28/18.
 * Baking Step POJO representation
 * "id":0,
 * "shortDescription":"Recipe Introduction",
 * "description":"Recipe Introduction",
 * "videoURL":"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc33_-intro-brownies/-intro-brownies.mp4",
 * "thumbnailURL":""
 */

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class BakingStep implements Parcelable {

    public static final String BAKING_DATA = "baking_data";
    private long id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;
    private boolean isSelected;

    protected BakingStep(Parcel in) {
        id = in.readLong();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<BakingStep> CREATOR = new Creator<BakingStep>() {
        @Override
        public BakingStep createFromParcel(Parcel in) {
            return new BakingStep(in);
        }

        @Override
        public BakingStep[] newArray(int size) {
            return new BakingStep[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    @Override
    public String toString() {
        return "BakingStep{" +
                "id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", videoURL='" + videoURL + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
