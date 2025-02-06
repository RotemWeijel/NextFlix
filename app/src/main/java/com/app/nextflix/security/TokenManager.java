package com.app.nextflix.security;

// Temporary mock for testing
public class TokenManager {
    public String getToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2Nzk1Mjk3ZDEyZGE1MmY5NmQ4Y2FkZDUiLCJ1c2VybmFtZSI6InRlaGlsbGEiLCJpc0FkbWluIjp0cnVlLCJmdWxsX25hbWUiOiJSb21lbWEiLCJpYXQiOjE3Mzg4MjYwMTksImV4cCI6MTczODg0MDQxOX0.g93E7OwfVykFgHFhekbvfa6oARH9vFTWjwVXjgEMbrs";
    }

    public String getAuthorizationHeader() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2Nzk1Mjk3ZDEyZGE1MmY5NmQ4Y2FkZDUiLCJ1c2VybmFtZSI6InRlaGlsbGEiLCJpc0FkbWluIjp0cnVlLCJmdWxsX25hbWUiOiJSb21lbWEiLCJpYXQiOjE3Mzg4MjYwMTksImV4cCI6MTczODg0MDQxOX0.g93E7OwfVykFgHFhekbvfa6oARH9vFTWjwVXjgEMbrs";
    }

    public boolean hasValidToken() {
        return true; // Always returns true for testing
    }
}
