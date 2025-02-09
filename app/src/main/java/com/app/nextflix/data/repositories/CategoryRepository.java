package com.app.nextflix.data.repositories;

import android.util.Log;
import androidx.lifecycle.LiveData;
import com.app.nextflix.data.local.dao.CategoryDao;
import com.app.nextflix.data.local.entity.CategoryEntity;
import com.app.nextflix.data.remote.api.ApiConfig;
import com.app.nextflix.models.CategoryResponse;
import com.app.nextflix.data.remote.api.CategoryApi;
import com.app.nextflix.security.TokenManager;
import retrofit2.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryRepository {
    private static final String TAG = "CategoryRepo";
    private final CategoryDao categoryDao;
    private final CategoryApi categoryApi;
    private final TokenManager tokenManager;
    private final ExecutorService executorService;

    public CategoryRepository(CategoryDao categoryDao, TokenManager tokenManager) {
        this.categoryDao = categoryDao;
        this.categoryApi = ApiConfig.getCategoryApi();
        this.tokenManager = tokenManager;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CategoryEntity>> getAllCategories() {
        refreshCategories();
        return categoryDao.getAllCategories();
    }

    public void refreshCategories() {
        executorService.execute(() -> {
            try {
                String token = tokenManager.getToken();
                Log.d(TAG, "Token: " + (token != null ? "exists" : "null"));

                if (token != null) {
                    Response<List<CategoryResponse>> response =
                            categoryApi.getAllCategories("Bearer " + token).execute();

                    Log.d(TAG, "API Response: " +
                            "isSuccessful=" + response.isSuccessful() +
                            ", body=" + (response.body() != null ? response.body().size() : "null"));

                    if (response.isSuccessful() && response.body() != null) {
                        List<CategoryEntity> entities = response.body().stream()
                                .map(CategoryResponse::toCategoryEntity)
                                .collect(Collectors.toList());

                        Log.d(TAG, "Converting " + entities.size() + " categories");
                        for (CategoryEntity entity : entities) {
                            Log.d(TAG, "Category: id=" + entity.getId() + ", name=" + entity.getName());
                        }
                        categoryDao.insertAll(entities);
                        Log.d(TAG, "Saved categories to database");
                    } else {
                        Log.e(TAG, "Error response: " +
                                (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching categories", e);
                e.printStackTrace();
            }
        });
    }

    public void createCategory(CategoryEntity category, Runnable onSuccess, OnError onError) {
        executorService.execute(() -> {
            try {
                String token = tokenManager.getToken();
                Log.d(TAG, "Creating new category with name: " + category.getName());

                if (token != null) {
                    Response<Void> response = categoryApi
                            .createCategory("Bearer " + token, category)
                            .execute();

                    Log.d(TAG, "Create response code: " + response.code());

                    if (response.isSuccessful()) {
                        Log.d(TAG, "Category created successfully");
                        // Refresh categories to get the new data
                        refreshCategories();
                        onSuccess.run();
                    } else {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Create failed. Response code: " + response.code() +
                                ", Error: " + errorBody);
                        onError.onError("Failed to create category: " + errorBody);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error creating category", e);
                e.printStackTrace();
                onError.onError(e.getMessage());
            }
        });
    }

    public void updateCategory(CategoryEntity category, Runnable onSuccess, OnError onError) {
        executorService.execute(() -> {
            try {
                String token = tokenManager.getToken();
                Log.d(TAG, "Updating category with ID: " + category.getId());
                Log.d(TAG, "Update data: name=" + category.getName() +
                        ", parentId=" + category.getParentCategoryId());

                if (token != null) {
                    // First, fetch the current category to preserve movie count
                    Response<CategoryResponse> currentCategoryResponse =
                            categoryApi.getCategoryById("Bearer " + token, category.getId()).execute();

                    if (currentCategoryResponse.isSuccessful() && currentCategoryResponse.body() != null) {
                        CategoryEntity currentCategory = currentCategoryResponse.body().toCategoryEntity();

                        // Preserve movie count
                        category.setMovieCount(currentCategory.getMovieCount());
                    }
                    Response<CategoryResponse> response = categoryApi
                            .updateCategory("Bearer " + token, category.getId(), category)
                            .execute();

                    Log.d(TAG, "Update response code: " + response.code());
                    // Changed this condition to handle 204 No Content
                    if (response.isSuccessful()) {  // This includes 200 and 204
                        if (response.code() == 204) {
                            // For 204, we just update our local entity since server accepted the update
                            categoryDao.update(category);
                            Log.d(TAG, "Category updated successfully (204 No Content)");
                        } else if (response.body() != null) {
                            // For 200 with content
                            CategoryEntity updatedCategory = response.body().toCategoryEntity();
                            categoryDao.update(updatedCategory);
                            Log.d(TAG, "Category updated successfully with response data");
                        }
                        onSuccess.run();
                    } else {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Update failed. Response code: " + response.code() +
                                ", Error: " + errorBody);
                        onError.onError("Failed to update category: " + errorBody);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error updating category", e);
                onError.onError(e.getMessage());
            }
        });
    }

    public void deleteCategory(CategoryEntity category, Runnable onSuccess, OnError onError) {
        executorService.execute(() -> {
            try {
                String token = tokenManager.getToken();
                if (token != null) {
                    Response<Void> response = categoryApi
                            .deleteCategory("Bearer " + token, category.getId())
                            .execute();

                    if (response.isSuccessful()) {
                        categoryDao.delete(category);
                        onSuccess.run();
                    } else {
                        onError.onError("Failed to delete category: " +
                                (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    }
                }
            } catch (Exception e) {
                onError.onError(e.getMessage());
            }
        });
    }

    public interface OnError {
        void onError(String message);
    }
}