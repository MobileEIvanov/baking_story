package com.bakingstory.RecipeDetails.BakingSteps;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.bakingstory.R;
import com.bakingstory.RecipeDetails.FragmentStepDescription;
import com.bakingstory.databinding.LayoutFullscreenVideoDialogBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;
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
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by emil.ivanov on 5/7/18.
 */
public class FullscreenVideoDialog
        extends DialogFragment {

    public static final String TAG = "fullscreen";
    private ComponentListener componentListener;
    private SimpleExoPlayer mExoPlayer;

    public static FullscreenVideoDialog newInstance(BakingStep bakingStep) {
        Bundle args = new Bundle();
        args.putParcelable(BakingStep.BAKING_DATA, bakingStep);
        FullscreenVideoDialog dialog = new FullscreenVideoDialog();
        dialog.setArguments(args);
        return dialog;
    }

    LayoutFullscreenVideoDialogBinding mBinding;
    private BakingStep mBakingStep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBakingStep = getArguments().getParcelable(BakingStep.BAKING_DATA);
            if (mBakingStep == null) {
                dismissAllowingStateLoss();
            }
        }
        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_fullscreen_video_dialog, container, false);
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


    private void showSystemUi() {
        mBinding.videoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT > 23) {
            if (mBakingStep != null) {
                hideSystemUi();
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


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
}
