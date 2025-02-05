package com.app.nextflix.data.repositories;

import android.content.Context;
import android.util.Log;

import com.app.nextflix.data.api.LoginRequest;
import com.app.nextflix.data.api.LoginResponse;
import com.app.nextflix.data.remote.api.AuthApi;
import com.app.nextflix.data.remote.api.RetrofitClient;
import com.app.nextflix.models.User;
import com.app.nextflix.security.TokenManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import retrofit2.Response;

public class AuthRepository {
    private final AuthApi authApi;
    private final TokenManager tokenManager;

    public AuthRepository(Context context) {
        this.tokenManager = TokenManager.getInstance(context);
        RetrofitClient.initialize(context);
        this.authApi = RetrofitClient.getAuthApi();
    }

    public CompletableFuture<User> login(String username, String password) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Log.d("AuthRepository", "Attempting login at URL: " + RetrofitClient.getClient().baseUrl());
                LoginRequest loginRequest = new LoginRequest(username, password);
                Response<LoginResponse> response = authApi.login(loginRequest).execute();
                Log.d("AuthRepository", "Response code: " + response.code());
                Log.d("AuthRepository", "Response body: " + (response.body() != null ? "not null" : "null"));
                Log.d("AuthRepository", "Error body: " + (response.errorBody() != null ? response.errorBody().string() : "null"));

                if (!response.isSuccessful()) {
                    tokenManager.clearToken();
                    throw new Exception("Login failed with code: " + response.code());
                }

                if (response.body() == null || response.body().getToken() == null) {
                    tokenManager.clearToken();
                    throw new Exception("No token received");
                }

                tokenManager.setToken(response.body().getToken());
                return response.body().getUser();
            } catch (Exception e) {
                Log.e("AuthRepository", "Login failed", e);
                tokenManager.clearToken();
                throw new CompletionException(e);
            }
        });
    }
}