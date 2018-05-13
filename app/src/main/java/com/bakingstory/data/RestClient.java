package com.bakingstory.data;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by emil ivanov on 2/21/18.
 * <p>
 * Retrofit request builder class which uses {@link QueryRequest} defined request.
 * {@link RxJava2CallAdapterFactory} provides the means to use RxJava 2 for creating observables.
 * {@link GsonConverterFactory} provides the means to deserialize the response from each request
 * {@link StethoInterceptor } provides log of all http request and possibility to see it in Chrome inspector
 * {@link HttpLoggingInterceptor}  provides log of all http requests visible in the Logcat
 */

class RestClient {


    static QueryRequest initConnection(String baseURL) {

        Retrofit retrofitRx = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(getCustomHeaderWithoutXAuth())
                .build();
        return retrofitRx.create(QueryRequest.class);
    }


    /**
     * Creates custom {@link OkHttpClient} which has log support for each http request.
     *
     * @return - instance of OkHttpClient to be used in Retrofit2 builder
     */
    private static OkHttpClient getCustomHeaderWithoutXAuth() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.addNetworkInterceptor(new StethoInterceptor());

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });


        return httpClient.build();
    }


}
