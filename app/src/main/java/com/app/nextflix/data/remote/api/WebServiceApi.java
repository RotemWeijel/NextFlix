package com.app.nextflix.data.remote.api;

import com.app.nextflix.models.Movie;
import com.app.nextflix.models.MovieCategory;
import com.app.nextflix.ui.admin.movies.UploadResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface WebServiceApi {
    @Headers("Content-Type: application/json")
    @GET("/api/movies")
    Call<List<MovieCategory>> getAllMovies();

    @Headers("Content-Type: application/json")
    @GET("/api/movies/{id}")
    Call<Movie> getMovie(@Path("id") String movieId);

    @Headers("Content-Type: application/json")
    @POST("/api/movies")
    Call<Movie> createMovie(@Body Movie movie);

    @Headers("Content-Type: application/json")
    @PUT("/api/movies/{id}")
    Call<Void> updateMovie(@Path("id") String movieId, @Body Movie movie);

    @DELETE("/api/movies/{id}")
    Call<Void> deleteMovie(@Path("id") String movieId);

    @GET("/api/movies/{id}/recommend")
    Call<List<Movie>> getRecommendedMovies(@Path("id") String movieId);

    @POST("/api/movies/{id}/recommend")
    Call<Void> markMovieAsWatched(@Path("id") String movieId);

    @GET("/api/movies/search/{query}")
    Call<List<Movie>> searchMovies(@Path("query") String query);

    @Multipart
    @POST("/api/upload/image")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);

    @Multipart
    @POST("/api/upload/trailer")
    Call<UploadResponse> uploadTrailer(@Part MultipartBody.Part trailer);

    @Multipart
    @POST("/api/upload/video")
    Call<UploadResponse> uploadVideo(@Part MultipartBody.Part video);
}