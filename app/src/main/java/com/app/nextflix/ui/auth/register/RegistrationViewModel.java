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
    private final MutableLiveData<String> usernameError = new MutableLiveData<>();
    private final MutableLiveData<String> passwordError = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPasswordError = new MutableLiveData<>();
    private final MutableLiveData<String> displayNameError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFormValid = new MutableLiveData<>(false);

    private final List<String> avatarUrls = Arrays.asList(
            "drawable/avatar1",
            "drawable/avatar2",
            "drawable/avatar3",
            "drawable/avatar4",
            "drawable/avatar5",
            "drawable/avatar6"
    );
    private static final String SPECIAL_CHARS = "!@#$%^&*-_";

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
        validateUsername(username);
        validatePassword(password);
        validateConfirmPassword(confirmPassword, password);
        validateDisplayName(displayName);

        return isFormValid.getValue() != null && isFormValid.getValue();
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

    public void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            usernameError.setValue("Username is required");
            return;
        }
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            usernameError.setValue("Username can only contain English letters and numbers");
            return;
        }
        if (username.length() < 6 || username.length() > 12) {
            usernameError.setValue("Username must be 6-12 characters long");
            return;
        }
        usernameError.setValue(null);
        validateForm();
    }

    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            passwordError.setValue("Password is required");
            return;
        }
        if (!password.matches("^[a-zA-Z0-9" + SPECIAL_CHARS + "]+$")) {
            passwordError.setValue("Password can only contain English letters, numbers, and special characters (!@#$%^&*-_)");
            return;
        }
        if (password.length() < 8 || password.length() > 20) {
            passwordError.setValue("Password must be 8-20 characters long");
            return;
        }
        passwordError.setValue(null);
        validateForm();
    }

    public void validateConfirmPassword(String confirmPassword, String password) {
        if (!confirmPassword.equals(password)) {
            confirmPasswordError.setValue("Passwords do not match");
            return;
        }
        confirmPasswordError.setValue(null);
        validateForm();
    }

    public void validateDisplayName(String displayName) {
        if (displayName == null || displayName.isEmpty()) {
            displayNameError.setValue("Display name is required");
            return;
        }
        if (!displayName.matches("^[a-zA-Z0-9]+$")) {
            displayNameError.setValue("Display name can only contain English letters and numbers");
            return;
        }
        if (displayName.length() < 5 || displayName.length() > 12) {
            displayNameError.setValue("Display name must be 5-12 characters long");
            return;
        }
        displayNameError.setValue(null);
        validateForm();
    }

    private void validateForm() {
        boolean isValid = usernameError.getValue() == null &&
                passwordError.getValue() == null &&
                confirmPasswordError.getValue() == null &&
                displayNameError.getValue() == null;
        isFormValid.setValue(isValid);
    }

    public LiveData<String> getUsernameError() { return usernameError; }
    public LiveData<String> getPasswordError() { return passwordError; }
    public LiveData<String> getConfirmPasswordError() { return confirmPasswordError; }
    public LiveData<String> getDisplayNameError() { return displayNameError; }
    public LiveData<Boolean> getIsFormValid() { return isFormValid; }
}