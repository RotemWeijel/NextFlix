package com.app.nextflix.data.remote.api;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.app.nextflix.data.local.AppDatabase;
import com.app.nextflix.data.local.dao.MovieDao;
import com.app.nextflix.data.local.entity.MovieEntity;
import com.app.nextflix.data.repositories.MovieRepository.MovieCallback;
import com.app.nextflix.data.repositories.MovieRepository.RecommendedMoviesCallback;
import com.app.nextflix.models.Movie;
import com.app.nextflix.models.MovieCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieApi {
    private final WebServiceApi api;
    private final MovieDao movieDao;
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
            this.movieDao = AppDatabase.getInstance(context).movieDao();

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

    public void getMovie(String movieId, MovieCallback callback) {
        new Thread(() -> {
            try {
                MovieEntity cachedMovie = movieDao.getMovie(movieId);
                if (cachedMovie != null) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        callback.onSuccess(cachedMovie.toMovie());
                    });
                }

                api.getMovie(movieId).enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NonNull Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            new Thread(() -> {
                                try {
                                    MovieEntity entity = MovieEntity.fromMovie(response.body());
                                    movieDao.insert(entity);
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        callback.onSuccess(response.body());
                                    });
                                } catch (Exception e) {
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        callback.onError("Database error: " + e.getMessage());
                                    });
                                }
                            }).start();
                        } else {
                            callback.onError("Failed to get movie: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                        callback.onError("Network error: " + t.getMessage());
                    }
                });
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        callback.onError("Error: " + e.getMessage())
                );
            }
        }).start();
    }

    public void getAllMovies(MoviesCallback callback) {
        new Thread(() -> {
            try {
                List<MovieEntity> cachedMovies = movieDao.getAllMovies();
                if (!cachedMovies.isEmpty()) {
                    List<MovieCategory> categories = convertToCategories(cachedMovies);
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onMoviesLoaded(categories, null)
                    );
                }

                api.getAllMovies().enqueue(new Callback<List<MovieCategory>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<MovieCategory>> call, Response<List<MovieCategory>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            new Thread(() -> {
                                try {
                                    movieDao.deleteAll();
                                    for (MovieCategory category : response.body()) {
                                        if (category != null && category.getMovies() != null) {
                                            for (Movie movie : category.getMovies()) {
                                                if (movie != null) {
                                                    movieDao.insert(MovieEntity.fromMovie(movie));
                                                }
                                            }
                                        }
                                    }
                                    new Handler(Looper.getMainLooper()).post(() ->
                                            callback.onMoviesLoaded(response.body(), null)
                                    );
                                } catch (Exception e) {
                                    new Handler(Looper.getMainLooper()).post(() ->
                                            callback.onMoviesLoaded(null, "Database error: " + e.getMessage())
                                    );
                                }
                            }).start();
                        } else {
                            callback.onMoviesLoaded(null, "Server error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<MovieCategory>> call, @NonNull Throwable t) {
                        callback.onMoviesLoaded(null, "Network error: " + t.getMessage());
                    }
                });
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        callback.onMoviesLoaded(null, "Error: " + e.getMessage())
                );
            }
        }).start();
    }

    public void getRecommendedMovies(String movieId, RecommendedMoviesCallback callback) {
        new Thread(() -> {
            try {
                List<MovieEntity> cachedMovies = movieDao.getAllMovies();
                if (!cachedMovies.isEmpty()) {
                    List<Movie> movies = new ArrayList<>();
                    for (MovieEntity entity : cachedMovies) {
                        movies.add(entity.toMovie());
                    }
                    Collections.shuffle(movies);
                    List<Movie> recommendations = movies.subList(0, Math.min(12, movies.size()));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        callback.onSuccess(recommendations);
                    });
                }

                api.getRecommendedMovies(movieId).enqueue(new Callback<List<Movie>>() {
                    @Override
                    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            new Thread(() -> {
                                try {
                                    for (Movie movie : response.body()) {
                                        movieDao.insert(MovieEntity.fromMovie(movie));
                                    }
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        callback.onSuccess(response.body());
                                    });
                                } catch (Exception e) {
                                    fallbackToRandomRecommendations(callback);
                                }
                            }).start();
                        } else {
                            fallbackToRandomRecommendations(callback);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Movie>> call, Throwable t) {
                        fallbackToRandomRecommendations(callback);
                    }
                });
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        callback.onError("Error: " + e.getMessage())
                );
            }
        }).start();
    }

    private void fallbackToRandomRecommendations(RecommendedMoviesCallback callback) {
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
                    callback.onError("Failed to get recommendations");
                }
            }

            @Override
            public void onFailure(Call<List<MovieCategory>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
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

    public MovieDao getMovieDao() {
        return movieDao;
    }
}