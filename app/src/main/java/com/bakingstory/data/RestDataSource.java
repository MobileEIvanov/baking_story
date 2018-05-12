package com.bakingstory.data;

import android.util.Log;

import com.bakingstory.entities.Recipe;

import java.util.List;

import io.reactivex.Observable;


public class RestDataSource implements QueryRequest {
    private static final String BASE_API_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking";


    private final QueryRequest mRequestQuery;

    public RestDataSource() {
        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(BASE_API_URL)
                .append("/");
        Log.d("Rest:", "RestDataSource: " + baseUrl.toString());
        mRequestQuery = RestClient.initConnection(baseUrl.toString());
    }


    @Override
    public Observable<List<Recipe>> requestRecipeList() {
        return mRequestQuery.requestRecipeList();
    }

}
