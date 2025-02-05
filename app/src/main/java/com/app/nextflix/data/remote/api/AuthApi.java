package com.app.nextflix.data.remote.api;

import com.app.nextflix.data.api.LoginRequest;
import com.app.nextflix.data.api.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/api/tokens")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}