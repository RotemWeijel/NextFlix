package com.app.nextflix.ui.main.search;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.nextflix.R;
import com.app.nextflix.data.remote.api.MovieApi;
import com.app.nextflix.data.repositories.MovieRepository;
import com.app.nextflix.models.Movie;
import com.app.nextflix.ui.admin.adapters.RecommendedMoviesAdapter;
import com.app.nextflix.ui.common.NavBarManager;
import com.app.nextflix.ui.main.details.DetailsMovie;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements RecommendedMoviesAdapter.OnMovieClickListener {
    private SearchViewModel viewModel;
    private RecyclerView searchResultsRecyclerView;
    private TextView searchQueryText;
    private TextView resultsCountText;
    private TextView noResultsText;
    private View loadingView;
    private NavBarManager navBarManager;
    private RecommendedMoviesAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        initViews();

        navBarManager = new NavBarManager(this);
        navBarManager.setupNavBars();

        setupViewModel();

        String searchQuery = getIntent().getStringExtra("search_query");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            searchQueryText.setText("Search results for \"" + searchQuery + "\"");
            viewModel.searchMovies(searchQuery);
        }
    }

    private void initViews() {
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        searchQueryText = findViewById(R.id.searchQueryText);
        resultsCountText = findViewById(R.id.resultsCountText);
        noResultsText = findViewById(R.id.noResultsText);
        loadingView = findViewById(R.id.loadingView);

        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new RecommendedMoviesAdapter(this);
        searchResultsRecyclerView.setAdapter(movieAdapter);
    }

    private void setupViewModel() {
        MovieApi movieApi = MovieApi.getInstance(this);
        MovieRepository movieRepository = new MovieRepository(movieApi, null); // Since we don't need Room for search

        SearchViewModelFactory factory = new SearchViewModelFactory(getApplication(), movieRepository);
        viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);

        viewModel.getIsLoading().observe(this, isLoading ->
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        viewModel.getSearchResults().observe(this, movies -> {
            if (movies != null) {
                if (movies.isEmpty()) {
                    noResultsText.setVisibility(View.VISIBLE);
                    searchResultsRecyclerView.setVisibility(View.GONE);
                    resultsCountText.setVisibility(View.GONE);
                } else {
                    noResultsText.setVisibility(View.GONE);
                    searchResultsRecyclerView.setVisibility(View.VISIBLE);
                    resultsCountText.setVisibility(View.VISIBLE);
                    resultsCountText.setText(movies.size() + " movies found");
                    movieAdapter.setMovies(movies);
                }
            }
        });

        viewModel.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, DetailsMovie.class);
        intent.putExtra("movie_id", movie.getId());
        startActivity(intent);
    }
}