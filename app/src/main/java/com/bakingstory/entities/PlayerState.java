package com.bakingstory.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by emil.ivanov on 5/14/18.
 * <p>
 * Helper class that will hold the current video player state
 */
public class PlayerState implements Parcelable {
    public static final String DATA = "player_state_data";

    private long seekPosition;
    private int currentWindow;
    private boolean playWhenReady;
    private int orientation;


    public PlayerState(long seekPosition, int currentWindow, boolean playWhenReady, int orientation) {
        this.seekPosition = seekPosition;
        this.currentWindow = currentWindow;
        this.playWhenReady = playWhenReady;
        this.orientation = orientation;
    }

    protected PlayerState(Parcel in) {
        seekPosition = in.readLong();
        currentWindow = in.readInt();
        playWhenReady = in.readByte() != 0;
        orientation = in.readInt();
    }

    public static final Creator<PlayerState> CREATOR = new Creator<PlayerState>() {
        @Override
        public PlayerState createFromParcel(Parcel in) {
            return new PlayerState(in);
        }

        @Override
        public PlayerState[] newArray(int size) {
            return new PlayerState[size];
        }
    };

    public PlayerState(long seekPosition, int currentWindow, boolean playWhenReady) {
        this.seekPosition = seekPosition;
        this.currentWindow = currentWindow;
        this.playWhenReady = playWhenReady;
    }

    public int getOrientation() {
        return orientation;
    }

    public long getSeekPosition() {
        return seekPosition;
    }

    public int getCurrentWindow() {
        return currentWindow;
    }

    public boolean isPlayWhenReady() {
        return playWhenReady;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(seekPosition);
        dest.writeInt(currentWindow);
        dest.writeByte((byte) (playWhenReady ? 1 : 0));
        dest.writeInt(orientation);
    }
}
