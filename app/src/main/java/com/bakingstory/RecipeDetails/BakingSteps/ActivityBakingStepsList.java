package com.bakingstory.RecipeDetails.BakingSteps;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bakingstory.R;
import com.bakingstory.RecipeDetails.FragmentRecipeDetails;
import com.bakingstory.RecipeDetails.ActivityBakingStepDetails;
import com.bakingstory.databinding.ActivityBakingStepsContentBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;
import com.bakingstory.utils.HelperIdlingResource;

import java.util.List;

/**
 * An activity representing a list of Recepies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ActivityBakingStepDetails} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ActivityBakingStepsList extends AppCompatActivity implements AdapterBakingSteps.IBakingStepsInteraction, FragmentRecipeDetails.IBakingStepChanged {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    ActivityBakingStepsContentBinding mBinding;
    private HelperIdlingResource mIdlingResource;
    private Recipe mRecipeData;
    private AdapterBakingSteps mAdapterListBakingSteps;

    public static Intent newInstance(Context context, Recipe recipeData) {
        Intent intent = new Intent(context, ActivityBakingStepsList.class);
        intent.putExtra(Recipe.RECIPE_DATA, recipeData);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_baking_steps_content);


        if (savedInstanceState == null) {
            mRecipeData = getIntent().getParcelableExtra(Recipe.RECIPE_DATA);
        }
        if (findViewById(R.id.fl_baking_steps_item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

        }
        setupRecyclerView(mRecipeData.getSteps());
        initToolbar();
        initHeader();
    }


    private void initHeader() {
//        if (mRecipeData.getName() != null && !mRecipeData.getName().isEmpty()) {
//            mBinding.layoutBakingStepsCollection.layoutHeaderRecipe.tvRecipeTitle.setText(mRecipeData.getName());
//        }

        if (mRecipeData.getServings() != 0) {
            mBinding.layoutBakingStepsCollection.layoutHeaderRecipe.tvServings.setText(String.format(getString(R.string.text_servings), mRecipeData.getServings()));
        }

    }

    private void setupRecyclerView(List<BakingStep> bakingStepList) {
        if (bakingStepList == null || bakingStepList.size() == 0) {
            finish();
            return;
        }

        if (mTwoPane) {
            bakingStepList.get(0).setSelected(true);
            onBakingStepSelection(0);
        }
        mAdapterListBakingSteps = new AdapterBakingSteps(this, bakingStepList, this);
        mBinding.layoutBakingStepsCollection.rvBakingSteps
                .setAdapter(mAdapterListBakingSteps);
    }

    private void initToolbar() {

        mBinding.toolbar.setTitle(mRecipeData.getName());
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBakingStepSelection(int position) {
        if (mTwoPane) {

            showFragmentRecipeDetails(position);

        } else {
            startActivity(ActivityBakingStepDetails.newInstance(this, mRecipeData));
        }
    }

    private void showFragmentRecipeDetails(int stepIndex) {
        FragmentRecipeDetails fragmentRecipeDetails = (FragmentRecipeDetails) getSupportFragmentManager().findFragmentByTag(FragmentRecipeDetails.TAG);

        if (fragmentRecipeDetails == null) {
            fragmentRecipeDetails = FragmentRecipeDetails.newInstance(mRecipeData);
        }

        fragmentRecipeDetails.setListenerBakingStepChanged(this);
        if (!fragmentRecipeDetails.isAdded() && !fragmentRecipeDetails.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_baking_steps_item_detail_container, fragmentRecipeDetails, FragmentRecipeDetails.TAG)
                    .commit();
        } else {
            fragmentRecipeDetails.navigateToStep(stepIndex);
        }
    }

    @Override
    public void onBakingPageChanged(int pageIndex) {
        if (mAdapterListBakingSteps != null) {
            mAdapterListBakingSteps.selectBakingStep(pageIndex);
        }
    }

    /**
     * Only called from test, creates and returns a new {@link HelperIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new HelperIdlingResource();
        }
        return mIdlingResource;
    }


}
