package com.bakingstory.RecipeDetails;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakingstory.R;
import com.bakingstory.RecipeCollection.RecipeItemListActivity;
import com.bakingstory.RecipeDetails.Ingredients.AdapterIngredients;
import com.bakingstory.databinding.ContentRecipeDetailsBinding;
import com.bakingstory.databinding.LayoutStepDescriptionBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;
import com.bakingstory.utils.UtilsAnimations;
import com.bakingstory.utils.UtilsNetworkConnection;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.synnapps.carouselview.ViewListener;

/**
 * A fragment representing a single RecepieItem detail screen.
 * This fragment is either contained in a {@link RecipeItemListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeItemDetailActivity}
 * on handsets.
 */
public class FragmentRecipeDetails extends Fragment {

    public static final String TAG = "Recipe Details";

    private static final int ANIMATION_DURATION = 200;
    private static final int ANIMATION_START_ANGLE = 0;
    private static final int ANIMATION_END_ANGLE = 180;

    private static final int SLIDER_INTERVAL = 60 * 60 * 1000;

    private Recipe mRecipeData;
    private ContentRecipeDetailsBinding mBinding;
    private LayoutStepDescriptionBinding mStepsBinding;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ExoPlayer mExoPlayer;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();


    // TODO: 4/16/18 Seee this one https://github.com/GIGAMOLE

    ViewListener viewListener = new ViewListener() {

        @Override
        public View setViewForPosition(int position) {
            mStepsBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.layout_step_description, null, false);
            //set view attributes here

            populateCurrentBakingStep(mRecipeData.getSteps().get(position));

            return mStepsBinding.getRoot();
        }
    };


    View.OnClickListener mListenerIngredients = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                mBinding.layoutIngredients.btnToggleIngredients.animate().setDuration(ANIMATION_DURATION).rotation(ANIMATION_END_ANGLE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                // TODO: 4/19/18 Animate icon
            } else {
                mBinding.layoutIngredients.btnToggleIngredients.animate().setDuration(ANIMATION_DURATION).rotation(ANIMATION_START_ANGLE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


            }
        }
    };

    public static FragmentRecipeDetails newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putParcelable(Recipe.RECIPE_DATA, recipe);
        FragmentRecipeDetails fragmentRecipeDetails = new FragmentRecipeDetails();
        fragmentRecipeDetails.setArguments(args);

        return fragmentRecipeDetails;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentRecipeDetails() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Recipe.RECIPE_DATA)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mRecipeData = getArguments().getParcelable(Recipe.RECIPE_DATA);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.content_recipe_details, container, false);
        initData();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initSteps();
    }

    private void initData() {
        if (mRecipeData == null) {
            return;
        }

        if (mRecipeData.getName() != null && !mRecipeData.getName().isEmpty()) {
            mBinding.tvRecipeTitle.setText(mRecipeData.getName());
        }


        if (mRecipeData.getIngredients() != null && mRecipeData.getIngredients().size() > 0) {
            mBinding.layoutIngredients.root.setVisibility(View.VISIBLE);
            mBinding.layoutIngredients.tvHeaderIngredients.setOnClickListener(mListenerIngredients);
            mBinding.layoutIngredients.btnToggleIngredients.setOnClickListener(mListenerIngredients);
            initBottomSheet();
        } else {
            mBinding.layoutIngredients.root.setVisibility(View.INVISIBLE);
        }

    }

    private void initBottomSheet() {
        if (mRecipeData.getIngredients() == null) {
            return;
        }

        mBinding.layoutIngredients.rvIngredientsList.setAdapter(new AdapterIngredients(mRecipeData.getIngredients()));
        mBottomSheetBehavior = BottomSheetBehavior.from(mBinding.layoutIngredients.root);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(mBinding.layoutIngredients.tvHeaderIngredients.getMinimumHeight());
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBinding.layoutIngredients.btnToggleIngredients.animate().rotation(0);
                    mBottomSheetBehavior.setPeekHeight(mBinding.layoutIngredients.tvHeaderIngredients.getMinimumHeight());
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
                if (slideOffset > 0) {
                    mBinding.layoutIngredients.btnToggleIngredients.animate().setDuration(ANIMATION_DURATION).rotation(ANIMATION_END_ANGLE);
                }
            }
        });
    }

    /**
     * Initialize the steps carousel view with slide interval of 1 min
     * {@link #SLIDER_INTERVAL}
     */


    private void initSteps() {
        if (mRecipeData == null) {
            return;
        }
        // set ViewListener for custom view
        mBinding.cvStepsContent.setViewListener(viewListener);
        mBinding.cvStepsContent.setPageCount(mRecipeData.getSteps().size());
        mBinding.cvStepsContent.setSlideInterval(SLIDER_INTERVAL);
    }

    public void populateCurrentBakingStep(BakingStep bakingStep) {
        if (mExoPlayer != null) {
            releasePlayer();
        }

        if (UtilsNetworkConnection.checkInternetConnection(getActivity())) {
            if (bakingStep.getVideoURL() != null && !bakingStep.getVideoURL().isEmpty()) {
                initializePlayer(Uri.parse(bakingStep.getVideoURL()));
            } else {
                mStepsBinding.videoContainer.setVisibility(View.GONE);
            }
        } else {
            mStepsBinding.videoPlayerView.setVisibility(View.INVISIBLE);
            mStepsBinding.rlVideoNotAvailable.setVisibility(View.VISIBLE);
            UtilsAnimations.createCircularReveal(mStepsBinding.rlVideoNotAvailable);
        }


        if (bakingStep.getShortDescription() != null && !bakingStep.getShortDescription().isEmpty()) {
            String step = null;
            if (bakingStep.getId() > 0) {

                step = String.format(getString(R.string.text_step_description), bakingStep.getId(), bakingStep.getShortDescription());
            } else {
                step = bakingStep.getShortDescription();
            }
            mStepsBinding.tvStepShortDescription.setText(step);
        }

        if (bakingStep.getDescription() != null && !bakingStep.getDescription().isEmpty() && bakingStep.getId() > 0) {
            mStepsBinding.tvStepDescription.setText(bakingStep.getDescription());
        }

        if (mRecipeData.getServings() != 0) {
            mBinding.tvServings.setText(String.format(getString(R.string.text_servings), mRecipeData.getServings()));
        }
    }


    /**
     * Initialize ExoPlayer.
     * https://codelabs.developers.google.com/codelabs/exoplayer-intro/#5
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
//        if (mExoPlayer == null) {
//            // Create an instance of the ExoPlayer.
//            TrackSelector trackSelector = new DefaultTrackSelector();
//            LoadControl loadControl = new DefaultLoadControl();
//            mExoPlayer = ExoPlayerFactory.newSimpleInstance(,trackSelector,loadControl);
//            mBinding.videoPlayerView.setPlayer(mExoPlayer);
//            // Prepare the MediaSource.
//            String userAgent = Util.getUserAgent(getActivity(), "BakingStory");
//            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getActivity(), userAgent)).createMediaSource(mediaUri);
//            mExoPlayer.prepare(mediaSource);
//            mExoPlayer.setPlayWhenReady(true);
//        }
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
        TrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getActivity(), userAgent)).createMediaSource(mediaUri);

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity(), null);


        mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
        mExoPlayer.setPlayWhenReady(false);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        if (mStepsBinding != null) {
            mStepsBinding.videoPlayerView.setPlayer(mExoPlayer);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

}


