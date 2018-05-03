package com.bakingstory.RecipeCollection;

import android.content.Context;

import com.bakingstory.data.HelperJsonDataParser;
import com.bakingstory.entities.Recipe;

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
    public void requestRecipes() {

        try {
            List<Recipe> recipeList = HelperJsonDataParser.getAllRecepies(mContext);

            if(recipeList!=null){
                mView.showRecipesList(recipeList);
            }
            else {
                mView.showErrorView();
            }
        } catch (IOException e) {
            e.printStackTrace();
            mView.showErrorView();
        }

    }
}
