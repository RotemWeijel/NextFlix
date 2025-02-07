package com.app.nextflix.data.remote.api;

import com.app.nextflix.models.Movie;
import com.app.nextflix.models.MovieCategory;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceApi {
    @GET("/api/movies")
    Call<List<MovieCategory>> getAllMovies();

    @GET("/api/movies/{id}")
    Call<Movie> getMovie(@Path("id") String movieId);

    @POST("/api/movies")
    Call<Void> createMovie(@Body Movie movie);

    @PATCH("/api/movies/{id}")
    Call<Void> UpdateMovie(@Path("id") String movieId, @Body Movie movie);

    @DELETE("/api/movies/{id}")
    Call<Void> DeleteMovie(@Path("id") String movieId);

    @GET("/api/movies/{id}/recommend")
    Call<List<Movie>> getRecommendedMovies(@Path("id") String movieId);

    @POST("/api/movies/{id}/recommend")
    Call<Void> markMovieAsWatched(@Path("id") String movieId);

    @GET("/api/movies/search/{query}")
    Call<List<Movie>> searchMovies(@Path("query") String query);
}