package com.bakingstory.recipe_details;


import android.content.res.Configuration;
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

import com.bakingstory.ApplicationBakingStory;
import com.bakingstory.R;
import com.bakingstory.databinding.LayoutStepDescriptionBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.PlayerState;
import com.bakingstory.recipe_details.baking_steps.FullscreenVideoDialog;
import com.bakingstory.utils.UtilsNetworkConnection;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStepDescription extends Fragment implements FullscreenVideoDialog.IDialogInteractions {


    private static final String TAG = "StepDescription";

    private long mSeekPosition = 0;
    private boolean mPlayWhenReady = false;
    private int mCurrentWindow = 0;

    private static final String PLAY_WHEN_READY = "auto_play";
    private static final String SEEK_POSITION = "seek_position";
    private static final String CURRENT_WINDOW = "current_window";


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

    private LayoutStepDescriptionBinding mBinding;
    private BakingStep mBakingStep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            mBakingStep = savedInstanceState.getParcelable(BakingStep.BAKING_DATA);
            mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            mSeekPosition = savedInstanceState.getLong(SEEK_POSITION);

        } else if (getArguments() != null) {
            mBakingStep = getArguments().getParcelable(BakingStep.BAKING_DATA);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_step_description, container, false);
        populateCurrentBakingStep(mBakingStep);
        if (!UtilsNetworkConnection.checkInternetConnection(ApplicationBakingStory.getInstance().getApplicationContext())) {
            mBinding.layoutEmptyView.emptyViewRoot.setVisibility(View.VISIBLE);
        }
        return mBinding.getRoot();
    }

    private void populateCurrentBakingStep(BakingStep bakingStep) {

        if (bakingStep == null) {
            return;
        }

        if (TextUtils.isEmpty(bakingStep.getVideoURL())) {
            mBinding.videoContainer.setVisibility(View.GONE);
        }
        if (bakingStep.getShortDescription() != null && !bakingStep.getShortDescription().isEmpty()) {
            String step;
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


    private SimpleExoPlayer mExoPlayer;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        releasePlayer();
        outState.putParcelable(BakingStep.BAKING_DATA, mBakingStep);
        outState.putLong(SEEK_POSITION, mSeekPosition);
        outState.putBoolean(PLAY_WHEN_READY, mPlayWhenReady);
        outState.putInt(CURRENT_WINDOW, mCurrentWindow);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (!isVisibleToUser) {
            releasePlayer();
        } else {
            if (UtilsNetworkConnection.checkInternetConnection(ApplicationBakingStory.getInstance().getApplicationContext())) {
                if (mBakingStep != null) {
                    initializePlayer(mBakingStep.getVideoURL());
                }
            } else {
                releasePlayer();
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

        if (getActivity() == null || mediaUri == null || mediaUri.isEmpty()) {

            return;
        }
        ComponentListener componentListener = new ComponentListener();
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource
                .Factory(new DefaultDataSourceFactory(getActivity(), userAgent))
                .createMediaSource(Uri.parse(mediaUri));


        TrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity(), null);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);


        mExoPlayer.prepare(mediaSource, false, true);

        mExoPlayer.setPlayWhenReady(mPlayWhenReady);
        mExoPlayer.seekTo(mCurrentWindow, mSeekPosition);

        mExoPlayer.addListener(componentListener);

        mBinding.videoPlayerView.setPlayer(mExoPlayer);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Configuration configuration) {
        /* Do something */
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && getUserVisibleHint()) {
            releasePlayer();
            showFullScreenDialog(new PlayerState(mSeekPosition, mCurrentWindow, mPlayWhenReady));
        } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT && getUserVisibleHint()) {
            hideFullScreenDialog();
        }
    }

    private void showFullScreenDialog(PlayerState playerState) {
        FullscreenVideoDialog dialog = (FullscreenVideoDialog) getChildFragmentManager().findFragmentByTag(FullscreenVideoDialog.TAG);
        if (dialog == null) {
            dialog = FullscreenVideoDialog.newInstance(mBakingStep, playerState);
        } else {
            dialog.refreshData(mBakingStep, playerState);
        }

        dialog.setListenerDialogActions(this);
        if (!dialog.isAdded() && !dialog.isVisible()) {
            dialog.show(getChildFragmentManager(), FullscreenVideoDialog.TAG);
        }

    }


    private void hideFullScreenDialog() {

        FullscreenVideoDialog dialog = (FullscreenVideoDialog) getChildFragmentManager().findFragmentByTag(FullscreenVideoDialog.TAG);
        if (dialog != null && dialog.isVisible()) {
            dialog.dismissAllowingStateLoss();
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mSeekPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    public void updatePlayerState(long seekPosition, boolean playWhenReady, int window) {
        mSeekPosition = seekPosition;
        mPlayWhenReady = playWhenReady;
        mCurrentWindow = window;
    }

    @Override
    public void onDialogDismiss(PlayerState playerState) {
        // TODO: 5/15/18 Update player state when dismissed.
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
