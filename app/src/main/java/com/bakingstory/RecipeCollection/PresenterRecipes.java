package com.bakingstory.RecipeCollection;

import android.content.Context;
import android.support.test.espresso.idling.CountingIdlingResource;

import com.bakingstory.data.HelperJsonDataParser;
import com.bakingstory.data.RestClient;
import com.bakingstory.data.RestDataSource;
import com.bakingstory.entities.Recipe;
import com.bakingstory.utils.HelperIdlingResource;

import java.io.IOException;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bakingstory.RecipeCollection.ContractRecipes.*;

/**
 * Created by emil.ivanov on 3/30/18.
 */

public class PresenterRecipes implements Presenter {

    private View mView;

    public PresenterRecipes(View view) {
        this.mView = view;
    }

    private CountingIdlingResource mIdlingResource;
    private Disposable mDisposable;

    @Override
    public void requestRecipes(CountingIdlingResource idlingResource) {

        // The IdlingResource is null in production.
        if (idlingResource != null) {
            mIdlingResource = idlingResource;
            mIdlingResource.increment();
        }

        RestDataSource restDataSource = new RestDataSource();
        mDisposable = restDataSource.requestRecipeList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mConsumerSuccess, mConsumerError);


    }

    @Override
    public void onStop() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private final Consumer<List<Recipe>> mConsumerSuccess = new Consumer<List<Recipe>>() {
        @Override
        public void accept(List<Recipe> recipes) {
            if (recipes != null) {
                mView.showRecipesList(recipes);
                if (mIdlingResource != null) {
                    mIdlingResource.decrement();
                }
            } else {
                mView.showErrorView();
                if (mIdlingResource != null) {
                    mIdlingResource.decrement();
                }
            }
        }
    };


    private final Consumer<Throwable> mConsumerError = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) {
            mView.showErrorView();
            if (mIdlingResource != null) {
                mIdlingResource.decrement();
            }
        }
    };

}
