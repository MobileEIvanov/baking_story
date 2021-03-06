package com.bakingstory.recipe_details.baking_steps;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bakingstory.R;
import com.bakingstory.recipe_details.FragmentRecipeDetails;
import com.bakingstory.recipe_details.ActivityBakingStepDetails;
import com.bakingstory.databinding.ActivityBakingStepsContentBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;

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
    private ActivityBakingStepsContentBinding mBinding;

    private Recipe mRecipeData;
    private AdapterBakingSteps mAdapterListBakingSteps;
    private int mCurrentSelectionIndex = 0;

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
        } else {
            mRecipeData = savedInstanceState.getParcelable(Recipe.RECIPE_DATA);
            mCurrentSelectionIndex = savedInstanceState.getInt(BakingStep.BAKING_DATA);
        }

        if (mRecipeData == null) {
            finish();
            return;
        }

        mTwoPane = getResources().getBoolean(R.bool.isTablet);

        setupRecyclerView(mRecipeData.getSteps());
        initToolbar();
        initHeader();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Recipe.RECIPE_DATA, mRecipeData);
        outState.putInt(BakingStep.BAKING_DATA, mCurrentSelectionIndex);
        super.onSaveInstanceState(outState);
    }

    private void initHeader() {
        if (!mTwoPane) {

            if (mRecipeData.getServings() != 0) {
                mBinding.layoutBakingStepsCollection.layoutHeaderRecipe.tvServings.setText(String.format(getString(R.string.text_servings), mRecipeData.getServings()));
            }
        } else {
            mBinding.layoutBakingStepsCollection.layoutHeaderRecipe.tvServings.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView(List<BakingStep> bakingStepList) {
        if (bakingStepList == null || bakingStepList.size() == 0) {
            finish();
            return;
        }

        if (mTwoPane) {
            bakingStepList.get(mCurrentSelectionIndex).setSelected(true);
            onBakingStepSelection(mCurrentSelectionIndex);
        }
        mAdapterListBakingSteps = new AdapterBakingSteps(this, bakingStepList, this);
        mBinding.layoutBakingStepsCollection.rvBakingSteps
                .setAdapter(mAdapterListBakingSteps);
    }

    private void initToolbar() {
        String toolbarTitle;

        if (mTwoPane) {
            toolbarTitle = mRecipeData.getName() + " - " + String.format(getString(R.string.text_servings), mRecipeData.getServings());
        } else {
            toolbarTitle = mRecipeData.getName();
        }

        mBinding.toolbar.setTitle(toolbarTitle);
        setSupportActionBar(mBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBakingStepSelection(int position) {
        mCurrentSelectionIndex = position;
        if (mTwoPane) {
            showFragmentRecipeDetails(mCurrentSelectionIndex);
        } else {
            startActivity(ActivityBakingStepDetails.newInstance(this, mRecipeData, mCurrentSelectionIndex));
        }
    }

    private void showFragmentRecipeDetails(int stepIndex) {
        FragmentRecipeDetails fragmentRecipeDetails = (FragmentRecipeDetails) getSupportFragmentManager().findFragmentByTag(FragmentRecipeDetails.TAG);

        if (fragmentRecipeDetails == null) {
            fragmentRecipeDetails = FragmentRecipeDetails.newInstance(mRecipeData, mCurrentSelectionIndex);
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


}
