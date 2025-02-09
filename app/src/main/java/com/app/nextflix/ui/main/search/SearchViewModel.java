package com.app.nextflix.ui.main.search;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.app.nextflix.data.repositories.MovieRepository;
import com.app.nextflix.models.Movie;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private final MovieRepository movieRepository;
    private final MutableLiveData<List<Movie>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private String currentQuery = "";

    public SearchViewModel(Application application, MovieRepository movieRepository) {
        super(application);
        this.movieRepository = movieRepository;
    }

    public void searchMovies(String query) {
        if (query == null || query.trim().isEmpty()) {
            searchResults.setValue(null);
            return;
        }

        currentQuery = query;
        isLoading.setValue(true);
        error.setValue(null);

        movieRepository.searchMovies(query, new MovieRepository.SearchMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                searchResults.setValue(movies);
                isLoading.setValue(false);
            }

            @Override
            public void onError(String message) {
                error.setValue(message);
                isLoading.setValue(false);
            }
        });
    }

    public LiveData<List<Movie>> getSearchResults() {
        return searchResults;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public String getCurrentQuery() {
        return currentQuery;
    }
}