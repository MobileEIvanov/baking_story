package com.bakingstory.WidgetIngredients;

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
 * Created by emil.ivanov on 4/27/18.
 */
public class BakingIngredientsRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;

    private List<Ingredient> mDataIngredients;

    private int appWidgetId;
    private String mRecipeTitle;

    public BakingIngredientsRemoteFactory(Context context) {
        mContext = context;

    }

    @Override
    public void onCreate() {

    }


    @Override
    public void onDataSetChanged() {
        mDataIngredients = loadRecipeIngredients();
    }


    private List<Ingredient> loadRecipeIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        appWidgetId = HomeWidgetConfigureActivity.loadLastWidgetIdPref(mContext);
        if (appWidgetId != -1) {
            mRecipeTitle = HomeWidgetConfigureActivity.loadTitlePref(mContext, appWidgetId);

            ArrayList<Recipe> recipes = null;

            try {
                recipes = (ArrayList<Recipe>) HelperJsonDataParser.getAllRecepies(mContext);
            } catch (IOException e) {
                e.printStackTrace();

            }


            for (Recipe recipe : recipes) {
                if (recipe.getName().equals(mRecipeTitle)) {
                    ingredients = recipe.getIngredients();
                }
            }
        }
        return ingredients;
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
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_ingredient);
        rv.setTextViewText(R.id.tv_ingredient, mDataIngredients.get(position).getIngredient());
        rv.setTextViewText(R.id.tv_measurement, mDataIngredients.get(position).getMeasurementAsPlainText());
        rv.setTextViewText(R.id.tv_quantity, "" + mDataIngredients.get(position).getFormattedQuantity());


        Intent fillInIntent = new Intent();
        rv.setOnClickFillInIntent(R.id.tv_ingredient, fillInIntent);
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