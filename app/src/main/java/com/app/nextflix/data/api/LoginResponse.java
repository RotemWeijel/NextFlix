// LoginResponse.java
package com.app.nextflix.data.api;

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