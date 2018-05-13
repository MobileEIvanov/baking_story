package com.bakingstory.recipe_details;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bakingstory.R;
import com.bakingstory.recipes_collection.ActivityRecipesList;
import com.bakingstory.recipe_details.baking_steps.FullscreenVideoDialog;
import com.bakingstory.databinding.ActivityBakingStepDetailsBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;

/**
 * An activity representing a single RecipeItem detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ActivityRecipesList}.
 */
public class ActivityBakingStepDetails extends AppCompatActivity
        implements FragmentRecipeDetails.IBakingStepChanged,
        FullscreenVideoDialog.IDialogInteractions {

    //    https://pngtree.com/pay?pay_ref=
    private ActivityBakingStepDetailsBinding mBinding;
    private int mCurrentSelectedStep = 0;

    public static Intent newInstance(Context context, Recipe recipeData, int bakingStepPosition) {
        Intent intent = new Intent(context, ActivityBakingStepDetails.class);
        intent.putExtra(Recipe.RECIPE_DATA, recipeData);
        intent.putExtra(BakingStep.BAKING_DATA, bakingStepPosition);
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


        if (savedInstanceState == null) {
            if (getIntent().hasExtra(Recipe.RECIPE_DATA)) {
                mRecipeData = getIntent().getParcelableExtra(Recipe.RECIPE_DATA);
                mCurrentSelectedStep = getIntent().getIntExtra(BakingStep.BAKING_DATA, 0);
                mBakingStep = mRecipeData.getSteps().get(mCurrentSelectedStep);
            } else {
                return;
            }
        } else {
            mRecipeData = savedInstanceState.getParcelable(Recipe.RECIPE_DATA);
            mCurrentSelectedStep = savedInstanceState.getInt(BakingStep.BAKING_DATA);
            mBakingStep = mRecipeData.getSteps().get(mCurrentSelectedStep);

        }
        initToolbar();
        displayContentBasedOnOrientation();
    }

    private void initToolbar() {
        mBinding.detailToolbar.setTitle(mRecipeData.getName());
        setSupportActionBar(mBinding.detailToolbar);
        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void displayContentBasedOnOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            removeRecipeDetails();
            showFullScreenDialog();
        } else {
            hideFullScreenDialog();
            showRecipeDetails();
        }
    }

    private void showRecipeDetails() {
        FragmentRecipeDetails fragmentRecipeDetails = (FragmentRecipeDetails) getSupportFragmentManager()
                .findFragmentByTag(FragmentRecipeDetails.TAG);

        if (fragmentRecipeDetails == null) {
            fragmentRecipeDetails = FragmentRecipeDetails.newInstance(mRecipeData, mCurrentSelectedStep);
        }
        fragmentRecipeDetails.setListenerBakingStepChanged(this);
        if (!fragmentRecipeDetails.isAdded() && !fragmentRecipeDetails.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_recipe_item_detail_container, fragmentRecipeDetails, FragmentRecipeDetails.TAG)
                    .commit();
        }
    }

    private void removeRecipeDetails() {
        FragmentRecipeDetails fragmentRecipeDetails = (FragmentRecipeDetails) getSupportFragmentManager().findFragmentByTag(FragmentRecipeDetails.TAG);
        if (fragmentRecipeDetails == null) {
            return;
        }

        if (fragmentRecipeDetails.isAdded() && fragmentRecipeDetails.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragmentRecipeDetails)
                    .commit();
        }
    }

    private void showFullScreenDialog() {
        FullscreenVideoDialog dialog = (FullscreenVideoDialog) getSupportFragmentManager().findFragmentByTag(FullscreenVideoDialog.TAG);
        if (dialog == null) {
            dialog = FullscreenVideoDialog.newInstance(mBakingStep);
        } else {
            dialog.refreshData(mBakingStep);
        }

        dialog.setListenerDialogActions(this);
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
        outState.putInt(BakingStep.BAKING_DATA, mCurrentSelectedStep);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onBakingPageChanged(int pageIndex) {
        mCurrentSelectedStep = pageIndex;
        mBakingStep = mRecipeData.getSteps().get(mCurrentSelectedStep);
    }

    @Override
    public void onDialogDismiss() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onBackPressed();
        }
    }
}
