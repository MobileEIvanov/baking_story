package com.bakingstory.RecipeDetails;


import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakingstory.R;
import com.bakingstory.databinding.LayoutStepDescriptionBinding;
import com.bakingstory.entities.BakingStep;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
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
    private static final String SEEKER_POSITION = "seeker_position";

    private long playbackPosition = 0;
    private int currentWindow = 0;
    private boolean playWhenReady = true;
    private long mSeekToPosition = 0;

    public FragmentStepDescription() {
        // Required empty public constructor
    }


    public static FragmentStepDescription newInstance(BakingStep data, int position) {

        Bundle args = new Bundle();
        args.putParcelable(BakingStep.BAKING_DATA, data);
        args.putInt("position", position);
        FragmentStepDescription fragment = new FragmentStepDescription();
        fragment.setArguments(args);
        return fragment;
    }

    LayoutStepDescriptionBinding mBinding;
    BakingStep mBakingStep;
    int mMediaIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            mBakingStep = savedInstanceState.getParcelable(BakingStep.BAKING_DATA);
            mSeekToPosition = savedInstanceState.getLong(SEEKER_POSITION, 0);
        } else if (getArguments() != null) {
            mBakingStep = getArguments().getParcelable(BakingStep.BAKING_DATA);
            mMediaIndex = getArguments().getInt("position");
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(BakingStep.BAKING_DATA, mBakingStep);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_step_description, container, false);
        populateCurrentBakingStep(mBakingStep);
        return mBinding.getRoot();
    }

    public void populateCurrentBakingStep(BakingStep bakingStep) {

        if (bakingStep == null) {
            return;
        }

        if (TextUtils.isEmpty(bakingStep.getVideoURL())) {
            mBinding.videoContainer.setVisibility(View.GONE);
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

    private static final DefaultBandwidthMeter BANDWIDTH_METER =
            new DefaultBandwidthMeter();


    private ComponentListener componentListener;
    private SimpleExoPlayer mExoPlayer;

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (mBakingStep != null && getUserVisibleHint()) {
                initializePlayer(mBakingStep.getVideoURL());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            if (mBakingStep != null && getUserVisibleHint()) {
                initializePlayer(mBakingStep.getVideoURL());
            }
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

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (!isVisibleToUser) {
            releasePlayer();
        } else if (isVisibleToUser) {
            if (mBakingStep != null) {
                initializePlayer(mBakingStep.getVideoURL());
            }
        }

        super.setUserVisibleHint(isVisibleToUser);


    }

    //    /**
//     * Extract the media playlist in order to cread dynamic playlist
//     * @return - list of all baking steps with videos.
//     */
//    private List<String> extractMediaResources() {
//        ArrayList<String> media = new ArrayList<>();
//        for (BakingStep bakingStep : mRecipeData.getSteps()) {
//            if (!TextUtils.isEmpty(bakingStep.getVideoURL())) {
//                media.add(bakingStep.getVideoURL());
//            }
//        }
//        return media;
//    }

    /**
     * Initialize ExoPlayer.
     * https://codelabs.developers.google.com/codelabs/exoplayer-intro/#5
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(String mediaUri) {

        if (mediaUri == null || mediaUri.isEmpty()) {

            return;
        }

        componentListener = new ComponentListener();
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource
                .Factory(new DefaultDataSourceFactory(getActivity(), userAgent))
                .createMediaSource(Uri.parse(mediaUri));


        TrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity(), null);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);

        mExoPlayer.prepare(mediaSource, true, true);
        mExoPlayer.addListener(componentListener);
        mExoPlayer.setPlayWhenReady(false);


        mBinding.videoPlayerView.setPlayer(mExoPlayer);
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
