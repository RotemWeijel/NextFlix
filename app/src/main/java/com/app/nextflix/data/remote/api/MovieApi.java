package com.app.nextflix.data.remote.api;

import android.content.Context;
import android.os.Build;
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
    private static final String TAG = "MovieApi";

    public interface MoviesCallback {
        void onMoviesLoaded(List<MovieCategory> movies, String errorMessage);
    }

    public MovieApi(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        String base;
        if (isEmulator()) {
            base = "http://10.0.2.2:4000/";
        } else {
            base = "http://192.168.7.3:4000/";
        }

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(base)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            this.api = retrofit.create(WebServiceApi.class);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing MovieApi", e);
            throw new RuntimeException("Failed to initialize MovieApi", e);
        }
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


}