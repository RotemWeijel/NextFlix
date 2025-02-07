// AuthApi.java
package com.app.nextflix.data.api;

import com.app.nextflix.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/api/tokens")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/users")
    Call<User> register(@Body RegistrationRequest request);
}