package com.bakingstory.WidgetIngredients;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by emil.ivanov on 4/30/18.
 */
public class IngredientService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new HomeWidget.BakingIngredientsRemoteFactory(this.getApplicationContext());
    }
}
