package com.bakingstory.widget_ingredients;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Remote view service responsible for the visualisation of the
 * Ingredient items on the home screen. using {@link BakingIngredientsRemoteFactory}
 */
public class IngredientService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new BakingIngredientsRemoteFactory(this.getApplicationContext());
    }
}
