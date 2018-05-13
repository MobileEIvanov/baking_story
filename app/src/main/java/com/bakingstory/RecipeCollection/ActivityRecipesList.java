package com.bakingstory.RecipeCollection;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.bakingstory.RecipeDetails.BakingSteps.ActivityBakingStepsList;
import com.bakingstory.R;
import com.bakingstory.RecipeDetails.ActivityBakingStepDetails;
import com.bakingstory.databinding.ActivityRecipesListBinding;
import com.bakingstory.entities.Recipe;
import com.bakingstory.utils.HelperIdlingResource;
import com.bakingstory.utils.UtilsNetworkConnection;

import java.util.List;

/**
 * An activity representing a list of Recepies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ActivityBakingStepDetails} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ActivityRecipesList extends AppCompatActivity implements ContractRecipes.View,
        AdapterRecipes.IRecipeInteraction {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    ActivityRecipesListBinding mBinding;
    public static CountingIdlingResource mIdlingResources;
    PresenterRecipes mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipes_list);
        mBinding.toolbar.setTitle(R.string.title_baking_story_recipes);
        setSupportActionBar(mBinding.toolbar);


        mPresenter = new PresenterRecipes(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestRecipes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    private void requestRecipes() {
        if (UtilsNetworkConnection.checkInternetConnection(this)) {
            mPresenter.requestRecipes(mIdlingResources);
        } else {
            showErrorView();
        }

    }

    private void setupRecyclerView(List<Recipe> recipeList) {

        mBinding.layoutRecipeCollection.rvRecipeItemList
                .setAdapter(new AdapterRecipes(this, recipeList, this));
        mBinding.layoutEmptyView.getRoot().setVisibility(View.INVISIBLE);
        mBinding.layoutRecipeCollection.getRoot().setVisibility(View.VISIBLE);
    }


    @Override
    public void onRecipeSelection(Recipe recipe) {

        startActivity(ActivityBakingStepsList.newInstance(this, recipe));
    }

    @Override
    public void showRecipesList(List<Recipe> recipesList) {
        setupRecyclerView(recipesList);
    }

    @Override
    public void showErrorView() {

        mBinding.layoutEmptyView.getRoot().setVisibility(View.VISIBLE);
        Snackbar snackbar = Snackbar.make(mBinding.getRoot(), R.string.error_message_no_list, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.btn_retry, v -> {
            requestRecipes();
            snackbar.dismiss();
        }).show();
    }


    /**
     * Only called from test, creates and returns a new {@link HelperIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public CountingIdlingResource getIdlingResource() {
        if (mIdlingResources == null) {
            mIdlingResources = new CountingIdlingResource("RECIPES REQUEST");

        }
        return mIdlingResources;
    }
}
