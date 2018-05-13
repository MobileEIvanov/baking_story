package com.bakingstory.utils;


import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Helper class in order to test network request and other data resource that require time for execution.
 *
 * Credits to: https://github.com/googlesamples/android-testing
 */
@SuppressWarnings("CanBeFinal")
public class HelperIdlingResource implements IdlingResource {

    // Idleness is controlled with this boolean.
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
    }


}
