package com.app.nextflix.models;


import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieCategory {
    @SerializedName("movies")
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }
}

