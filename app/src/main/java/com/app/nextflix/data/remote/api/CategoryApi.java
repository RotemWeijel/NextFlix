package com.app.nextflix.data.remote.api;

import com.app.nextflix.data.local.entity.CategoryEntity;
import com.app.nextflix.models.CategoryResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface CategoryApi {
    @GET("/api/categories")
    Call<List<CategoryResponse>> getAllCategories();

    @POST("/api/categories")
    Call<Void> createCategory(@Body CategoryEntity category);

    @GET("/api/categories/{id}")
    Call<CategoryResponse> getCategoryById(@Path("id") String id);

    @PATCH("/api/categories/{id}")
    Call<CategoryResponse> updateCategory(@Path("id") String id, @Body CategoryEntity category);

    @DELETE("/api/categories/{id}")
    Call<Void> deleteCategory(@Path("id")String id);
}