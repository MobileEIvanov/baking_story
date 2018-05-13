package com.bakingstory.WidgetIngredients;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.bakingstory.R;
import com.bakingstory.RecipeCollection.ActivityRecipesList;
import com.bakingstory.RecipeDetails.ActivityBakingStepDetails;
import com.bakingstory.RecipeDetails.BakingSteps.ActivityBakingStepsList;
import com.bakingstory.entities.Recipe;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link HomeWidgetConfigureActivity HomeWidgetConfigureActivity}
 */
public class HomeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_widget);
        views.setTextViewText(R.id.tv_header_ingredients, PreferensesManager.loadTitlePref(context, appWidgetId) + " " + context.getString(R.string.label_widget_header));

        Intent intent = new Intent(context, IngredientService.class);

        views.setEmptyView(R.id.lv_widget_ingredients, R.id.layout_widget_empty_view);
        views.setRemoteAdapter(R.id.lv_widget_ingredients, intent);


        Recipe recipe = PreferensesManager.loadSelectedRecipe(context);
        Intent appIntent = new Intent(context, ActivityBakingStepsList.class);
        appIntent.putExtra(Recipe.RECIPE_DATA, recipe);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.lv_widget_ingredients, appPendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            PreferensesManager.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

