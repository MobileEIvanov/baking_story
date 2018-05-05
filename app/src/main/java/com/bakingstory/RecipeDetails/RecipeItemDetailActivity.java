package com.bakingstory.RecipeDetails;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;

import com.bakingstory.R;
import com.bakingstory.RecipeCollection.RecipeItemListActivity;
import com.bakingstory.databinding.ActivityRecipeItemDetailBinding;
import com.bakingstory.entities.Recipe;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * An activity representing a single RecepieItem detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeItemListActivity}.
 */
public class RecipeItemDetailActivity extends AppCompatActivity {

//    https://pngtree.com/pay?pay_ref=
    ActivityRecipeItemDetailBinding mBinding;


    public static Intent newInstance(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeItemDetailActivity.class);
        intent.putExtra(Recipe.RECIPE_DATA,recipe);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_item_detail);


        setSupportActionBar(mBinding.detailToolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            if (getIntent().hasExtra(Recipe.RECIPE_DATA)) {
                Recipe recipe = getIntent().getParcelableExtra(Recipe.RECIPE_DATA);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fl_recipe_item_detail_container,
                                FragmentRecipeDetails.newInstance(recipe))
                        .commit();
            } else {
                return;
            }

        }
    }


}
