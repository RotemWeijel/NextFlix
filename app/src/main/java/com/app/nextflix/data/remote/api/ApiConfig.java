package com.app.nextflix.data.remote.api;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfig {
    private static final String BASE_URL = "http://10.0.2.2:4000/";
    private static Retrofit retrofit;
    private static CategoryApi categoryApi;
    private static WebServiceApi webServiceApi;

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static CategoryApi getCategoryApi() {
        Log.d("ApiConfig", "Creating CategoryApi with base URL: " + BASE_URL);
        if (categoryApi == null) {
            categoryApi = getRetrofit().create(CategoryApi.class);
        }
        return categoryApi;
    }

    public static WebServiceApi getWebServiceApi() {
        if (webServiceApi == null) {
            webServiceApi = getRetrofit().create(WebServiceApi.class);
        }
        return webServiceApi;
    }
}