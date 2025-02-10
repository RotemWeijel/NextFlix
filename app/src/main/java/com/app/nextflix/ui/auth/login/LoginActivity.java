package com.app.nextflix.ui.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.app.nextflix.data.repositories.AuthRepository;
import com.app.nextflix.ui.main.browse.BrowseActivity;
import com.app.nextflix.ui.auth.register.RegistrationActivity;
import com.app.nextflix.databinding.ActivityLoginBinding;
import com.app.nextflix.security.TokenManager;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize TokenManager
        tokenManager = TokenManager.getInstance(this);

        // Check for existing authentication before setting up the UI
        if (tokenManager.getStoredToken() != null) {
            // User is already logged in, redirect to BrowseActivity
            Intent intent = new Intent(this, BrowseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        setupViews();
        observeViewModel();
    }

    private void setupViewModel() {
        AuthRepository authRepository = new AuthRepository(this);
        LoginViewModelFactory factory = new LoginViewModelFactory(authRepository);
        viewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);
    }

    private void setupViews() {
        binding.loginButton.setOnClickListener(v -> attemptLogin());
        binding.registerLink.setOnClickListener(v -> navigateToRegister());
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.loginButton.setEnabled(!isLoading);
            binding.usernameEditText.setEnabled(!isLoading);
            binding.passwordEditText.setEnabled(!isLoading);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                binding.errorTextView.setText(error);
                binding.errorTextView.setVisibility(View.VISIBLE);
            } else {
                binding.errorTextView.setVisibility(View.GONE);
            }
        });

        viewModel.getLoginResult().observe(this, user -> {
            if (user != null && tokenManager.getStoredToken() != null) {
                startActivity(new Intent(this, BrowseActivity.class));
                finish();
            }
        });
    }

    private void attemptLogin() {
        String username = binding.usernameEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();
        viewModel.login(username, password);
    }

    private void navigateToRegister() {
        startActivity(new Intent(this, RegistrationActivity.class));
    }
}