package com.bakingstory.RecipeCollection;

import android.support.test.espresso.idling.CountingIdlingResource;

import com.bakingstory.entities.Recipe;
import com.bakingstory.utils.HelperIdlingResource;

import java.util.List;

/**
 * Created by emil.ivanov on 3/30/18.
 */

public interface ContractRecipes {


    interface Presenter {
        void requestRecipes(CountingIdlingResource idlingResource);
    }

    interface View {
        void showRecipesList(List<Recipe> recipesList);

        void showErrorView();
    }
}
