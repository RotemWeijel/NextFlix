package com.app.nextflix.ui.auth.login;

import com.app.nextflix.models.User;

public class LoginResponse {
    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}