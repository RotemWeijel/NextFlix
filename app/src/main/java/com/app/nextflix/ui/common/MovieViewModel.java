package com.app.nextflix.ui.common;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.nextflix.data.local.AppDatabase;
import com.app.nextflix.data.local.dao.MovieDao;
import com.app.nextflix.data.remote.api.MovieApi;
import com.app.nextflix.data.repositories.MovieRepository;
import com.app.nextflix.models.Movie;

import java.util.List;




public class MovieViewModel extends ViewModel {
    private MovieRepository movieRepository;
    private final MutableLiveData<Movie> movieData = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> recommendedMovies = new MutableLiveData<>();

    public void init(Context context) {
        // Initialize MovieApi
        MovieApi movieApi = new MovieApi(context);

        // Get MovieDao from AppDatabase directly
        MovieDao movieDao = AppDatabase.getInstance(context).movieDao();

        // Initialize Repository with both dependencies
        movieRepository = new MovieRepository(movieApi, movieDao);
    }

    public MutableLiveData<Movie> getMovieData() {
        return movieData;
    }

    public MutableLiveData<List<Movie>> getRecommendedMovies() {
        return recommendedMovies;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public void loadMovie(String movieId) {
        loading.setValue(true);

        if (movieRepository == null) {
            error.postValue("Repository not initialized");
            return;
        }

        movieRepository.getMovie(movieId, new MovieRepository.MovieCallback() {
            @Override
            public void onSuccess(Movie movie) {
                movieData.postValue(movie);
                loading.setValue(false);
            }

            @Override
            public void onError(String message) {
                error.postValue(message);
                loading.setValue(false);
            }
        });

        movieRepository.getRecommendedMovies(movieId, new MovieRepository.RecommendedMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                recommendedMovies.postValue(movies);
            }

            @Override
            public void onError(String message) {
                error.postValue(message);
            }
        });
    }
}
