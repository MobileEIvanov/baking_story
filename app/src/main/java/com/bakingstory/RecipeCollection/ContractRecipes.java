package com.bakingstory.RecipeCollection;

import com.bakingstory.entities.Recipe;

import java.util.List;

/**
 * Created by emil.ivanov on 3/30/18.
 */

public interface ContractRecipes {


    interface Presenter {
        void requestRecipes();
    }

    interface View {
        void showRecipesList(List<Recipe> recipesList);
        void showErrorView();
    }
}
