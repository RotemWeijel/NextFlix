package com.app.nextflix.models;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieCategory {
    @SerializedName("categoryId")
    private String id;

    @SerializedName("categoryName")
    private String name;

    @SerializedName("promoted")
    private boolean promoted;

    @SerializedName("movies")
    private List<Movie> movies;

    // Constructor
    public MovieCategory() {
        this.name = "Unknown";
    }

    // Getters and Setters
    public String getId() {
        return id != null ? id : "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name : "Unknown";
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public List<Movie> getMovies() {
        return movies != null ? movies : new ArrayList<>();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}