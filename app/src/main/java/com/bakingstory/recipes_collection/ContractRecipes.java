package com.bakingstory.recipes_collection;

import android.support.test.espresso.idling.CountingIdlingResource;

import com.bakingstory.entities.Recipe;

import java.util.List;

/**
 * Created by emil.ivanov on 3/30/18.
 */

public interface ContractRecipes {


    @SuppressWarnings("unused")
    interface Presenter {
        void requestRecipes(CountingIdlingResource idlingResource);
        void onStop();
    }

    interface View {
        void showRecipesList(List<Recipe> recipesList);

        void showErrorView();
    }
}
