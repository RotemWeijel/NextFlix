package com.app.nextflix.data.remote.api;

import com.app.nextflix.models.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceApi {
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 6431820a048e3f5f75604a9c9e62bef4"
    })
    @GET ("/api/movies")
    Call<List<Movie>> getAllMovies();
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 6431820a048e3f5f75604a9c9e62bef4"
    })
    @GET("/api/movies/{id}")
    Call <Movie> getMovie(@Path("id") String movieId);
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 6431820a048e3f5f75604a9c9e62bef4"
    })
    @POST("/api/movies")

    Call<Void> createMovie(@Body Movie movie);
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 6431820a048e3f5f75604a9c9e62bef4"
    })
    @PATCH("/api/movies/{id}")
    Call <Void> UpdateMovie(@Path("id") String movieId, @Body Movie movie);
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 6431820a048e3f5f75604a9c9e62bef4"
    })
    @DELETE("/api/movies/{id}")
    Call <Void> DeleteMovie(@Path("id")String movieId);
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 6431820a048e3f5f75604a9c9e62bef4"
    })
    @GET("/api/movies/{id}/recommend/")
    Call<List<Movie>> getRecommendedMovies(@Path("id") String movieId);
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 6431820a048e3f5f75604a9c9e62bef4"
    })

    @POST("/api/movies/{id}/recommend/")
    Call<Void> markMovieAsWatched(@Path("id") String movieId);
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 6431820a048e3f5f75604a9c9e62bef4"
    })
    @GET("/api/movies/search/{query}")
    Call<List<Movie>> searchMovies(@Path("query") String query);




}
