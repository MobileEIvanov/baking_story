package com.bakingstory.RecipeDetails;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;

import com.bakingstory.R;
import com.bakingstory.RecipeCollection.ActivityRecipesList;
import com.bakingstory.RecipeDetails.BakingSteps.FullscreenVideoDialog;
import com.bakingstory.databinding.ActivityBakingStepDetailsBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;

/**
 * An activity representing a single RecepieItem detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ActivityRecipesList}.
 */
public class ActivityBakingStepDetails extends AppCompatActivity {

    //    https://pngtree.com/pay?pay_ref=
    ActivityBakingStepDetailsBinding mBinding;


    public static Intent newInstance(Context context, Recipe recipeData, BakingStep bakingStep) {
        Intent intent = new Intent(context, ActivityBakingStepDetails.class);
        intent.putExtra(Recipe.RECIPE_DATA, recipeData);
        intent.putExtra(BakingStep.BAKING_DATA, bakingStep);
        return intent;
    }

    private Recipe mRecipeData;
    private BakingStep mBakingStep;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        displayContentBasedOnOrientation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_baking_step_details);

        initToolbar();

        if (savedInstanceState == null) {
            if (getIntent().hasExtra(Recipe.RECIPE_DATA)) {
                mRecipeData = getIntent().getParcelableExtra(Recipe.RECIPE_DATA);
                mBakingStep = getIntent().getParcelableExtra(BakingStep.BAKING_DATA);
            } else {
                return;
            }
        } else {
            mRecipeData = savedInstanceState.getParcelable(Recipe.RECIPE_DATA);
            mBakingStep = savedInstanceState.getParcelable(BakingStep.BAKING_DATA);
        }
        displayContentBasedOnOrientation();
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.detailToolbar);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void displayContentBasedOnOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showFullScreenDialog();
        } else {
            hideFullScreenDialog();
            showRecipeDetails();
        }
    }

    private void showRecipeDetails() {
        FragmentRecipeDetails fragmentRecipeDetails = (FragmentRecipeDetails) getSupportFragmentManager().findFragmentByTag(FragmentRecipeDetails.TAG);

        if (fragmentRecipeDetails == null) {
            fragmentRecipeDetails = FragmentRecipeDetails.newInstance(mRecipeData);
        }

        if (!fragmentRecipeDetails.isAdded() && !fragmentRecipeDetails.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_recipe_item_detail_container, fragmentRecipeDetails, FragmentRecipeDetails.TAG)
                    .commit();
        }
    }


    private void showFullScreenDialog() {
        FullscreenVideoDialog dialog = (FullscreenVideoDialog) getSupportFragmentManager().findFragmentByTag(FullscreenVideoDialog.TAG);
        if (dialog == null) {
            dialog = FullscreenVideoDialog.newInstance(mBakingStep);
        }
        if (!dialog.isAdded() && !dialog.isVisible()) {
            dialog.show(getSupportFragmentManager(), FullscreenVideoDialog.TAG);
        }

    }

    private void hideFullScreenDialog() {

        FullscreenVideoDialog dialog = (FullscreenVideoDialog) getSupportFragmentManager().findFragmentByTag(FullscreenVideoDialog.TAG);
        if (dialog != null && dialog.isVisible()) {
            dialog.dismissAllowingStateLoss();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Recipe.RECIPE_DATA, mRecipeData);
        outState.putParcelable(BakingStep.BAKING_DATA, mBakingStep);
        super.onSaveInstanceState(outState);
    }
}
