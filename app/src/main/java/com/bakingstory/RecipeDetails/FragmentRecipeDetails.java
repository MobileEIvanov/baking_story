package com.bakingstory.RecipeDetails;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakingstory.R;
import com.bakingstory.RecipeCollection.ActivityRecipesList;
import com.bakingstory.RecipeDetails.BakingSteps.ViewPagerAdapterBakingSteps;
import com.bakingstory.RecipeDetails.Ingredients.AdapterIngredients;
import com.bakingstory.databinding.ContentRecipeDetailsBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;

/**
 * A fragment representing a single RecepieItem detail screen.
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

    }

    /**
     * Initialize Recipe data
     */
    private void initData() {
        if (mRecipeData == null) {
            return;
        }

        initSteps();

        if (mRecipeData.getName() != null && !mRecipeData.getName().isEmpty()) {
            mBinding.tvRecipeTitle.setText(mRecipeData.getName());
        }

        if (mRecipeData.getServings() != 0) {
            mBinding.tvServings.setText(String.format(getString(R.string.text_servings), mRecipeData.getServings()));
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


    /**
     * Initialize the Baking steps view pager
     */
    private void initSteps() {
        if (mRecipeData.getSteps() == null || mRecipeData.getSteps().size() == 0) {
            return;
        }
        mBinding.vpBakingSteps.setAdapter(new ViewPagerAdapterBakingSteps(getChildFragmentManager(), getActivity(), mRecipeData.getSteps()));
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

}


