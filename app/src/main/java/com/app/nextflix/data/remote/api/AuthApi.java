package com.app.nextflix.data.remote.api;

import com.app.nextflix.data.api.LoginRequest;
import com.app.nextflix.data.api.LoginResponse;
import com.app.nextflix.data.api.RegistrationRequest;
import com.app.nextflix.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/api/tokens")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/users")
    Call<Void> register(@Body RegistrationRequest request);
}