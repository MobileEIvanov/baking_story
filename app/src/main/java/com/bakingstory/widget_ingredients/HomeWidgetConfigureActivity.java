package com.bakingstory.widget_ingredients;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bakingstory.R;
import com.bakingstory.recipes_collection.AdapterRecipes;
import com.bakingstory.recipes_collection.ContractRecipes;
import com.bakingstory.recipes_collection.PresenterRecipes;
import com.bakingstory.databinding.ActivityRecipesListBinding;
import com.bakingstory.entities.Recipe;
import com.bakingstory.utils.UtilsNetworkConnection;

import java.util.List;

import static com.bakingstory.widget_ingredients.PreferencesManager.saveLastWidgetIdPref;
import static com.bakingstory.widget_ingredients.PreferencesManager.saveSelectedIngredientsSteps;
import static com.bakingstory.widget_ingredients.PreferencesManager.saveSelectedRecipe;
import static com.bakingstory.widget_ingredients.PreferencesManager.saveTitlePref;

/**
 * The configuration screen for the {@link HomeWidget HomeWidget} AppWidget.
 * <p>
 * Credit to store list in preferences:
 * https://freakycoder.com/android-notes-40-how-to-save-and-get-arraylist-into-sharedpreference-7d1f044bc79a
 */
public class HomeWidgetConfigureActivity extends AppCompatActivity implements ContractRecipes.View, AdapterRecipes.IRecipeInteraction {

    private final static String WIDGET_IDLE_REQUEST = "Widget request";

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private ActivityRecipesListBinding mBinding;
    private PresenterRecipes mPresenter;

    public HomeWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        setTheme(R.style.AppTheme);
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.home_widget_configure);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipes_list);

        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setTitle(R.string.title_baking_story_recipes);
        mPresenter = new PresenterRecipes(this);
        requestRecipes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    private void requestRecipes() {
        if (UtilsNetworkConnection.checkInternetConnection(this)) {
            mPresenter.requestRecipes(new CountingIdlingResource(WIDGET_IDLE_REQUEST));
        } else {
            showErrorView();
        }
    }


    @Override
    public void onRecipeSelection(Recipe recipe) {
        final Context context = HomeWidgetConfigureActivity.this;

        // When the button is clicked, store the string locally
        saveTitlePref(context, mAppWidgetId, recipe.getName());
        saveLastWidgetIdPref(context, mAppWidgetId);
        saveSelectedIngredientsSteps(this, recipe.getIngredients());
        saveSelectedRecipe(this, recipe);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        HomeWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public void showRecipesList(List<Recipe> recipesList) {

        AdapterRecipes mAdapter = new AdapterRecipes(this, recipesList, this);
        mBinding.layoutRecipeCollection.rvRecipeItemList.setAdapter(mAdapter);
        mBinding.toolbar.setTitle(R.string.title_baking_story_recipes);


        mBinding.layoutEmptyView.getRoot().setVisibility(View.INVISIBLE);
        mBinding.layoutRecipeCollection.getRoot().setVisibility(View.VISIBLE);


        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
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
}

