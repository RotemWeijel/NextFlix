package com.app.nextflix.data.repositories;

import android.content.Context;
import android.util.Log;

import com.app.nextflix.data.api.LoginRequest;
import com.app.nextflix.data.api.LoginResponse;
import com.app.nextflix.data.api.RegistrationRequest;
import com.app.nextflix.data.local.UserPreferences;
import com.app.nextflix.data.remote.api.AuthApi;
import com.app.nextflix.data.remote.api.RetrofitClient;
import com.app.nextflix.models.User;
import com.app.nextflix.security.TokenManager;
import com.app.nextflix.utils.UrlUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import retrofit2.Response;

public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private final AuthApi authApi;
    private final TokenManager tokenManager;
    private final UserPreferences userPreferences;
    private final UserRepository userRepository;

    public AuthRepository(Context context) {
        this.tokenManager = TokenManager.getInstance(context);
        this.userRepository = UserRepository.getInstance(context);
        this.userPreferences = UserPreferences.getInstance(context);
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

                // Save token
                tokenManager.setToken(response.body().getToken());

                // Get user and transform URLs
                User user = response.body().getUser();
                if (user != null && user.getPicture() != null) {
                    String originalUrl = user.getPicture();
                    Log.d(TAG, "Original profile picture URL: " + originalUrl);
                    String transformedUrl = UrlUtils.transformUrl(originalUrl);
                    Log.d(TAG, "Transformed profile picture URL: " + transformedUrl);
                    user.setPicture(transformedUrl);
                }

                // Save transformed user data
                userPreferences.saveUser(user);
                Log.d(TAG, "Saved user with picture URL: " + (user != null ? user.getPicture() : "null"));

                return user;

            } catch (Exception e) {
                Log.e("AuthRepository", "Login failed", e);
                tokenManager.clearToken();
                userRepository.clearCurrentUser();
                throw new CompletionException(e);
            }
        });
    }

    public User getCurrentUser() {
        User user = userPreferences.getUser();
        // Transform URLs when getting current user too
        if (user != null && user.getPicture() != null) {
            String transformedUrl = UrlUtils.transformUrl(user.getPicture());
            user.setPicture(transformedUrl);
        }
        return user;
    }

    public CompletableFuture<User> register(String username, String password, String displayName, String avatarUrl) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                RegistrationRequest request = new RegistrationRequest(username, password, displayName, avatarUrl);
                Response<Void> response = authApi.register(request).execute();

                if (!response.isSuccessful()) {
                    String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                    Log.e(TAG, "Registration failed with code: " + response.code() + ", error: " + errorBody);
                    throw new Exception("Registration failed with code: " + response.code());
                }

                // Create user object since registration was successful
                User user = new User();
                user.setUsername(username);
                user.setFull_name(displayName);
                user.setPicture(avatarUrl);

                return user;
            } catch (Exception e) {
                Log.e(TAG, "Registration failed", e);
                throw new CompletionException(e);
            }
        });
    }
}