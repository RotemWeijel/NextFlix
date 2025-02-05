package com.app.nextflix.data.remote.api;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private static Retrofit retrofit = null;
    private static String baseUrl;

    public static void initialize(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        baseUrl = isEmulator() ?
                "http://10.0.2.2:4000/" :
                "http://192.168.7.3:4000/";
    }

    private static boolean isEmulator() {
        return Build.PRODUCT.contains("sdk") ||
                Build.PRODUCT.contains("gphone") ||
                Build.PRODUCT.contains("emulator") ||
                Build.FINGERPRINT.contains("generic") ||
                Build.MANUFACTURER.contains("Genymotion");
    }

    public static Retrofit getClient() {
        if (baseUrl == null) {
            throw new IllegalStateException("RetrofitClient must be initialized with context first");
        }

        if (retrofit == null) {
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            } catch (Exception e) {
                Log.e(TAG, "Error initializing Retrofit", e);
                throw new RuntimeException("Failed to initialize Retrofit", e);
            }
        }
        return retrofit;
    }

    public static AuthApi getAuthApi() {
        return getClient().create(AuthApi.class);
    }

    public static WebServiceApi getWebServiceApi() {
        return getClient().create(WebServiceApi.class);
    }
}