package com.app.nextflix.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import com.app.nextflix.models.User;
import com.google.gson.Gson;

public class UserPreferences {
    private static UserPreferences instance;
    private final SharedPreferences preferences;
    private final Gson gson;
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER = "current_user";

    private UserPreferences(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized UserPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new UserPreferences(context.getApplicationContext());
        }
        return instance;
    }

    public void saveUser(User user) {
        preferences.edit().putString(KEY_USER, gson.toJson(user)).apply();
    }

    public User getUser() {
        String userJson = preferences.getString(KEY_USER, null);
        return userJson != null ? gson.fromJson(userJson, User.class) : null;
    }

    public void clearUser() {
        preferences.edit().remove(KEY_USER).apply();
    }
}