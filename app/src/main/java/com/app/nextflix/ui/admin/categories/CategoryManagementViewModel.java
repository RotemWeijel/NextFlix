package com.app.nextflix.ui.admin.categories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.app.nextflix.data.local.entity.CategoryEntity;
import com.app.nextflix.data.repositories.CategoryRepository;
import java.util.List;

public class CategoryManagementViewModel extends ViewModel {
    private final CategoryRepository repository;
    private final MutableLiveData<String> feedback = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public CategoryManagementViewModel(CategoryRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<CategoryEntity>> getCategories() {
        Log.d("CategoryViewModel", "getCategories() called");
            return repository.getAllCategories();
    }

    public LiveData<String> getFeedback() {
        return feedback;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }


    public void createCategory(CategoryEntity category) {
        isLoading.setValue(true);
        repository.createCategory(category,
                () -> {
                    feedback.postValue("Category created successfully!");
                    isLoading.postValue(false);
                },
                error -> {
                    feedback.postValue("Failed to create category: " + error);
                    isLoading.postValue(false);
                }
        );
    }

    public void updateCategory(CategoryEntity category) {
        isLoading.setValue(true);
        repository.updateCategory(category,
                () -> {
                    feedback.postValue("Category updated successfully!");
                    isLoading.postValue(false);
                },
                error -> {
                    feedback.postValue("Failed to update category: " + error);
                    isLoading.postValue(false);
                }
        );
    }

    public void deleteCategory(CategoryEntity category) {
        isLoading.setValue(true);
        repository.deleteCategory(category,
                () -> {
                    feedback.postValue("Category deleted successfully!");
                    isLoading.postValue(false);
                },
                error -> {
                    feedback.postValue("Failed to delete category: " + error);
                    isLoading.postValue(false);
                }
        );
    }
}