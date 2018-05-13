package com.bakingstory;

import android.app.Application;

/**
 * Created by emil.ivanov on 5/13/18.
 */
public class ApplicationBakingStory extends Application {
    private static ApplicationBakingStory mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


    public static synchronized ApplicationBakingStory getInstance() {
        return mInstance;
    }
}
