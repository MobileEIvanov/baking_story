package com.bakingstory.data;


import com.bakingstory.entities.Recipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;


/**
 * Request recipe list information
 * {@link QueryRequest} is then used to create an Retrofit client in order to retrieve the information {@link RestClient}
 */
interface QueryRequest {
    String URL_RECIPES = "baking.json";

    @GET(URL_RECIPES)
    Observable<List<Recipe>> requestRecipeList();

}
