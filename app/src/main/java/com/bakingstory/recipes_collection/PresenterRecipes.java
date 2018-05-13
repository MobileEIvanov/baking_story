package com.bakingstory.recipes_collection;

import android.support.test.espresso.idling.CountingIdlingResource;

import com.bakingstory.data.RestDataSource;
import com.bakingstory.entities.Recipe;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bakingstory.recipes_collection.ContractRecipes.*;

/**
 * Created by emil.ivanov on 3/30/18.
 * Presenter class responsible for content loading for {@link Recipe} list items.
 */

public class PresenterRecipes implements Presenter {

    private final View mView;

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
