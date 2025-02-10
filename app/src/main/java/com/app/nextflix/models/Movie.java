package com.app.nextflix.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Movie {
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    private int duration;
    private int releaseYear;
    private Date addedAt;
    private List<Actor> actors;
    private List<String> categories;
    private List<WatchedBy> watchedBy;
    private int ageAllow;
    private String director;
    private String language;
    private String imageUrl;
    private String trailerUrl;
    private String videoUrl;

    public static class Actor {
        private String name;

        public Actor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name != null ? name.trim() : null;
        }
    }

    public static class WatchedBy {
        private String userId;  // Changed to String to match MongoDB ObjectId
        private Date watchedAt;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Date getWatchedAt() {
            return watchedAt;
        }

        public void setWatchedAt(Date watchedAt) {
            this.watchedAt = watchedAt != null ? watchedAt : new Date();
        }
    }


    // Constructors
    public Movie() {
        this.addedAt = new Date(); // Initialize with current date
    }

    public Movie(String id, String name, String description, int duration, int releaseYear, List<Actor> actors, List<String> categories, int ageAllow, String director, String language, String imageUrl, String trailerUrl, String videoUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.addedAt = new Date();
        this.actors = actors;
        this.categories = categories;
        this.ageAllow = ageAllow;
        this.director = director;
        this.language = language;
        this.imageUrl = imageUrl;
        this.trailerUrl = trailerUrl;
        this.videoUrl = videoUrl;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description.trim() : null;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = Math.max(1, duration); // Ensure minimum duration of 1
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt != null ? addedAt : new Date();
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<WatchedBy> getWatchedBy() {
        return watchedBy;
    }

    public void setWatchedBy(List<WatchedBy> watchedBy) {
        this.watchedBy = watchedBy;
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
        this.director = director != null ? director.trim() : null;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language != null ? language.trim() : null;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl != null ? imageUrl.trim() : null;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl != null ? trailerUrl.trim() : null;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl != null ? videoUrl.trim() : null;
    }
}