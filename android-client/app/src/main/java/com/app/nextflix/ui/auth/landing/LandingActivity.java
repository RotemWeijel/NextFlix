package com.app.nextflix.ui.auth.landing;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.app.nextflix.R;
import com.app.nextflix.ui.auth.login.LoginActivity;
import com.app.nextflix.ui.auth.register.RegistrationActivity;

public class LandingActivity extends AppCompatActivity {

    private LandingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        viewModel = new ViewModelProvider(this).get(LandingViewModel.class);

        setupUI();
        observeViewModel();
    }

    private void setupUI() {
        // Setup RecyclerView
        RecyclerView featuresRecyclerView = findViewById(R.id.featuresRecyclerView);

        // Use LinearLayoutManager with horizontal orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        featuresRecyclerView.setLayoutManager(layoutManager);

        // Optional: Add snap helper to snap cards to center
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(featuresRecyclerView);

        // Create and set adapter
        FeatureAdapter adapter = new FeatureAdapter();
        featuresRecyclerView.setAdapter(adapter);

        // Set fixed height to RecyclerView
        ViewGroup.LayoutParams params = featuresRecyclerView.getLayoutParams();
        params.height = getResources().getDimensionPixelSize(R.dimen.features_grid_height);
        featuresRecyclerView.setLayoutParams(params);

        // Setup Buttons
        findViewById(R.id.getStartedButton).setOnClickListener(v ->
                viewModel.onGetStartedClick());

        findViewById(R.id.signInButton).setOnClickListener(v ->
                viewModel.onSignInClick());
    }

    private void observeViewModel() {
        viewModel.getNavigationEvent().observe(this, event -> {
            if (event == null) return;

            Intent intent;
            switch (event) {
                case LOGIN:
                    intent = new Intent(this, LoginActivity.class);
                    break;
                case REGISTER:
                    intent = new Intent(this, RegistrationActivity.class);
                    break;
                default:
                    return;
            }
            startActivity(intent);
        });
    }
}