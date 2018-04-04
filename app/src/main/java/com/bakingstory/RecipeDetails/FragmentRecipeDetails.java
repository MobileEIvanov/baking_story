package com.bakingstory.RecipeDetails;

import android.app.Activity;
import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bakingstory.R;
import com.bakingstory.RecipeCollection.RecipeItemListActivity;
import com.bakingstory.databinding.FragmentRecipeDetailsBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;

/**
 * A fragment representing a single RecepieItem detail screen.
 * This fragment is either contained in a {@link RecipeItemListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeItemDetailActivity}
 * on handsets.
 */
public class FragmentRecipeDetails extends Fragment implements AdapterBakingSteps.IStepsInteraction {

    public static final String TAG = "Recipe Details";

    private Recipe mRecipeData;

    FragmentRecipeDetailsBinding mBinding;

    public static FragmentRecipeDetails newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putParcelable(Recipe.RECIPE_DATA, recipe);
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

        if (getArguments().containsKey(Recipe.RECIPE_DATA)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mRecipeData = getArguments().getParcelable(Recipe.RECIPE_DATA);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipeData.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_details, container, false);

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
        initSteps();
    }


    private void initSteps() {
        mBinding.rvRecipeSteps.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mBinding.rvRecipeSteps.setAdapter(new AdapterBakingSteps(mRecipeData.getSteps(), this));
    }

    private void initVideo() {
        // TODO: 4/4/18 Initialise the player and video
    }

    private void refreshContent(BakingStep bakingStep){

    }


    @Override
    public void onBakingStepSelected(Object bakingStep) {
    }
}


