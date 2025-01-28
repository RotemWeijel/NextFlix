package com.app.nextflix.data.remote.api;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.app.nextflix.data.local.AppDatabase;
import com.app.nextflix.data.local.dao.MovieDao;
import com.app.nextflix.data.repositories.MovieRepository.MovieCallback;
import com.app.nextflix.data.repositories.MovieRepository.RecommendedMoviesCallback;
import com.app.nextflix.models.Movie;
import com.app.nextflix.models.MovieCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieApi {
    private final WebServiceApi api;
    private final MovieDao movieDao;

    public MovieApi(Context context) {
        String base;
        if (isEmulator()) {
            base = "http://10.0.2.2:4000/";
        } else {
            base = "http://192.168.7.3:4000/";
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.api = retrofit.create(WebServiceApi.class);
        this.movieDao = AppDatabase.getInstance(context).movieDao();
    }
    private boolean isEmulator() {
        return Build.PRODUCT.contains("sdk") ||
                Build.PRODUCT.contains("gphone") ||
                Build.PRODUCT.contains("emulator") ||
                Build.FINGERPRINT.contains("generic") ||
                Build.MANUFACTURER.contains("Genymotion");
    }

    public void getMovie(String movieId, MovieCallback callback) {
        Log.d("MovieApi", "Getting movie ID: " + movieId);

        api.getMovie(movieId).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("MovieApi", "Success getting movie: " + response.body().getName());
                    callback.onSuccess(response.body());
                } else {
                    String error = "Failed to get movie: " + response.code();
                    Log.e("MovieApi", error);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                String error = "Network error: " + t.getMessage();
                Log.e("MovieApi", error);
                callback.onError(error);
            }
        });
    }

    public void getRecommendedMovies(String movieId, RecommendedMoviesCallback callback) {
        Log.d("MovieApi", "Starting getRecommendedMovies call with ID: " + movieId);

        api.getRecommendedMovies(movieId).enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    api.getAllMovies().enqueue(new Callback<List<MovieCategory>>() {
                        @Override
                        public void onResponse(Call<List<MovieCategory>> call, Response<List<MovieCategory>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Movie> allMovies = new ArrayList<>();
                                for (MovieCategory category : response.body()) {
                                    if (category.getMovies() != null) {
                                        allMovies.addAll(category.getMovies());
                                    }
                                }
                                Collections.shuffle(allMovies);
                                callback.onSuccess(allMovies.subList(0, Math.min(12, allMovies.size())));
                            } else {
                                callback.onError("Failed to get movies: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<MovieCategory>> call, Throwable t) {
                            callback.onError("Network error: " + t.getMessage());
                        }
                    });
                } else {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public MovieDao getMovieDao() {
        return movieDao;
    }
}
