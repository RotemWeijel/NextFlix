package com.app.nextflix.ui.auth.landing;

import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.nextflix.R;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder> {

    private final Feature[] features = {
            new Feature(
                    "Instant Streaming",
                    "Enjoy instant streaming of your favorite content directly in your browser or app. No downloads needed.\n",
                    R.drawable.ic_streaming
            ),
            new Feature(
                    "Watch Everywhere",
                    "Access your favorite content seamlessly on both Web and Android devices. Switch between platforms effortlessly.",
                    R.drawable.ic_devices
            ),
            new Feature(
                    "Smart Recommendations",
                    "Get personalized content suggestions based on your watching habits and preferences.",
                    R.drawable.ic_recommendations
            )
    };

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feature, parent, false);

        // Add click effect
        view.setOnClickListener(v -> {
            // Just for the ripple effect, no action needed
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        });

        return new FeatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        Feature feature = features[position];
        Log.d("FeatureAdapter", "Binding position " + position + " with title: " + feature.getTitle());
        holder.titleView.setText(feature.getTitle());
        holder.descriptionView.setText(feature.getDescription());
        holder.imageView.setImageResource(feature.getImageResId());
    }

    @Override
    public int getItemCount() {
        return features.length;
    }


    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView titleView;
        final TextView descriptionView;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.featureImage);
            titleView = itemView.findViewById(R.id.featureTitle);
            descriptionView = itemView.findViewById(R.id.featureDescription);
        }
    }

    private static class Feature {
        private final String title;
        private final String description;
        private final int imageResId;

        Feature(String title, String description, int imageResId) {
            this.title = title;
            this.description = description;
            this.imageResId = imageResId;
        }

        String getTitle() { return title; }
        String getDescription() { return description; }
        int getImageResId() { return imageResId; }
    }
}