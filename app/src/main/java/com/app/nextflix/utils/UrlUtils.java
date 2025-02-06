package com.app.nextflix.utils;

import android.os.Build;
import android.util.Log;

public class UrlUtils {
    private static final String TAG = "UrlUtils";
    private static final String API_PORT = "4000";
    private static final String PROFILE_PORT = "3000";


    public static String transformUrl(String originalUrl) {
        if (originalUrl == null) return null;

        String serverIp = isEmulator() ? "10.0.2.2" : "192.168.7.3";
        String transformedUrl;

        // If it's a profile picture URL (contains /images/Register/), use port 3000
        if (originalUrl.contains("/images/Register/")) {
            transformedUrl = originalUrl
                    .replace("localhost:" + PROFILE_PORT, serverIp + ":" + PROFILE_PORT)
                    .replace("127.0.0.1:" + PROFILE_PORT, serverIp + ":" + PROFILE_PORT);
        }
        // For all other URLs (movies, videos), use port 4000
        else {
            transformedUrl = originalUrl
                    .replace("localhost:" + API_PORT, serverIp + ":" + API_PORT)
                    .replace("127.0.0.1:" + API_PORT, serverIp + ":" + API_PORT);
        }

        Log.d(TAG, "Original URL: " + originalUrl);
        Log.d(TAG, "Transformed URL: " + transformedUrl);

        return transformedUrl;
    }

    public static String transformAvatarUrl(String drawablePath) {
        String serverIp = isEmulator() ? "10.0.2.2" : "192.168.7.3";
        String avatarNumber = drawablePath.replace("drawable/avatar", "");
        return "http://" + serverIp + ":" + PROFILE_PORT + "/images/Register/avatar" + avatarNumber + ".png";
    }

    public static boolean isEmulator() {
        return Build.PRODUCT.contains("sdk") ||
                Build.PRODUCT.contains("gphone") ||
                Build.PRODUCT.contains("emulator") ||
                Build.FINGERPRINT.contains("generic") ||
                Build.MANUFACTURER.contains("Genymotion");
    }
}