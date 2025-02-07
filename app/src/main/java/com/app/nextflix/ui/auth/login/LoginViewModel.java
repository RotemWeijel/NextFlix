package com.app.nextflix.ui.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.nextflix.data.repositories.AuthRepository;
import com.app.nextflix.models.User;
import com.app.nextflix.utils.SecurityUtils;

public class LoginViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<User> loginResult = new MutableLiveData<>();

    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<User> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        if (!validateInput(username, password)) {
            return;
        }

        isLoading.setValue(true);
        errorMessage.setValue(null);

        authRepository.login(username, password)
                .thenAccept(result -> {
                    isLoading.postValue(false);
                    loginResult.postValue((User) result);
                })
                .exceptionally(throwable -> {
                    isLoading.postValue(false);
                    errorMessage.postValue(throwable.getCause() != null ?
                            throwable.getCause().getMessage() : "Login failed");
                    loginResult.postValue(null);
                    return null;
                });
    }

    private boolean validateInput(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            errorMessage.setValue("Username is required");
            return false;
        }
        if (password == null || password.isEmpty()) {
            errorMessage.setValue("Password is required");
            return false;
        }
        return true;
    }
}