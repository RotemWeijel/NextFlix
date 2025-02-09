package com.app.nextflix.ui.main.browse;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import com.app.nextflix.R;
import com.app.nextflix.utils.ImageUtils;
import com.app.nextflix.data.repositories.AuthRepository;
import com.app.nextflix.data.repositories.UserRepository;
import com.app.nextflix.models.Movie;
import com.app.nextflix.models.User;
import com.app.nextflix.ui.admin.adapters.MovieCategoryAdapter;
import com.app.nextflix.ui.common.CategoryMoviesViewModel;
import com.app.nextflix.ui.admin.adapters.RecommendedMoviesAdapter;

import com.app.nextflix.ui.common.NavBarManager;
import com.app.nextflix.ui.main.details.DetailsMovie;
import com.app.nextflix.ui.main.player.PlayerActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class BrowseActivity extends AppCompatActivity implements RecommendedMoviesAdapter.OnMovieClickListener {

    private CategoryMoviesViewModel viewModel;
    private RequestQueue requestQueue;
    private MovieCategoryAdapter categoryAdapter;
    private RecyclerView contentRecyclerView;
    private View loadingView;

    private ImageView heroImage;
    private TextView heroTitle;
    private ChipGroup categoryChipGroup;
    private Button playButton;
    private NavBarManager navBarManager;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        appBarLayout.setBackgroundColor(Color.BLACK);

        // Setup NavBarManager with current user
        navBarManager = new NavBarManager(this);
        navBarManager.setupNavBars();

        initViews();
        setupViewModel();
        setupRecyclerView();
        setupHeroSection();

    }

    private void setupHeroSection() {
        playButton.setOnClickListener(v -> {
            Movie curr = viewModel.getHeroMovie().getValue();
            if (curr != null) {
                Intent intent = new Intent(this, PlayerActivity.class);
                intent.putExtra("video_path", curr.getVideoUrl());
                intent.putExtra("name", curr.getName());
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        heroTitle = findViewById(R.id.heroTitle);
        contentRecyclerView = findViewById(R.id.contentRecyclerView);
        loadingView = findViewById(R.id.loadingView);
        heroImage = findViewById(R.id.heroImage);
        playButton = findViewById(R.id.playButton);
    }


    private void setupRecyclerView() {
        categoryAdapter = new MovieCategoryAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(layoutManager);
        contentRecyclerView.setAdapter(categoryAdapter);

    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(CategoryMoviesViewModel.class);

        loadingView.setVisibility(View.VISIBLE);

        viewModel.getLoading().observe(this, isLoading -> {
            loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getCategorizedMovies().observe(this, categories -> {
            if (categories != null) {
                categoryAdapter.setCategories(categories);
            }
        });

        viewModel.getHeroMovie().observe(this, movie -> {
            if (movie != null) {
                updateHeroSection(movie);
            }
        });

        viewModel.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
       observeViewModel();
        viewModel.init(this);
    }

    private void observeViewModel() {
        viewModel.getCategorizedMovies().observe(this, movieCategories -> {
            Log.d("BrowseActivity", "Received categories update: " +
                    (movieCategories != null ? movieCategories.size() : 0) + " categories");
            categoryAdapter.setCategories(movieCategories);
        });

        viewModel.getHeroMovie().observe(this, movie -> {
            if (movie != null) {
                Log.d("BrowseActivity", "Updating hero movie: " + movie.getName());
                updateHeroSection(movie);
            }
        });
        viewModel.getLoading().observe(this, isLoading ->
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE));
        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateHeroSection(Movie movie) {
        Log.d("BrowseActivity", "Categories for " + movie.getName() + ": " +
                (movie.getCategories() != null ? movie.getCategories().toString() : "null"));

        heroTitle.setText(movie.getName());
        Log.d("BrowseActivity", "Movie image URL: " + movie.getImageUrl());

        // class that handle in logics of load image
        ImageUtils.loadMovieImage(this, movie.getImageUrl(), heroImage);
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent=new Intent(this, DetailsMovie.class);
        intent.putExtra("movie_id",movie.getId());
        startActivity(intent);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }
}

