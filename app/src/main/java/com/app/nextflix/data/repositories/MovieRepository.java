
package com.app.nextflix.data.repositories;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.app.nextflix.data.local.dao.MovieDao;
import com.app.nextflix.data.local.entity.MovieEntity;
import com.app.nextflix.data.remote.api.MovieApi;
import com.app.nextflix.models.Movie;
import com.app.nextflix.models.MovieCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public interface MoviesCallback {
        void onMoviesLoaded(List<MovieCategory> movies, String errorMessage);
    }

    public void getMovie(String movieId, MovieCallback callback) {
        new Thread(() -> {
            try {
                // First try local cache
                MovieEntity cachedMovie = movieDao.getMovie(movieId);
                if (cachedMovie != null) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Log.d(TAG, "Returning cached movie: " + cachedMovie.getName());
                        callback.onSuccess(cachedMovie.toMovie());
                    });
                }

                // Then get fresh data
                movieApi.getMovie(movieId, new MovieCallback() {
                    @Override
                    public void onSuccess(Movie movie) {
                        new Thread(() -> {
                            try {
                                movieDao.insert(MovieEntity.fromMovie(movie));
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    Log.d(TAG, "Success getting movie: " + movie.getName());
                                    callback.onSuccess(movie);
                                });
                            } catch (Exception e) {
                                Log.e(TAG, "Error saving to database", e);
                                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(movie));
                            }
                        }).start();
                    }

                    @Override
                    public void onError(String message) {
                        if (cachedMovie == null) {
                            Log.e(TAG, "Error getting movie: " + message);
                            callback.onError(message);
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error in getMovie", e);
                callback.onError("Error: " + e.getMessage());
            }
        }).start();
    }

    public void getAllMovies(MovieApi.MoviesCallback callback) {
        new Thread(() -> {
            try {
                // First try local cache
                List<MovieEntity> cachedMovies = movieDao.getAllMovies();
                if (!cachedMovies.isEmpty()) {
                    List<MovieCategory> categories = convertToCategories(cachedMovies);
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onMoviesLoaded(categories, null)
                    );
                }

                // Then get fresh data
                movieApi.getAllMovies(new MovieApi.MoviesCallback() {
                    @Override
                    public void onMoviesLoaded(List<MovieCategory> movies, String errorMessage) {
                        if (errorMessage != null) {
                            if (cachedMovies.isEmpty()) {
                                callback.onMoviesLoaded(null, errorMessage);
                            }
                            return;
                        }

                        new Thread(() -> {
                            try {
                                movieDao.deleteAll();
                                for (MovieCategory category : movies) {
                                    if (category != null && category.getMovies() != null) {
                                        for (Movie movie : category.getMovies()) {
                                            movieDao.insert(MovieEntity.fromMovie(movie));
                                        }
                                    }
                                }
                                new Handler(Looper.getMainLooper()).post(() ->
                                        callback.onMoviesLoaded(movies, null)
                                );
                            } catch (Exception e) {
                                Log.e(TAG, "Error saving to database", e);
                                new Handler(Looper.getMainLooper()).post(() ->
                                        callback.onMoviesLoaded(movies, null)
                                );
                            }
                        }).start();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error in getAllMovies", e);
                callback.onMoviesLoaded(null, "Error: " + e.getMessage());
            }
        }).start();
    }

    public void getRecommendedMovies(String movieId, RecommendedMoviesCallback callback) {
        new Thread(() -> {
            try {
                // First try local cache
                List<MovieEntity> cachedMovies = movieDao.getAllMovies();
                if (!cachedMovies.isEmpty()) {
                    List<Movie> movies = new ArrayList<>();
                    for (MovieEntity entity : cachedMovies) {
                        movies.add(entity.toMovie());
                    }
                    Collections.shuffle(movies);
                    List<Movie> recommendations = movies.subList(0, Math.min(12, movies.size()));
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onSuccess(recommendations)
                    );
                }

                // Then get fresh recommendations
                movieApi.getRecommendedMovies(movieId, new RecommendedMoviesCallback() {
                    @Override
                    public void onSuccess(List<Movie> movies) {
                        new Thread(() -> {
                            try {
                                for (Movie movie : movies) {
                                    movieDao.insert(MovieEntity.fromMovie(movie));
                                }
                                new Handler(Looper.getMainLooper()).post(() ->
                                        callback.onSuccess(movies)
                                );
                            } catch (Exception e) {
                                Log.e(TAG, "Error saving to database", e);
                                new Handler(Looper.getMainLooper()).post(() ->
                                        callback.onSuccess(movies)
                                );
                            }
                        }).start();
                    }

                    @Override
                    public void onError(String message) {
                        if (cachedMovies.isEmpty()) {
                            Log.e(TAG, "Error getting recommendations: " + message);
                            callback.onError(message);
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error in getRecommendedMovies", e);
                callback.onError("Error: " + e.getMessage());
            }
        }).start();
    }

    private List<MovieCategory> convertToCategories(List<MovieEntity> movies) {
        Map<String, List<Movie>> categoryMap = new HashMap<>();
        for (MovieEntity entity : movies) {
            Movie movie = entity.toMovie();
            for (String category : movie.getCategories()) {
                categoryMap.computeIfAbsent(category, k -> new ArrayList<>()).add(movie);
            }
        }

        List<MovieCategory> categories = new ArrayList<>();
        for (Map.Entry<String, List<Movie>> entry : categoryMap.entrySet()) {
            MovieCategory category = new MovieCategory();
            category.setName(entry.getKey());
            category.setMovies(entry.getValue());
            categories.add(category);
        }
        return categories;
    }
}