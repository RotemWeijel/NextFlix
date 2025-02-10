package com.app.nextflix.data.remote.api;

import android.util.Log;

import com.app.nextflix.security.TokenManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final TokenManager tokenManager;

    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        String token = tokenManager.getStoredToken();
        Log.d("AuthInterceptor", "Original URL: " + chain.request().url());
        Log.d("AuthInterceptor", "Token present: " + (token != null));

        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        Request request = requestBuilder.build();
        Log.d("AuthInterceptor", "Headers: " + request.headers());
        return chain.proceed(request);
    }
}