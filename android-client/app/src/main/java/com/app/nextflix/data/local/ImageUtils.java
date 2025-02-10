package com.app.nextflix.data.local;

import android.content.Context;
import android.widget.ImageView;

import com.app.nextflix.R;

public class ImageUtils {
    public static void loadMovieImage(Context context, String imageUrl, ImageView imageView) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String fileName;
            if (imageUrl.startsWith("/")) {
                fileName = imageUrl.substring(1);
            } else {
                fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            }

            String resourceName = fileName;
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex > 0) {
                resourceName = fileName.substring(0, dotIndex);
            }

            int resourceId = context.getResources().getIdentifier(
                    resourceName,
                    "raw",
                    context.getPackageName()
            );

            if (resourceId != 0) {
                imageView.setImageResource(resourceId);
            } else {
                resourceId = context.getResources().getIdentifier(
                        resourceName,
                        "drawable",
                        context.getPackageName()
                );
                if (resourceId != 0) {
                    imageView.setImageResource(resourceId);
                } else {
                    imageView.setImageResource(R.drawable.error_movie);
                }
            }
        } else {
            imageView.setImageResource(R.drawable.error_movie);
        }
    }
}
