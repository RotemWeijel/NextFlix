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
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface WebServiceApi {
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer  eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2Nzk1Mjk3ZDEyZGE1MmY5NmQ4Y2FkZDUiLCJ1c2VybmFtZSI6InRlaGlsbGEiLCJpc0FkbWluIjp0cnVlLCJmdWxsX25hbWUiOiJSb21lbWEiLCJpYXQiOjE3Mzg4NDA2NjEsImV4cCI6MTczODg1NTA2MX0.pDsiEAt_w2M7lEye77hV8BkMnTde3hIJ54BJymtetkE"
    })
    @GET ("/api/movies")
    Call<List<MovieCategory>> getAllMovies();
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2Nzk1Mjk3ZDEyZGE1MmY5NmQ4Y2FkZDUiLCJ1c2VybmFtZSI6InRlaGlsbGEiLCJpc0FkbWluIjp0cnVlLCJmdWxsX25hbWUiOiJSb21lbWEiLCJpYXQiOjE3Mzg4NDA2NjEsImV4cCI6MTczODg1NTA2MX0.pDsiEAt_w2M7lEye77hV8BkMnTde3hIJ54BJymtetkE"
    })
    @GET("/api/movies/{id}")
    Call <Movie> getMovie(@Path("id") String movieId);
    @Headers({ "Content-Type: application/json",  "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2Nzk1Mjk3ZDEyZGE1MmY5NmQ4Y2FkZDUiLCJ1c2VybmFtZSI6InRlaGlsbGEiLCJpc0FkbWluIjp0cnVlLCJmdWxsX25hbWUiOiJSb21lbWEiLCJpYXQiOjE3Mzg5MTE0MjksImV4cCI6MTczODkyNTgyOX0.LdHh9Yr_8HLZKMsbbqihx43p1hUhf1cPSMFy2tXLd3o"})
    @POST("/api/movies")
    Call<Movie> createMovie(@Body Movie movie);
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2Nzk1Mjk3ZDEyZGE1MmY5NmQ4Y2FkZDUiLCJ1c2VybmFtZSI6InRlaGlsbGEiLCJpc0FkbWluIjp0cnVlLCJmdWxsX25hbWUiOiJSb21lbWEiLCJpYXQiOjE3Mzg5MTE0MjksImV4cCI6MTczODkyNTgyOX0.LdHh9Yr_8HLZKMsbbqihx43p1hUhf1cPSMFy2tXLd3o"    })
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
            "Authorization: Bearer 1e7c520c1ed064dc0b7e50c0b2bef5cf"
    })
    @GET("/api/movies/{id}/recommend")
    Call<List<Movie>> getRecommendedMovies(@Path("id") String movieId);
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 6431820a048e3f5f75604a9c9e62bef4"
    })

    @POST("/api/movies/{id}/recommend")
    Call<Void> markMovieAsWatched(@Path("id") String movieId);
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 6431820a048e3f5f75604a9c9e62bef4"
    })
    @GET("/api/movies/search/{query}")
    Call<List<Movie>> searchMovies(@Path("query") String query);


    @Headers({
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2Nzk1Mjk3ZDEyZGE1MmY5NmQ4Y2FkZDUiLCJ1c2VybmFtZSI6InRlaGlsbGEiLCJpc0FkbWluIjp0cnVlLCJmdWxsX25hbWUiOiJSb21lbWEiLCJpYXQiOjE3Mzg5MTE0MjksImV4cCI6MTczODkyNTgyOX0.LdHh9Yr_8HLZKMsbbqihx43p1hUhf1cPSMFy2tXLd3o"    })
    @Multipart
    @POST("/api/upload/image")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);

    @Headers({
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2Nzk1Mjk3ZDEyZGE1MmY5NmQ4Y2FkZDUiLCJ1c2VybmFtZSI6InRlaGlsbGEiLCJpc0FkbWluIjp0cnVlLCJmdWxsX25hbWUiOiJSb21lbWEiLCJpYXQiOjE3Mzg5MTE0MjksImV4cCI6MTczODkyNTgyOX0.LdHh9Yr_8HLZKMsbbqihx43p1hUhf1cPSMFy2tXLd3o"    })
    @Multipart
    @POST("/api/upload/trailer")
    Call<UploadResponse> uploadTrailer(@Part MultipartBody.Part trailer);

    @Headers({
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2Nzk1Mjk3ZDEyZGE1MmY5NmQ4Y2FkZDUiLCJ1c2VybmFtZSI6InRlaGlsbGEiLCJpc0FkbWluIjp0cnVlLCJmdWxsX25hbWUiOiJSb21lbWEiLCJpYXQiOjE3Mzg5MTE0MjksImV4cCI6MTczODkyNTgyOX0.LdHh9Yr_8HLZKMsbbqihx43p1hUhf1cPSMFy2tXLd3o"
    })
    @Multipart
    @POST("/api/upload/video")
    Call<UploadResponse> uploadVideo(@Part MultipartBody.Part video);
}

