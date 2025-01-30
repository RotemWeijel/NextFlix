package com.app.nextflix.ui.common;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;


import android.animation.ArgbEvaluator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;



import com.app.nextflix.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavBarManager {
    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_IS_DARK_MODE = "isDarkMode";
    private Activity activity;
    private boolean isDarkMode;
    private SharedPreferences settings;
    private ImageButton themeToggleButton;

    public NavBarManager(Activity activity) {
        this.activity = activity;
        this.settings = activity.getSharedPreferences(PREFS_NAME, 0);
        this.isDarkMode = settings.getBoolean(KEY_IS_DARK_MODE, true);
        initializeTheme(); // נוסיף אתחול ראשוני של הרקע
    }

    private void initializeTheme() {
        View rootView = activity.findViewById(android.R.id.content);
        CoordinatorLayout mainLayout = rootView.findViewById(R.id.coordinator_layout);
        if (mainLayout != null) {
            mainLayout.setBackgroundColor(isDarkMode ?
                    Color.BLACK :
                    Color.parseColor("#f5eae7"));
        }
    }

    public void setupNavBars() {
        setupThemeToggle();
        setupBottomNav();
    }

    private void setupThemeToggle() {
        themeToggleButton = activity.findViewById(R.id.themeToggleButton);
        updateThemeIcon();

        themeToggleButton.setOnClickListener(v -> {
            toggleTheme();
        });
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        settings.edit().putBoolean(KEY_IS_DARK_MODE, isDarkMode).apply();
        updateThemeIcon();

        View rootView = activity.findViewById(android.R.id.content);
        CoordinatorLayout mainLayout = rootView.findViewById(R.id.coordinator_layout);
        if (mainLayout != null) {

            int colorFrom = isDarkMode ? Color.parseColor("#f5eae7") : Color.BLACK;
            int colorTo = isDarkMode ? Color.BLACK : Color.parseColor("#f5eae7");

            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(300);
            colorAnimation.addUpdateListener(animator -> {
                mainLayout.setBackgroundColor((int) animator.getAnimatedValue());
            });
            colorAnimation.start();
        }
    }

    private void updateThemeIcon() {
        themeToggleButton.setImageResource(isDarkMode ?
                R.drawable.ic_light_mode : R.drawable.ic_dark_mode);
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = activity.findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            // Handle navigation
            return true;
        });
    }
}
