package com.app.nextflix.ui.admin.categories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.app.nextflix.data.repositories.CategoryRepository;

public class CategoryViewModelFactory implements ViewModelProvider.Factory {
    private final CategoryRepository repository;

    public CategoryViewModelFactory(CategoryRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryManagementViewModel.class)) {
            return (T) new CategoryManagementViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}