package com.bakingstory.WidgetIngredients;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bakingstory.R;
import com.bakingstory.data.HelperJsonDataParser;
import com.bakingstory.entities.Ingredient;
import com.bakingstory.entities.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link HomeWidgetConfigureActivity HomeWidgetConfigureActivity}
 */
public class HomeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = HomeWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_widget);

        ArrayList<Recipe> recipes = null;

        try {
            recipes = (ArrayList<Recipe>) HelperJsonDataParser.getAllRecepies(context);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Intent intent = new Intent(context, IngredientService.class);
//        Bundle bundle = new Bundle();
//        bundle.setClassLoader(Ingredient.class.getClassLoader());
//        bundle.putParcelableArrayList(Ingredient.INGREDIENT_DATA, (ArrayList<? extends Parcelable>) recipes.get(0).getIngredients());
        List<Ingredient> ingredients = recipes.get(0).getIngredients();


        views.setRemoteAdapter(R.id.lv_widget_ingredients, intent);
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
            HomeWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
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

    /**
     * Created by emil.ivanov on 4/27/18.
     */
    public static class BakingIngredientsRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;

        private List<Ingredient> mDataIngredients;

        public BakingIngredientsRemoteFactory(Context context) {
            mContext = context;


        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            try {
                mDataIngredients = HelperJsonDataParser.getAllRecepies(mContext).get(0).getIngredients();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {

            return mDataIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredient);
            rv.setTextViewText(R.id.tv_ingredient, mDataIngredients.get(position).getIngredient());

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}

