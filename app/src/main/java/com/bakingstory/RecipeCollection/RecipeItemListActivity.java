package com.bakingstory.RecipeCollection;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.bakingstory.RecipeDetails.FragmentRecipeDetails;
import com.bakingstory.R;
import com.bakingstory.RecipeDetails.RecipeItemDetailActivity;
import com.bakingstory.databinding.ActivityRecipesListBinding;
import com.bakingstory.entities.Recipe;
import com.bakingstory.utils.HelperIdlingResource;

import java.util.List;

/**
 * An activity representing a list of Recepies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeItemListActivity extends AppCompatActivity implements ContractRecipes.View, AdapterRecipes.IRecipeInteraction {

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

        if (findViewById(R.id.fl_recipe_item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        PresenterRecipes presenterRecipes = new PresenterRecipes(this, this);
        presenterRecipes.requestRecipes(mIdlingResource);

    }

    private void setupRecyclerView(List<Recipe> recipeList) {
        mBinding.layoutRecipeCollection.rvRecipeItemList.setAdapter(new AdapterRecipes(this, recipeList));
    }


    @Override
    public void onRecipeSelection(Recipe recipe, View view) {
        if (mTwoPane) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_recipe_item_detail_container, FragmentRecipeDetails.newInstance(recipe))
                    .commit();
        } else {
            startActivity(RecipeItemDetailActivity.newInstance(this, recipe));
        }
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
