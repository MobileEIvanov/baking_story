package com.bakingstory.RecipeDetails;

import android.app.Activity;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bakingstory.R;
import com.bakingstory.RecipeCollection.RecipeItemListActivity;
import com.bakingstory.databinding.ContentRecipeDetailsBinding;
import com.bakingstory.databinding.LayoutStepDescriptionBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
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

    private Recipe mRecipeData;

    ContentRecipeDetailsBinding mBinding;
    LayoutStepDescriptionBinding mStepsBinding;
    private BottomSheetBehavior mBottomSheetBehavior;
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
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                // TODO: 4/19/18 Animate icon
            } else {
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

            Activity activity = this.getActivity();
            Toolbar appBarLayout = (Toolbar) activity.findViewById(R.id.detail_toolbar);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipeData.getName());
            }
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
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        if (mRecipeData == null) {
            return;
        }

        mBinding.layoutIngredients.tvHeaderIngredients.setOnClickListener(mListenerIngredients);
        initSteps();
        initBottomSheet();

    }

    private void initBottomSheet() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mBinding.layoutIngredients.root);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(110);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setPeekHeight(110);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });
    }

    private void initSteps() {


        // set ViewListener for custom view
        mBinding.cvStepsContent.setViewListener(viewListener);
        mBinding.cvStepsContent.setPageCount(mRecipeData.getSteps().size());
        mBinding.cvStepsContent.setSlideInterval(36000000);
    }

    public void populateCurrentBakingStep(BakingStep bakingStep) {
        if (mExoPlayer != null) {
            releasePlayer();
        }

        if (bakingStep.getVideoURL() != null && !bakingStep.getVideoURL().isEmpty()) {
            initializePlayer(Uri.parse(bakingStep.getVideoURL()));
        }

        if (bakingStep.getDescription() != null && !bakingStep.getDescription().isEmpty()) {
            mStepsBinding.tvStepDescription.setText(bakingStep.getDescription());
        }

        if (mRecipeData.getServings() != 0) {
            mBinding.tvServings.setText("People to make happy: " + mRecipeData.getServings());
        }
    }

    ExoPlayer mExoPlayer;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    /**
     * Initialize ExoPlayer.
     *
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
        String userAgent = Util.getUserAgent(getActivity(), "BakingStory");
        TrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getActivity(), userAgent)).createMediaSource(mediaUri);

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity(), null);

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.prepare(mediaSource);

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


