package com.bakingstory.RecipeDetails;


import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bakingstory.R;
import com.bakingstory.RecipeDetails.BakingSteps.FullscreenVideoDialog;
import com.bakingstory.databinding.LayoutStepDescriptionBinding;
import com.bakingstory.entities.BakingStep;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStepDescription extends Fragment {


    private static final String TAG = "StepDescription";
    private ComponentListener componentListener;
    private SimpleExoPlayer mExoPlayer;

    private long playbackPosition = 0;
    private int currentWindow = 0;
    private boolean playWhenReady = true;

    public FragmentStepDescription() {
        // Required empty public constructor
    }


    public static FragmentStepDescription newInstance(BakingStep data) {

        Bundle args = new Bundle();
        args.putParcelable(BakingStep.BAKING_DATA, data);
        FragmentStepDescription fragment = new FragmentStepDescription();
        fragment.setArguments(args);
        return fragment;
    }

    LayoutStepDescriptionBinding mBinding;
    BakingStep mBakingStep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mBakingStep = getArguments().getParcelable(BakingStep.BAKING_DATA);
        }




    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (mBakingStep != null) {
                initializePlayer(mBakingStep.getVideoURL());
            }
        }
    }


    private void showSystemUi() {
        mBinding.videoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: 5/8/18 Show fullscreen video dialog!
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            showFullScreenDialog();
//        } else {
//            hideFullScreenDialog();
//        }
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            if (mBakingStep != null) {
                initializePlayer(mBakingStep.getVideoURL());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_step_description, container, false);
        populateCurrentBakingStep(mBakingStep);
        return mBinding.getRoot();
    }

    private static final DefaultBandwidthMeter BANDWIDTH_METER =
            new DefaultBandwidthMeter();

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mBinding.videoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    private void showFullScreenDialog() {
        String video = "";
        FullscreenVideoDialog.newInstance(video)
                .show(getChildFragmentManager(), FullscreenVideoDialog.TAG);
    }

    private void hideFullScreenDialog() {

        FullscreenVideoDialog dialog = (FullscreenVideoDialog) getChildFragmentManager().findFragmentByTag(FullscreenVideoDialog.TAG);
        if (dialog != null && dialog.isVisible()) {
            dialog.dismissAllowingStateLoss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    /**
     * Initialize ExoPlayer.
     * https://codelabs.developers.google.com/codelabs/exoplayer-intro/#5
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(String mediaUri) {

        if (mediaUri == null || mediaUri.isEmpty()) {
            mBinding.videoContainer.setVisibility(View.GONE);
            return;
        }

        componentListener = new ComponentListener();
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);


        TrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity(), null);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);

        String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource
                .Factory(new DefaultDataSourceFactory(getActivity(), userAgent))
                .createMediaSource(Uri.parse(mediaUri));


        mExoPlayer.prepare(mediaSource, true, true);
        mExoPlayer.addListener(componentListener);
        mExoPlayer.setPlayWhenReady(false);

        mBinding.videoPlayerView.setPlayer(mExoPlayer);
    }


    public void populateCurrentBakingStep(BakingStep bakingStep) {

        if (bakingStep == null) {
            return;
        }

        if (bakingStep.getShortDescription() != null && !bakingStep.getShortDescription().isEmpty()) {
            String step = null;
            if (bakingStep.getId() > 0) {

                step = String.format(getString(R.string.text_step_description), bakingStep.getId(), bakingStep.getShortDescription());
            } else {
                step = bakingStep.getShortDescription();
            }
            mBinding.tvStepShortDescription.setText(step);
        }

        if (bakingStep.getDescription() != null && !bakingStep.getDescription().isEmpty() && bakingStep.getId() > 0) {
            mBinding.tvStepDescription.setText(bakingStep.getDescription());
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {

            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private class ComponentListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            String stateString;
            switch (playbackState) {
                case Player.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case Player.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case Player.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                case Player.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.d(TAG, "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady);
        }
    }

}
