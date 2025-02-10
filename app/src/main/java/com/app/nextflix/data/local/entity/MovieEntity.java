package com.app.nextflix.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.app.nextflix.data.local.converter.Converters;
import com.app.nextflix.models.Movie;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

@Entity(tableName = "movies",
        indices = {
                @Index("id"),
                @Index("name")
        })
@TypeConverters({Converters.class})
public class MovieEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String description;
    private int duration;
    private int releaseYear;
    private long addedAtTimestamp;
    private String actorsJson;
    private String categoriesJson;
    private String watchedByJson;
    private int ageAllow;
    private String director;
    private String language;
    private String imageUrl;
    private String trailerUrl;
    private String videoUrl;


    public static MovieEntity fromMovie(Movie movie) {
        Gson gson = new Gson();
        MovieEntity entity = new MovieEntity();
        if (movie.getId() == null) {
            throw new IllegalArgumentException("Movie must have an ID");
        }
        entity.setId(movie.getId());
        entity.setName(movie.getName());
        entity.setDescription(movie.getDescription());
        entity.setDuration(movie.getDuration());
        entity.setReleaseYear(movie.getReleaseYear());
        entity.setAddedAtTimestamp(movie.getAddedAt() != null ?
                movie.getAddedAt().getTime() : new Date().getTime());
        entity.setActorsJson(gson.toJson(movie.getActors()));
        entity.setCategoriesJson(gson.toJson(movie.getCategories()));
        entity.setWatchedByJson(gson.toJson(movie.getWatchedBy()));
        entity.setAgeAllow(movie.getAgeAllow());
        entity.setDirector(movie.getDirector());
        entity.setLanguage(movie.getLanguage());
        entity.setImageUrl(movie.getImageUrl() != null ? movie.getImageUrl().trim() : null);
        entity.setTrailerUrl(movie.getTrailerUrl() != null ? movie.getTrailerUrl().trim() : null);
        entity.setVideoUrl(movie.getVideoUrl() != null ? movie.getVideoUrl().trim() : null);
        return entity;
    }

    public Movie toMovie() {
        Gson gson = new Gson();
        Movie movie = new Movie();

        movie.setId(this.id);
        movie.setName(this.name);
        movie.setDescription(this.description);
        movie.setDuration(this.duration);
        movie.setReleaseYear(this.releaseYear);
        movie.setAddedAt(new Date(this.addedAtTimestamp));
        movie.setActors(gson.fromJson(this.actorsJson,
                new TypeToken<List<Movie.Actor>>(){}.getType()));
        movie.setCategories(gson.fromJson(this.categoriesJson,
                new TypeToken<List<String>>(){}.getType()));
        movie.setWatchedBy(gson.fromJson(this.watchedByJson,
                new TypeToken<List<Movie.WatchedBy>>(){}.getType()));
        movie.setAgeAllow(this.ageAllow);
        movie.setDirector(this.director);
        movie.setLanguage(this.language);
        movie.setImageUrl(this.imageUrl != null ? this.imageUrl.trim() : null);
        movie.setTrailerUrl(this.trailerUrl != null ? this.trailerUrl.trim() : null);
        movie.setVideoUrl(this.videoUrl != null ? this.videoUrl.trim() : null);
        return movie;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public long getAddedAtTimestamp() {
        return addedAtTimestamp;
    }

    public void setAddedAtTimestamp(long addedAtTimestamp) {
        this.addedAtTimestamp = addedAtTimestamp;
    }

    public String getActorsJson() {
        return actorsJson;
    }

    public void setActorsJson(String actorsJson) {
        this.actorsJson = actorsJson;
    }

    public String getCategoriesJson() {
        return categoriesJson;
    }

    public void setCategoriesJson(String categoriesJson) {
        this.categoriesJson = categoriesJson;
    }

    public String getWatchedByJson() {
        return watchedByJson;
    }

    public void setWatchedByJson(String watchedByJson) {
        this.watchedByJson = watchedByJson;
    }

    public int getAgeAllow() {
        return ageAllow;
    }

    public void setAgeAllow(int ageAllow) {
        this.ageAllow = ageAllow;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}