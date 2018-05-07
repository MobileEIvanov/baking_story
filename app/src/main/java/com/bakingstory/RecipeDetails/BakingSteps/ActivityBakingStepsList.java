package com.bakingstory.RecipeDetails.BakingSteps;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

import com.bakingstory.R;
import com.bakingstory.RecipeDetails.FragmentRecipeDetails;
import com.bakingstory.RecipeDetails.ActivityBakingStepDetails;
import com.bakingstory.databinding.ActivityBakingStepsContentBinding;
import com.bakingstory.databinding.ActivityRecipesListBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;
import com.bakingstory.utils.HelperIdlingResource;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Recepies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ActivityBakingStepDetails} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ActivityBakingStepsList extends AppCompatActivity implements AdapterBakingSteps.IBakingStepsInteraction {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    ActivityBakingStepsContentBinding mBinding;
    private HelperIdlingResource mIdlingResource;
    private Recipe mRecipeData;

    public static Intent newInstance(Context context, Recipe recipeData) {
        Intent intent = new Intent(context, ActivityBakingStepsList.class);
        intent.putExtra(Recipe.RECIPE_DATA, recipeData);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_baking_steps_content);

        mRecipeData = getIntent().getParcelableExtra(Recipe.RECIPE_DATA);
        setupRecyclerView(mRecipeData.getSteps());


        initToolbar();

        if (findViewById(R.id.fl_baking_steps_item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(List<BakingStep> bakingStepList) {
        if (bakingStepList == null || bakingStepList.size() == 0) {
            finish();
            return;
        }

        if (mTwoPane) {
            bakingStepList.get(0).setSelected(true);
            onBakingStepSelection();
        }
        mBinding.layoutBakingStepsCollection.rvBakingSteps
                .setAdapter(new AdapterBakingSteps(this, bakingStepList, this));
    }

    private void initToolbar() {

        mBinding.toolbar.setTitle(mRecipeData.getName());
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBakingStepSelection() {
        if (mTwoPane) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_baking_steps_item_detail_container, FragmentRecipeDetails.newInstance(mRecipeData))
                    .commit();
        } else {
            startActivity(ActivityBakingStepDetails.newInstance(this, mRecipeData));
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
