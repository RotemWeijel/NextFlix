package com.app.nextflix.models;

import java.util.Date;
import java.util.List;

public class Movie {
    private String id;
    private String name;
    private String description;
    private int duration;
    private int releaseYear;
    private Date addedAt;
    private List<Actor> actors;
    private List<String> categories;  // Category IDs
    private List<WatchedBy> watchedBy;
    private int ageAllow;
    private String director;
    private String language;
    private String imageUrl;
    private String trailerUrl;
    private String videoUrl;

    // Inner classes for nested objects
    public static class Actor {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class WatchedBy {
        private String userId;
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
            this.watchedAt = watchedAt;
        }
    }

    // Constructors
    public Movie() {
        this.addedAt = new Date();
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

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
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