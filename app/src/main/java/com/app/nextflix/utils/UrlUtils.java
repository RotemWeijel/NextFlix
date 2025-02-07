package com.app.nextflix.utils;

import android.os.Build;
import android.util.Log;

public class UrlUtils {
    private static final String TAG = "UrlUtils";
    private static final String API_PORT = "4000";
    private static final String IMAGES_PORT = "4000";

    public static String transformUrl(String originalUrl) {
        if (originalUrl == null) return null;

        String serverIp = isEmulator() ? "10.0.2.2" : "192.168.7.3";
        Log.d(TAG, "Original URL: " + originalUrl);

        // Add the localhost pattern with port number
        if (originalUrl.startsWith("http://localhost:4000") ||
                originalUrl.startsWith("http://127.0.0.1:4000")) {
            String transformedUrl = originalUrl
                    .replace("localhost:4000", serverIp + ":4000")
                    .replace("127.0.0.1:4000", serverIp + ":4000");
            Log.d(TAG, "Transformed localhost URL: " + transformedUrl);
            return transformedUrl;
        }

        // Handle drawable references directly
        if (originalUrl.startsWith("drawable/avatar")) {
            String avatarPath = transformAvatarUrl(originalUrl);
            String fullUrl = String.format("http://%s:%s%s", serverIp, IMAGES_PORT, avatarPath);
            Log.d(TAG, "Transformed avatar URL: " + fullUrl);
            return fullUrl;
        }

        // Handle relative paths for avatars
        if (originalUrl.startsWith("/images/Register/")) {
            String fullUrl = String.format("http://%s:%s%s", serverIp, IMAGES_PORT, originalUrl);
            Log.d(TAG, "Transformed register URL: " + fullUrl);
            return fullUrl;
        }

        // Handle relative paths for movies/posters
        if (originalUrl.startsWith("/uploads/")) {
            String fullUrl = String.format("http://%s:%s%s", serverIp, API_PORT, originalUrl);
            Log.d(TAG, "Transformed movie URL: " + fullUrl);
            return fullUrl;
        }

        // Handle absolute URLs
        if (originalUrl.startsWith("http://localhost") || originalUrl.startsWith("http://127.0.0.1")) {
            String transformedUrl = originalUrl
                    .replace("localhost", serverIp)
                    .replace("127.0.0.1", serverIp);
            Log.d(TAG, "Transformed localhost URL: " + transformedUrl);
            return transformedUrl;
        }

        Log.d(TAG, "URL unchanged: " + originalUrl);
        return originalUrl;
    }

    public static String transformAvatarUrl(String drawablePath) {
        if (drawablePath == null) return null;

        // Extract just the number for the avatar
        String avatarNumber;
        if (drawablePath.startsWith("drawable/avatar")) {
            avatarNumber = drawablePath.replace("drawable/avatar", "");
        } else {
            avatarNumber = drawablePath;
        }

        // Clean the number and ensure it's valid
        avatarNumber = avatarNumber.replaceAll("[^0-9]", "");
        if (avatarNumber.length() > 1) {
            avatarNumber = avatarNumber.substring(avatarNumber.length() - 1);  // Take only the last digit
        }
        if (avatarNumber.isEmpty()) {
            avatarNumber = "1";
        }

        // For registration - match the web version format exactly
        return String.format("http://localhost:3000/images/Register/avatar%s.png", avatarNumber);
    }

    public static boolean isEmulator() {
        return Build.PRODUCT.contains("sdk") ||
                Build.PRODUCT.contains("gphone") ||
                Build.PRODUCT.contains("emulator") ||
                Build.FINGERPRINT.contains("generic") ||
                Build.MANUFACTURER.contains("Genymotion");
    }
}