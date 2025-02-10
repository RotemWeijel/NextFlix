package com.app.nextflix.data.repositories;

import android.content.Context;
import com.app.nextflix.data.local.UserPreferences;
import com.app.nextflix.models.User;

public class UserRepository {
    private final UserPreferences userPreferences;
    private static UserRepository instance;

    private UserRepository(Context context) {
        userPreferences = UserPreferences.getInstance(context);
    }

    public static synchronized UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context.getApplicationContext());
        }
        return instance;
    }

    public void saveUser(User user) {
        userPreferences.saveUser(user);
    }

    public User getCurrentUser() {
        return userPreferences.getUser();
    }

    public void clearCurrentUser() {
        userPreferences.clearUser();
    }
}