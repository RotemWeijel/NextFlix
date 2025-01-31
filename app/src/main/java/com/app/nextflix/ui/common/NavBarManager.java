package com.app.nextflix.ui.common;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.SharedPreferences;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;


import android.animation.ArgbEvaluator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;



import com.app.nextflix.R;
import com.google.android.material.appbar.AppBarLayout;
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
        initializeTheme();
    }

    private void initializeTheme() {
        View rootView = activity.findViewById(android.R.id.content);
        CoordinatorLayout mainLayout = rootView.findViewById(R.id.coordinator_layout);
        if (mainLayout != null) {
            updateColors(mainLayout);
        }
    }

    private void updateColors(CoordinatorLayout mainLayout) {
        // Update main background
        mainLayout.setBackgroundColor(isDarkMode ?
                Color.BLACK :
                Color.parseColor("#f5eae7"));

        // Update bottom navigation
        BottomNavigationView bottomNav = mainLayout.findViewById(R.id.bottomNav);
        if (bottomNav != null) {
            bottomNav.setBackgroundColor(isDarkMode ?
                    Color.BLACK :
                    Color.parseColor("#f5eae7"));

            int textColor = isDarkMode ? Color.WHITE : Color.BLACK;
            bottomNav.setItemTextColor(ColorStateList.valueOf(textColor));
            bottomNav.setItemIconTintList(ColorStateList.valueOf(textColor));
        }

        // Update top AppBarLayout
        AppBarLayout appBarLayout = mainLayout.findViewById(R.id.appBarLayout);
        if (appBarLayout != null) {
            appBarLayout.setBackgroundColor(isDarkMode ?
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
                int color = (int) animator.getAnimatedValue();
                mainLayout.setBackgroundColor(color);

                // Update navigation colors during animation
                BottomNavigationView bottomNav = mainLayout.findViewById(R.id.bottomNav);
                if (bottomNav != null) {
                    bottomNav.setBackgroundColor(color);
                    float progress = animator.getAnimatedFraction();
                    int textColor = isDarkMode ?
                            blendColors(Color.BLACK, Color.WHITE, progress) :
                            blendColors(Color.WHITE, Color.BLACK, progress);
                    bottomNav.setItemTextColor(ColorStateList.valueOf(textColor));
                    bottomNav.setItemIconTintList(ColorStateList.valueOf(textColor));
                }

                AppBarLayout appBarLayout = mainLayout.findViewById(R.id.appBarLayout);
                if (appBarLayout != null) {
                    appBarLayout.setBackgroundColor(color);
                }
            });
            colorAnimation.start();
        }
    }

    private int blendColors(int color1, int color2, float ratio) {
        float inverseRatio = 1f - ratio;
        float r = (Color.red(color1) * inverseRatio + Color.red(color2) * ratio);
        float g = (Color.green(color1) * inverseRatio + Color.green(color2) * ratio);
        float b = (Color.blue(color1) * inverseRatio + Color.blue(color2) * ratio);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    private void updateThemeIcon() {
        themeToggleButton.setImageResource(isDarkMode ?
                R.drawable.ic_light_mode : R.drawable.ic_dark_mode);
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = activity.findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            return true;
        });
    }
}
