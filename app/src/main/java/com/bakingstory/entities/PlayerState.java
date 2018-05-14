package com.bakingstory.entities;

/**
 * Created by emil.ivanov on 5/14/18.
 * <p>
 * Helper class that will hold the current video player state
 */
public class PlayerState {

    long seekPosition;
    int currentWindow;
    boolean playWhenReady;

    public PlayerState(long seekPosition, int currentWindow, boolean playWhenReady) {
        this.seekPosition = seekPosition;
        this.currentWindow = currentWindow;
        this.playWhenReady = playWhenReady;
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
}
