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


    public static Intent newInstance(Context context, Recipe recipeData) {
        Intent intent = new Intent(context, ActivityBakingStepDetails.class);
        intent.putExtra(Recipe.RECIPE_DATA, recipeData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_baking_step_details);

        setSupportActionBar(mBinding.detailToolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            if (getIntent().hasExtra(Recipe.RECIPE_DATA)) {
                Recipe recipeData = getIntent().getParcelableExtra(Recipe.RECIPE_DATA);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fl_recipe_item_detail_container,
                                FragmentRecipeDetails.newInstance(recipeData))
                        .commit();
            } else {
                return;
            }
        }
    }

}
