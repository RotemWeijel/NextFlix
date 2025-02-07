package com.app.nextflix.data.api;

import java.util.ArrayList;
import java.util.List;

public class RegistrationRequest {
    private String username;
    private String password;
    private String full_name;
    private String picture;
    private boolean isAdmin;
    private List<String> watchedMovies;

    public RegistrationRequest(String username, String password, String fullName, String picture) {
        this.username = username;
        this.password = password;
        this.full_name = fullName;
        this.picture = picture;
        this.isAdmin = false;
        this.watchedMovies = new ArrayList<>();
    }

    // Getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<String> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(List<String> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }
}