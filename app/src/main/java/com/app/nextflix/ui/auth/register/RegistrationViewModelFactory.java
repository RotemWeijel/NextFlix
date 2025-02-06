package com.app.nextflix.ui.auth.register;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.nextflix.data.repositories.AuthRepository;

public class RegistrationViewModelFactory implements ViewModelProvider.Factory {
    private final AuthRepository authRepository;

    public RegistrationViewModelFactory(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegistrationViewModel.class)) {
            return (T) new RegistrationViewModel(authRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}