package com.app.nextflix.ui.auth.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.nextflix.data.api.RegistrationRequest;
import com.app.nextflix.data.repositories.AuthRepository;
import com.app.nextflix.models.User;

import java.util.Arrays;
import java.util.List;

public class RegistrationViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<User> registrationResult = new MutableLiveData<>();
    private final MutableLiveData<User> loginResult = new MutableLiveData<>();
    private final List<String> avatarUrls = Arrays.asList(
            "drawable/avatar1",
            "drawable/avatar2",
            "drawable/avatar3",
            "drawable/avatar4",
            "drawable/avatar5",
            "drawable/avatar6"
    );

    public RegistrationViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void register(String username, String password, String confirmPassword,
                         String displayName, String avatarUrl) {
        if (!validateInput(username, password, confirmPassword, displayName)) {
            return;
        }

        isLoading.setValue(true);
        errorMessage.setValue(null);

        // Use direct parameters instead of RegistrationRequest
        authRepository.register(username, password, displayName, avatarUrl)
                .thenCompose(user -> {
                    registrationResult.postValue(user);
                    return authRepository.login(username, password);
                })
                .thenAccept(user -> {
                    loginResult.postValue(user);
                    isLoading.postValue(false);
                })
                .exceptionally(error -> {
                    isLoading.postValue(false);
                    errorMessage.postValue(error.getMessage());
                    return null;
                });
    }

    private boolean validateInput(String username, String password,
                                  String confirmPassword, String displayName) {
        if (username == null || username.trim().isEmpty()) {
            errorMessage.setValue("Username is required");
            return false;
        }
        if (password == null || password.isEmpty()) {
            errorMessage.setValue("Password is required");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            errorMessage.setValue("Passwords do not match");
            return false;
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            errorMessage.setValue("Display name is required");
            return false;
        }
        return true;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<User> getRegistrationResult() {
        return registrationResult;
    }

    public LiveData<User> getLoginResult() {
        return loginResult;
    }

    public List<String> getAvatarUrls() {
        return avatarUrls;
    }
}