package com.app.nextflix.data.remote.api;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import com.app.nextflix.data.repositories.MovieRepository;
import com.app.nextflix.models.Movie;
import com.app.nextflix.models.MovieCategory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import retrofit2.Callback;
import retrofit2.Response;


public class MovieApi {
    private final WebServiceApi api;
    private static MovieApi instance;
    private static final String TAG = "MovieApi";

    public interface MoviesCallback {
        void onMoviesLoaded(List<MovieCategory> movies, String errorMessage);
    }

    public MovieApi(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        RetrofitClient.initialize(context);
        this.api = RetrofitClient.getClient().create(WebServiceApi.class);
    }

    private boolean isEmulator() {
        return Build.PRODUCT.contains("sdk") ||
                Build.PRODUCT.contains("gphone") ||
                Build.PRODUCT.contains("emulator") ||
                Build.FINGERPRINT.contains("generic") ||
                Build.MANUFACTURER.contains("Genymotion");
    }

    public void getMovie(String movieId, MovieRepository.MovieCallback callback) {
        if (movieId == null) {
            callback.onError("Movie ID cannot be null");
            return;
        }

        api.getMovie(movieId).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get movie: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getAllMovies(MoviesCallback callback) {
        Log.d("MovieApi", "Making API request to get all movies");
        api.getAllMovies().enqueue(new Callback<List<MovieCategory>>() {
            @Override
            public void onResponse(@NonNull Call<List<MovieCategory>> call, @NonNull Response<List<MovieCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("MovieApi", "Successfully got " + response.body().size() + " categories");
                    callback.onMoviesLoaded(response.body(), null);
                } else {
                    Log.e("MovieApi", "Server error: " + response.code());
                    callback.onMoviesLoaded(null, "Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MovieCategory>> call, @NonNull Throwable t) {
                Log.e("MovieApi", "Network error: " + t.getMessage());
                callback.onMoviesLoaded(null, "Network error: " + t.getMessage());
            }
        });
    }

    public void getRecommendedMovies(String movieId, MovieRepository.RecommendedMoviesCallback callback) {
        api.getRecommendedMovies(movieId).enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get recommendations: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    public void markMovieAsRecommended(String movieId, MovieRepository.RecommendCallback callback) {
        new Thread(() -> {
            try {
                api.markMovieAsWatched(movieId).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError("Failed to mark movie as recommended: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        callback.onError("Network error: " + t.getMessage());
                    }
                });
            } catch (Exception e) {
                callback.onError("Error: " + e.getMessage());
            }
        }).start();
    }
    public static synchronized MovieApi getInstance(Context context) {
        if (instance == null) {
            instance = new MovieApi(context);
        }
        return instance;
    }

    public WebServiceApi getWebServiceApi() {
        return api;
    }

    public interface SearchMoviesCallback {
        void onSuccess(List<Movie> movies);
        void onError(String message);
    }

    // Add this method to your MovieApi class
    public void searchMovies(String query, MovieRepository.SearchMoviesCallback callback) {
        if (query == null || query.trim().isEmpty()) {
            callback.onError("Search query cannot be empty");
            return;
        }

        api.searchMovies(query).enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Search successful, found " + response.body().size() + " movies");
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "Search failed with code: " + response.code());
                    callback.onError("Search failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                Log.e(TAG, "Search network error", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }


}