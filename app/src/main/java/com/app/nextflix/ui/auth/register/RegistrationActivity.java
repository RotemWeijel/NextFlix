package com.app.nextflix.ui.auth.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.app.nextflix.databinding.ActivityRegistrationBinding;
import com.app.nextflix.data.repositories.AuthRepository;
import com.app.nextflix.security.TokenManager;
import com.app.nextflix.ui.auth.login.LoginActivity;
import com.app.nextflix.utils.UrlUtils;

public class RegistrationActivity extends AppCompatActivity implements AvatarAdapter.OnAvatarSelectedListener {
    private ActivityRegistrationBinding binding;
    private RegistrationViewModel viewModel;
    private String selectedAvatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        setupViews();
        observeViewModel();
    }

    private void setupViewModel() {
        AuthRepository authRepository = new AuthRepository(this);
        RegistrationViewModelFactory factory = new RegistrationViewModelFactory(authRepository);
        viewModel = new ViewModelProvider(this, factory).get(RegistrationViewModel.class);
    }

    private void setupViews() {
        // Setup avatar recycler view
        binding.avatarRecyclerView.setLayoutManager(
                new GridLayoutManager(this, 3));
        AvatarAdapter adapter = new AvatarAdapter(viewModel.getAvatarUrls(), this, this);
        binding.avatarRecyclerView.setAdapter(adapter);

        // Setup click listeners
        binding.registerButton.setOnClickListener(v -> attemptRegistration());
        binding.loginLink.setOnClickListener(v -> navigateToLogin());
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.registerButton.setEnabled(!isLoading);
            binding.usernameEditText.setEnabled(!isLoading);
            binding.passwordEditText.setEnabled(!isLoading);
            binding.confirmPasswordEditText.setEnabled(!isLoading);
            binding.displayNameEditText.setEnabled(!isLoading);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                binding.errorTextView.setText(error);
                binding.errorTextView.setVisibility(View.VISIBLE);
            } else {
                binding.errorTextView.setVisibility(View.GONE);
            }
        });

        viewModel.getRegistrationResult().observe(this, user -> {
            if (user != null) {
                startActivity(new Intent(this, RegistrationSuccessActivity.class));
                finish();
            }
        });
    }

    private void attemptRegistration() {
        String username = binding.usernameEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();
        String confirmPassword = binding.confirmPasswordEditText.getText().toString();
        String displayName = binding.displayNameEditText.getText().toString();

        viewModel.register(username, password, confirmPassword,
                displayName, selectedAvatarUrl);

        viewModel.getLoginResult().observe(this, user -> {
            if (user != null) {
                startActivity(new Intent(this, RegistrationSuccessActivity.class));
                finish();
            }
        });
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onAvatarSelected(int position, String avatarUrl) {
        selectedAvatarUrl = UrlUtils.transformUrl(avatarUrl);
    }
}