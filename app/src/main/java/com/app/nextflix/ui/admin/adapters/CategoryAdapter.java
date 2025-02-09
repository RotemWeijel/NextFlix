package com.app.nextflix.ui.admin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.nextflix.R;
import com.app.nextflix.data.local.entity.CategoryEntity;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<CategoryEntity> categories = new ArrayList<>();
    private final CategoryClickListener listener;

    public interface CategoryClickListener {
        void onEditClick(CategoryEntity category);
        void onDeleteClick(CategoryEntity category);
    }

    public CategoryAdapter(CategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryEntity category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
    public List<CategoryEntity> getCategories() {
        return new ArrayList<>(categories);
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryName;
        private final TextView movieCount;
        private final TextView categoryInfo;
        private final Button editButton;
        private final Button deleteButton;

        CategoryViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            movieCount = itemView.findViewById(R.id.movieCount);
            categoryInfo = itemView.findViewById(R.id.categoryInfo);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        void bind(CategoryEntity category) {
            categoryName.setText(category.getName());
            movieCount.setText(itemView.getContext().getString(
                    R.string.movies_count, category.getMovieCount()));

            String info = String.format("%s | Sort Order: %d",
                    category.isPromoted() ? "Promoted" : "Not Promoted",
                    category.getSortOrder());
            categoryInfo.setText(info);

            editButton.setOnClickListener(v -> listener.onEditClick(category));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(category));
        }
    }
}