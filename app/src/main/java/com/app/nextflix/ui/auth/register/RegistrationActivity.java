package com.app.nextflix.ui.auth.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
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
        binding.avatarRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        AvatarAdapter adapter = new AvatarAdapter(viewModel.getAvatarUrls(), this, this);
        binding.avatarRecyclerView.setAdapter(adapter);

        binding.usernameEditText.addTextChangedListener(createTextWatcher(text ->
                viewModel.validateUsername(text)));
        binding.passwordEditText.addTextChangedListener(createTextWatcher(text -> {
            viewModel.validatePassword(text);
            if (!binding.confirmPasswordEditText.getText().toString().isEmpty()) {
                viewModel.validateConfirmPassword(
                        binding.confirmPasswordEditText.getText().toString(), text);
            }
        }));
        binding.confirmPasswordEditText.addTextChangedListener(createTextWatcher(text ->
                viewModel.validateConfirmPassword(text, binding.passwordEditText.getText().toString())));
        binding.displayNameEditText.addTextChangedListener(createTextWatcher(text ->
                viewModel.validateDisplayName(text)));

        binding.registerButton.setOnClickListener(v -> attemptRegistration());
        binding.loginLink.setOnClickListener(v -> navigateToLogin());

        observeErrors();
    }

    private TextWatcher createTextWatcher(Consumer<String> onTextChanged) {
        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                onTextChanged.accept(s.toString());
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };
    }

    private void observeErrors() {
        viewModel.getUsernameError().observe(this, error ->
                binding.usernameLayout.setError(error));
        viewModel.getPasswordError().observe(this, error ->
                binding.passwordLayout.setError(error));
        viewModel.getConfirmPasswordError().observe(this, error ->
                binding.confirmPasswordLayout.setError(error));
        viewModel.getDisplayNameError().observe(this, error ->
                binding.displayNameLayout.setError(error));
        viewModel.getIsFormValid().observe(this, isValid ->
                binding.registerButton.setEnabled(isValid));
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