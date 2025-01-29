package com.app.nextflix.ui.common;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.nextflix.data.local.AppDatabase;
import com.app.nextflix.data.local.dao.MovieDao;
import com.app.nextflix.data.remote.api.MovieApi;
import com.app.nextflix.data.repositories.MovieRepository;
import com.app.nextflix.models.Movie;
import com.app.nextflix.models.MovieCategory;

import java.util.List;
import java.util.Random;

public class CategoryMoviesViewModel extends ViewModel {
    private MovieRepository movieRepository;
    private final MutableLiveData<List<MovieCategory>> categorizedMovies = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Movie> heroMovie = new MutableLiveData<>();

    public void init(Context context) {
        MovieApi movieApi = new MovieApi(context);
        MovieDao movieDao = AppDatabase.getInstance(context).movieDao();
        movieRepository = new MovieRepository(movieApi, movieDao);
        loadCategorizedMovies();
    }

    public void loadCategorizedMovies() {
        loading.setValue(true);

        if (movieRepository == null) {
            error.setValue("MovieRepository not initialized");
            loading.setValue(false);
            return;
        }

        movieRepository.getAllMovies((movies, errorMessage) -> {
            if (errorMessage != null) {
                error.setValue(errorMessage);
            } else if (movies != null) {
                categorizedMovies.setValue(movies);
                if (!movies.isEmpty() && movies.get(0) != null &&
                        movies.get(0).getMovies() != null &&
                        !movies.get(0).getMovies().isEmpty()) {
                    loadHeroMovie(movies.get(0).getMovies());
                }
            }
            loading.setValue(false);
        });
    }

    private void loadHeroMovie(List<Movie> movies) {
        if (movies.isEmpty()) return;
        int randomIndex = new Random().nextInt(movies.size());
        Movie selectedMovie = movies.get(randomIndex);
        heroMovie.setValue(selectedMovie);
    }

    public MutableLiveData<List<MovieCategory>> getCategorizedMovies() {
        return categorizedMovies;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Movie> getHeroMovie() {
        return heroMovie;
    }
}