package com.app.nextflix.data.repositories;

import android.util.Log;
import com.app.nextflix.data.local.dao.MovieDao;
import com.app.nextflix.data.local.entity.MovieEntity;
import com.app.nextflix.data.remote.api.MovieApi;
import com.app.nextflix.models.Movie;
import java.util.List;

public class MovieRepository {
    private final MovieApi movieApi;
    private final MovieDao movieDao;
    private static final String TAG = "MovieRepository";

    public MovieRepository(MovieApi movieApi, MovieDao movieDao) {
        this.movieApi = movieApi;
        this.movieDao = movieDao;
    }

    public interface MovieCallback {
        void onSuccess(Movie movie);
        void onError(String message);
    }

    public interface RecommendedMoviesCallback {
        void onSuccess(List<Movie> movies);
        void onError(String message);
    }

    public void getMovie(String movieId, MovieCallback callback) {
        Log.d(TAG, "Getting movie ID: " + movieId);

        movieApi.getMovie(movieId, new MovieCallback() {
            @Override
            public void onSuccess(Movie movie) {
                Log.d(TAG, "Success getting movie: " + movie.getName());
                callback.onSuccess(movie);
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "Error getting movie: " + message);
                callback.onError(message);
            }
        });
    }

    public void getRecommendedMovies(String movieId, RecommendedMoviesCallback callback) {
        Log.d(TAG, "Getting recommended movies for ID: " + movieId);

        movieApi.getRecommendedMovies(movieId, new RecommendedMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                Log.d(TAG, "Success getting recommendations. Count: " + movies.size());
                for (Movie movie : movies) {
                    movieDao.insert(MovieEntity.fromMovie(movie));
                }
                callback.onSuccess(movies);
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "Error getting recommendations: " + message);
                callback.onError(message);
            }
        });
    }
}