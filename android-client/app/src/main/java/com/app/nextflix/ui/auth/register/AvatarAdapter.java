package com.app.nextflix.ui.auth.register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.nextflix.R;
import com.app.nextflix.utils.UrlUtils;

import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {
    private final List<String> avatarUrls;
    private final OnAvatarSelectedListener listener;
    private int selectedPosition = -1;
    private final Context context;

    public AvatarAdapter(List<String> avatarUrls, OnAvatarSelectedListener listener, Context context) {
        this.avatarUrls = avatarUrls;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_avatar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String avatarPath = avatarUrls.get(position);
        int resourceId = context.getResources().getIdentifier(avatarPath, null, context.getPackageName());
        holder.avatarImage.setImageResource(resourceId);
        holder.itemView.setSelected(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
            listener.onAvatarSelected(position, UrlUtils.transformAvatarUrl(avatarPath));
        });
    }

    @Override
    public int getItemCount() {
        return avatarUrls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;

        ViewHolder(View view) {
            super(view);
            avatarImage = view.findViewById(R.id.avatarImage);
        }
    }

    public interface OnAvatarSelectedListener {
        void onAvatarSelected(int position, String avatarPath);
    }
}