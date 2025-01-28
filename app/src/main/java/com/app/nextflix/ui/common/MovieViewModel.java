package com.app.nextflix.ui.common;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.nextflix.data.remote.api.MovieApi;
import com.app.nextflix.data.repositories.MovieRepository;
import com.app.nextflix.models.Movie;

import java.util.List;




public class MovieViewModel extends ViewModel {
    private MovieApi movieApi;
    private MovieRepository movieRepository;
    private final MutableLiveData<Movie> movieData = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> recommendedMovies = new MutableLiveData<>();

    public void init(Context context) {
        movieApi = new MovieApi(context);
        movieRepository = new MovieRepository(movieApi, movieApi.getMovieDao());
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
