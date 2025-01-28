package com.app.nextflix.data.remote.api;

import android.content.Context;
import android.util.Log;
import com.app.nextflix.data.local.AppDatabase;
import com.app.nextflix.data.local.dao.MovieDao;
import com.app.nextflix.data.repositories.MovieRepository.MovieCallback;
import com.app.nextflix.data.repositories.MovieRepository.RecommendedMoviesCallback;
import com.app.nextflix.models.Movie;
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


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:4000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.api = retrofit.create(WebServiceApi.class);
        this.movieDao = AppDatabase.getInstance(context).movieDao();
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
        api.getRecommendedMovies(movieId).enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get recommendations: " + response.code());
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
