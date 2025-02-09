package com.app.nextflix.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Context;
import android.widget.Button;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.animation.ArgbEvaluator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.app.nextflix.data.repositories.UserRepository;
import com.app.nextflix.ui.main.search.SearchResultsActivity;
import com.app.nextflix.utils.ImageUtils;
import com.app.nextflix.utils.UrlUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.ViewGroup;

import com.app.nextflix.R;
import com.app.nextflix.models.User;
import com.app.nextflix.security.TokenManager;
import com.app.nextflix.ui.auth.login.LoginActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class NavBarManager {
    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_IS_DARK_MODE = "isDarkMode";
    private final Activity activity;
    private boolean isDarkMode;
    private final SharedPreferences settings;
    private ImageButton themeToggleButton;
    private ImageButton profileButton;
    private PopupWindow profileDropdown;
    private final TokenManager tokenManager;
    private User currentUser;
    private PopupWindow searchOverlay;
    private EditText searchEditText;

    public NavBarManager(Activity activity) {
        this.activity = activity;
        this.settings = activity.getSharedPreferences(PREFS_NAME, 0);
        this.isDarkMode = settings.getBoolean(KEY_IS_DARK_MODE, true);
        this.tokenManager = TokenManager.getInstance(activity);
        UserRepository userRepository = UserRepository.getInstance(activity);
        this.currentUser = userRepository.getCurrentUser();
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
        mainLayout.setBackgroundColor(isDarkMode ?
                Color.BLACK :
                Color.parseColor("#f5eae7"));

        // Update bottom navigation colors
        BottomNavigationView bottomNav = mainLayout.findViewById(R.id.bottomNav);
        if (bottomNav != null) {
            bottomNav.setBackgroundColor(isDarkMode ?
                    Color.BLACK :
                    Color.parseColor("#f5eae7"));

            int textColor = isDarkMode ? Color.WHITE : Color.BLACK;
            bottomNav.setItemTextColor(ColorStateList.valueOf(textColor));
            bottomNav.setItemIconTintList(ColorStateList.valueOf(textColor));
        }

        // Update AppBar colors
        AppBarLayout appBarLayout = mainLayout.findViewById(R.id.appBarLayout);
        if (appBarLayout != null) {
            appBarLayout.setBackgroundColor(isDarkMode ?
                    Color.BLACK :
                    Color.parseColor("#f5eae7"));
        }

        // Update search button color
        ImageButton searchButton = activity.findViewById(R.id.searchButton);
        if (searchButton != null) {
            searchButton.setColorFilter(isDarkMode ? Color.WHITE : Color.BLACK);
        }
    }

    public void setupNavBars() {
        setupThemeToggle();
        setupBottomNav();
        setupSearchButton();
        if (currentUser != null) {
            setupProfileButton();
        }
    }

    private void setupThemeToggle() {
        themeToggleButton = activity.findViewById(R.id.themeToggleButton);
        updateThemeIcon();

        themeToggleButton.setOnClickListener(v -> toggleTheme());
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

                ImageButton searchButton = activity.findViewById(R.id.searchButton);
                if (searchButton != null) {
                    float progress = animator.getAnimatedFraction();
                    int iconColor = isDarkMode ?
                            blendColors(Color.BLACK, Color.WHITE, progress) :
                            blendColors(Color.WHITE, Color.BLACK, progress);
                    searchButton.setColorFilter(iconColor);
                }

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
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener(item -> true);
        }
    }

    private void setupProfileButton() {
        profileButton = activity.findViewById(R.id.profileButton);
        Log.d("NavBarManager", "Setting up profile button");
        Log.d("NavBarManager", "Current user: " + (currentUser != null ? currentUser.getUsername() : "null"));

        if (profileButton != null) {
            Log.d("NavBarManager", "Profile button found");

            if (currentUser != null && currentUser.getPicture() != null) {
                Log.d("NavBarManager", "Loading profile picture: " + currentUser.getPicture());
                ImageUtils.loadProfileImage(activity, currentUser.getPicture(), profileButton);
            } else {
                profileButton.setImageResource(R.drawable.ic_person);
            }

            profileButton.setOnClickListener(v -> showProfileDropdown());
        }
    }

    private void showProfileDropdown() {
        View dropdownView = LayoutInflater.from(activity).inflate(R.layout.profile_dropdown_menu, null);

        // Set background color based on theme
        dropdownView.setBackgroundColor(isDarkMode ? Color.BLACK : Color.parseColor("#f5eae7"));

        profileDropdown = new PopupWindow(
                dropdownView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Find all views
        ImageView dropdownProfileImage = dropdownView.findViewById(R.id.dropdownProfileImage);
        TextView usernameText = dropdownView.findViewById(R.id.dropdownUsername);
        TextView displayNameText = dropdownView.findViewById(R.id.dropdownDisplayName);
        TextView roleText = dropdownView.findViewById(R.id.dropdownRole);
        Button logoutButton = dropdownView.findViewById(R.id.logoutButton);

        // Find label views
        TextView usernameLabel = dropdownView.findViewById(R.id.usernameLabel);
        TextView displayNameLabel = dropdownView.findViewById(R.id.displayNameLabel);
        TextView roleLabel = dropdownView.findViewById(R.id.roleLabel);

        // Apply theme colors
        int textColor = isDarkMode ? Color.WHITE : Color.BLACK;
        usernameText.setTextColor(textColor);
        displayNameText.setTextColor(textColor);
        roleText.setTextColor(textColor);
        usernameLabel.setTextColor(textColor);
        displayNameLabel.setTextColor(textColor);
        roleLabel.setTextColor(textColor);

        // Style the logout button based on theme
        if (isDarkMode) {
            //logoutButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            logoutButton.setTextColor(Color.WHITE);
        } else {
            //logoutButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            logoutButton.setTextColor(Color.BLACK);
        }

        if (currentUser != null) {
            usernameText.setText(currentUser.getUsername());
            displayNameText.setText(currentUser.getFull_name());
            roleText.setText(currentUser.isAdmin() ? "Administrator" : "User");

            if (currentUser.getPicture() != null) {
                ImageUtils.loadProfileImage(activity, currentUser.getPicture(), dropdownProfileImage);
            } else {
                dropdownProfileImage.setImageResource(R.drawable.ic_person);
            }
        }

        logoutButton.setOnClickListener(v -> {
            handleLogout();
            profileDropdown.dismiss();
        });

        profileDropdown.setElevation(8f);
        profileDropdown.showAsDropDown(profileButton, 0, 0, Gravity.END);
    }

    private void handleLogout() {
        tokenManager.clearToken();
        currentUser = null;

        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        setupProfileButton();
    }

    private void setupSearchButton() {
        ImageButton searchButton = activity.findViewById(R.id.searchButton);
        if (searchButton != null) {
            searchButton.setOnClickListener(v -> showSearchOverlay());
        }
    }

    private void showSearchOverlay() {
        View overlayView = LayoutInflater.from(activity).inflate(R.layout.search_overlay, null);
        overlayView.setBackgroundColor(isDarkMode ? Color.BLACK : Color.parseColor("#f5eae7"));
        searchOverlay = new PopupWindow(
                overlayView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Initialize views
        searchEditText = overlayView.findViewById(R.id.searchEditText);
        ImageButton closeButton = overlayView.findViewById(R.id.closeButton);
        ImageButton searchActionButton = overlayView.findViewById(R.id.searchButton);

        // Apply theme colors
        int textColor = isDarkMode ? Color.WHITE : Color.BLACK;
        int hintColor = isDarkMode ? Color.LTGRAY : Color.DKGRAY;

        searchEditText.setTextColor(textColor);
        searchEditText.setHintTextColor(hintColor);
        searchActionButton.setColorFilter(textColor);
        closeButton.setColorFilter(textColor);

        // Setup search action
        View.OnClickListener searchAction = v -> performSearch();
        searchActionButton.setOnClickListener(searchAction);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        // Setup close button
        closeButton.setOnClickListener(v -> searchOverlay.dismiss());

        // Show overlay with animation
        searchOverlay.setAnimationStyle(R.style.PopupAnimation);
        View anchor = activity.findViewById(R.id.mainToolbar);
        if (anchor != null) {
            searchOverlay.showAsDropDown(anchor);
            searchEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            Intent intent = new Intent(activity, SearchResultsActivity.class);
            intent.putExtra("search_query", query);
            activity.startActivity(intent);
            searchOverlay.dismiss();
        }
    }
}