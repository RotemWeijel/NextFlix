package com.app.nextflix.data.repositories;

import android.content.Context;

import com.app.nextflix.data.api.LoginRequest;
import com.app.nextflix.data.remote.api.AuthApi;
import com.app.nextflix.data.remote.api.RetrofitClient;
import com.app.nextflix.models.User;
import com.app.nextflix.security.TokenManager;
import com.app.nextflix.ui.auth.login.LoginResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import retrofit2.Response;

public class AuthRepository {
    private final AuthApi authApi;
    private final TokenManager tokenManager;
    private final Context context;

    public AuthRepository(Context context) {
        this.context = context;
        this.tokenManager = TokenManager.getInstance(context);
        RetrofitClient.initialize(context);
        this.authApi = RetrofitClient.getAuthApi();
    }

    public CompletableFuture<User> login(String username, String password) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LoginRequest loginRequest = new LoginRequest(username, password);
                Response<LoginResponse> response = authApi.login(loginRequest).execute();

                if (response.isSuccessful() && response.body() != null) {
                    tokenManager.setToken(response.body().getToken());
                    return response.body().getUser();
                } else {
                    String errorBody = response.errorBody() != null ?
                            response.errorBody().string() : "Login failed";
                    throw new Exception(errorBody);
                }
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }
}