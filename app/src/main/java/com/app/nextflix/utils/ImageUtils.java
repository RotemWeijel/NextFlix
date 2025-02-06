package com.app.nextflix.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.app.nextflix.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
        String transformedUrl = UrlUtils.transformUrl(imageUrl);
        Log.d(TAG, "Loading profile image from transformed URL: " + transformedUrl);

        Glide.with(context)
                .load(transformedUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .circleCrop()
                .into(imageView);
    }
}