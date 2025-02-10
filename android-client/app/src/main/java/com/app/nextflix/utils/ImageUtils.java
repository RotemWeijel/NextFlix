package com.app.nextflix.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.app.nextflix.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ImageUtils {
    private static final String TAG = "ImageUtils";

    public static void loadMovieImage(Context context, String imageUrl, ImageView imageView) {
        String transformedUrl = UrlUtils.transformUrl(imageUrl);
        Log.d(TAG, "Loading movie image from transformed URL: " + transformedUrl);

        Glide.with(context)
                .load(transformedUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }

    public static void loadProfileImage(Context context, String imageUrl, ImageView imageView) {
        Log.d(TAG, "Loading profile image from URL: " + imageUrl);

        // Check if it's an avatar URL
        if (imageUrl != null && imageUrl.contains("/images/Register/avatar")) {
            // Extract the avatar number
            String number = imageUrl.replaceAll(".*avatar([0-9]+)\\.png.*", "$1");
            // Create drawable resource name
            String resourceName = "avatar" + number;

            // Get the resource ID
            int resourceId = context.getResources().getIdentifier(
                    resourceName, "drawable", context.getPackageName());

            if (resourceId != 0) {
                // Load the local drawable
                Glide.with(context)
                        .load(resourceId)
                        .circleCrop()
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(imageView);
                return;
            }
        }

        // For all other cases (non-avatar images), use the original URL loading logic
        String transformedUrl = UrlUtils.transformUrl(imageUrl);
        Log.d(TAG, "Loading profile image from transformed URL: " + transformedUrl);

        Glide.with(context)
                .load(transformedUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .circleCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "Failed to load profile image: " + transformedUrl, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "Successfully loaded profile image: " + transformedUrl);
                        return false;
                    }
                })
                .into(imageView);
    }
}