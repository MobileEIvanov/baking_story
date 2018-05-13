package com.bakingstory.recipe_details;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakingstory.R;
import com.bakingstory.recipes_collection.ActivityRecipesList;
import com.bakingstory.recipe_details.baking_steps.ViewPagerAdapterBakingSteps;
import com.bakingstory.recipe_details.ingredients.AdapterIngredients;
import com.bakingstory.databinding.ContentRecipeDetailsBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;

/**
 * A fragment representing a single Recipe Item detail screen.
 * This fragment is either contained in a {@link ActivityRecipesList}
 * in two-pane mode (on tablets) or a {@link ActivityBakingStepDetails}
 * on handsets.
 * <p>
 * <p>
 * Credits: Page indicator view
 * https://github.com/romandanylyk/PageIndicatorView
 */
public class FragmentRecipeDetails extends Fragment {

    public static final String TAG = "Recipe Details";

    private static final int ANIMATION_DURATION = 200;
    private static final int ANIMATION_START_ANGLE = 0;
    private static final int ANIMATION_END_ANGLE = 180;

    private Recipe mRecipeData;
    private ContentRecipeDetailsBinding mBinding;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int mCurrentSelection = 0;

    private final View.OnClickListener mListenerIngredients = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                mBinding.layoutIngredients.btnToggleIngredients.animate().setDuration(ANIMATION_DURATION).rotation(ANIMATION_END_ANGLE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                mBinding.layoutIngredients.btnToggleIngredients.animate().setDuration(ANIMATION_DURATION).rotation(ANIMATION_START_ANGLE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    };

    public static FragmentRecipeDetails newInstance(Recipe recipe, int currentSelection) {
        Bundle args = new Bundle();
        args.putParcelable(Recipe.RECIPE_DATA, recipe);
        args.putInt(BakingStep.BAKING_DATA, currentSelection);
        FragmentRecipeDetails fragmentRecipeDetails = new FragmentRecipeDetails();
        fragmentRecipeDetails.setArguments(args);

        return fragmentRecipeDetails;
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
        setRetainInstance(true);

        if (savedInstanceState != null) {
            mRecipeData = savedInstanceState.getParcelable(Recipe.RECIPE_DATA);
            mCurrentSelection = savedInstanceState.getInt(BakingStep.BAKING_DATA);
        } else if (getArguments() != null && getArguments().containsKey(Recipe.RECIPE_DATA)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mRecipeData = getArguments().getParcelable(Recipe.RECIPE_DATA);
            mCurrentSelection = getArguments().getInt(BakingStep.BAKING_DATA);
        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.content_recipe_details, container, false);
        initData();
        return mBinding.getRoot();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(Recipe.RECIPE_DATA, mRecipeData);
        outState.putInt(BakingStep.BAKING_DATA, mCurrentSelection);
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialize Recipe data
     */
    private void initData() {
        if (mRecipeData == null) {
            return;
        }

        initSteps();

        if (mRecipeData.getIngredients() != null && mRecipeData.getIngredients().size() > 0) {
            mBinding.layoutIngredients.root.setVisibility(View.VISIBLE);
            mBinding.layoutIngredients.tvHeaderIngredients.setOnClickListener(mListenerIngredients);
            mBinding.layoutIngredients.btnToggleIngredients.setOnClickListener(mListenerIngredients);
            initBottomSheet();
        } else {
            mBinding.layoutIngredients.root.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * Initialize the Baking steps view pager
     */
    private void initSteps() {
        if (mRecipeData.getSteps() == null || mRecipeData.getSteps().size() == 0) {
            return;
        }
        mBinding.vpBakingSteps.setAdapter(new ViewPagerAdapterBakingSteps(getChildFragmentManager(), mRecipeData.getSteps()));
        navigateToStep(mCurrentSelection);

        mBinding.vpBakingSteps.addOnPageChangeListener(mListenerPageChanged);
    }

    private final ViewPager.OnPageChangeListener mListenerPageChanged = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mListenerBakingStepChanged != null) {
                mCurrentSelection = position;
                mListenerBakingStepChanged.onBakingPageChanged(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void setListenerBakingStepChanged(IBakingStepChanged listenerBakingStepChanged) {
        mListenerBakingStepChanged = listenerBakingStepChanged;
    }

    private IBakingStepChanged mListenerBakingStepChanged;

    /**
     * Tracks the user interactions with the ViewPager if the page changes
     * it will notify the listener in order to accurately update the ui.
     */
    public interface IBakingStepChanged {
        void onBakingPageChanged(int pageIndex);
    }

    /**
     * Initializes the Ingredients bottom sheet.
     */
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
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBinding.layoutIngredients.btnToggleIngredients.animate().rotation(0);
                    mBottomSheetBehavior.setPeekHeight(mBinding.layoutIngredients.tvHeaderIngredients.getMinimumHeight());
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0) {
                    mBinding.layoutIngredients.btnToggleIngredients.animate().setDuration(ANIMATION_DURATION).rotation(ANIMATION_END_ANGLE);
                }
            }
        });
    }


    public void navigateToStep(int stepIndex) {
        if (mBinding.vpBakingSteps.getCurrentItem() != stepIndex) {
            mBinding.vpBakingSteps.setCurrentItem(stepIndex, true);
        }
    }
}


