package com.bakingstory.RecipeCollection;

import android.content.Context;

import com.bakingstory.data.HelperJsonDataParser;
import com.bakingstory.entities.Recipe;
import com.bakingstory.utils.HelperIdlingResource;

import java.io.IOException;
import java.util.List;

import static com.bakingstory.RecipeCollection.ContractRecipes.*;

/**
 * Created by emil.ivanov on 3/30/18.
 */

public class PresenterRecipes implements Presenter {

    private View mView;
    private Context mContext;

    public PresenterRecipes(Context context, View mView) {
        this.mView = mView;
        this.mContext = context;
    }

    @Override
    public void requestRecipes(HelperIdlingResource idlingResource) {

        try {
            // The IdlingResource is null in production.
            if (idlingResource != null) {
                idlingResource.setIdleState(false);
            }

            List<Recipe> recipeList = HelperJsonDataParser.getAllRecepies(mContext);

            if (recipeList != null) {
                mView.showRecipesList(recipeList);
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            } else {
                mView.showErrorView();
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            mView.showErrorView();
            if (idlingResource != null) {
                idlingResource.setIdleState(true);
            }
        }

    }
}
