package com.bakingstory.RecipeCollection;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;

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
    private HelperIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipes_list);

        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setTitle(getTitle());

        PresenterRecipes presenterRecipes = new PresenterRecipes(this, this);
        if (UtilsNetworkConnection.checkInternetConnection(this)) {
            presenterRecipes.requestRecipes(mIdlingResource);
        }else{
            Snackbar.make(mBinding.getRoot(), R.string.error_connection_message, Snackbar.LENGTH_LONG).show();
        }

    }

    private void setupRecyclerView(List<Recipe> recipeList) {

        mBinding.layoutRecipeCollection.rvRecipeItemList
                .setAdapter(new AdapterRecipes(this, recipeList, this));
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
        Snackbar.make(mBinding.getRoot(), "Failed to retrieve list", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
