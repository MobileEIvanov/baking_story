package com.bakingstory.WidgetIngredients;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;

import com.bakingstory.R;
import com.bakingstory.RecipeCollection.AdapterRecipes;
import com.bakingstory.data.HelperJsonDataParser;
import com.bakingstory.entities.Ingredient;
import com.bakingstory.entities.Recipe;

import java.io.IOException;
import java.util.List;

/**
 * The configuration screen for the {@link HomeWidget HomeWidget} AppWidget.
 */
public class HomeWidgetConfigureActivity extends Activity implements AdapterRecipes.IRecipeInteraction {

    private static final String PREFS_NAME = "com.bakingstory.WidgetIngredients.HomeWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_LAST_WIDGET_KEY = "lass_widget_id";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    AdapterRecipes mAdapter;
    RecyclerView mListRecipes;


    public HomeWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveLastWidgetIdPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_LAST_WIDGET_KEY, appWidgetId);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadLastWidgetIdPref(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int widget = prefs.getInt(PREF_LAST_WIDGET_KEY, -1);
        return widget;
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.home_widget_configure);
        List<Recipe> recipeList = null;
        try {
            recipeList = HelperJsonDataParser.getAllRecepies(this);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        mAdapter = new AdapterRecipes(this, recipeList, this);
        mListRecipes = (RecyclerView) findViewById(R.id.rv_recipe_list);
        mListRecipes.setAdapter(mAdapter);

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
            return;
        }
    }

    @Override
    public void onRecipeSelection(Recipe recipe) {
        final Context context = HomeWidgetConfigureActivity.this;

        StringBuilder stringBuilder = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            stringBuilder.append(ingredient.getIngredient());
            stringBuilder.append("\n\n");
        }

        // When the button is clicked, store the string locally
        saveTitlePref(context, mAppWidgetId, recipe.getName());
        saveLastWidgetIdPref(context, mAppWidgetId);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        HomeWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

