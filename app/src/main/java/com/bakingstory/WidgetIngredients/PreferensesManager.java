package com.bakingstory.WidgetIngredients;

import android.content.Context;
import android.content.SharedPreferences;

import com.bakingstory.R;
import com.bakingstory.entities.Ingredient;
import com.bakingstory.entities.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by emil.ivanov on 5/13/18.
 */
public class PreferensesManager {


    private static final String PREFS_NAME = "com.bakingstory.WidgetIngredients.HomeWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_LAST_WIDGET_KEY = "lass_widget_id";
    private static final String PREF_SELECTED_INGREDIENTS = "selected_ingredients";
    private static final String PREF_SELECTED_RECIPE = "selected_recipe";
    private static final String PREF_RECIPE_ID = "recipe_id";

    public static void saveSelectedIngredientsSteps(Context context, List<Ingredient> bakingSteps) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        Gson gson = new Gson();
        String jsonIngredients = gson.toJson(bakingSteps);
        prefs.putString(PREF_SELECTED_INGREDIENTS, jsonIngredients);
        prefs.apply();
    }


    public static ArrayList<Ingredient> loadSelectedIngredientsSteps(Context context) {
        List<Ingredient> list;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
        String jsonList = sharedPreferences.getString(PREF_SELECTED_INGREDIENTS, null);

        if (sharedPreferences.contains(PREF_SELECTED_INGREDIENTS)) {

            Gson gson = new Gson();
            Ingredient[] ingredients = gson.fromJson(jsonList, Ingredient[].class);
            list = Arrays.asList(ingredients);
            list = new ArrayList<Ingredient>(list);

            return (ArrayList<Ingredient>) list;
        } else {
            return null;
        }

    }

    public static void saveSelectedRecipe(Context context, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        Gson gson = new Gson();
        String json = gson.toJson(recipe);
        prefs.putString(PREF_SELECTED_RECIPE, json);
        prefs.apply();
    }


    public static Recipe loadSelectedRecipe(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
        String jsonList = sharedPreferences.getString(PREF_SELECTED_RECIPE, null);

        if (sharedPreferences.contains(PREF_SELECTED_RECIPE)) {

            Gson gson = new Gson();
            Recipe recipe = gson.fromJson(jsonList, Recipe.class);

            return recipe;
        } else {
            return null;
        }

    }


    // Used to send the pending intent and navigate within the application
    static void saveSelectedRecipeId(Context context, int recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_RECIPE_ID, recipeId);
        prefs.apply();
    }

    // Used to send the pending intent and navigate within the application
    // If there is no preference saved, get the default from a resource
    static int loadRecipeId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int widget = prefs.getInt(PREF_RECIPE_ID, 0);
        return widget;
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


}
