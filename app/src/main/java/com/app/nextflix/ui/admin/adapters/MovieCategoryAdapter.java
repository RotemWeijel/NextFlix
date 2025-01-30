package com.app.nextflix.ui.admin.adapters;

import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.nextflix.R;
import com.app.nextflix.models.MovieCategory;

import java.util.ArrayList;
import java.util.List;

public class MovieCategoryAdapter extends RecyclerView.Adapter<MovieCategoryAdapter.CategoryViewHolder> {
    private List<MovieCategory> categories = new ArrayList<>();
    private final RecommendedMoviesAdapter.OnMovieClickListener movieClickListener;

    public MovieCategoryAdapter(RecommendedMoviesAdapter.OnMovieClickListener listener) {
        this.movieClickListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<MovieCategory> categories) {
        Log.d("MovieCategoryAdapter", "Setting " + (categories != null ? categories.size() : 0) + " categories");
        if (categories != null) {
            for (MovieCategory category : categories) {
                Log.d("MovieCategoryAdapter", "Category: " + category.getName() +
                        " with " + (category.getMovies() != null ? category.getMovies().size() : 0) + " movies");
            }
        }
        this.categories = categories;
        notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryTitle;
        private final RecyclerView moviesRecyclerView;
        private final RecommendedMoviesAdapter moviesAdapter;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            moviesRecyclerView = itemView.findViewById(R.id.moviesRecyclerView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            moviesRecyclerView.setLayoutManager(layoutManager);

            // Use a smaller spacing value
            int spacing = (int) (itemView.getContext().getResources().getDimension(R.dimen.movie_item_spacing) * 0.7);
            moviesRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                           @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.left = spacing; // Small left margin for first item
                    }
                    outRect.right = spacing; // Small right margin for all items
                }
            });

            moviesAdapter = new RecommendedMoviesAdapter(movieClickListener);
            moviesRecyclerView.setAdapter(moviesAdapter);
        }

        void bind(MovieCategory category) {
            categoryTitle.setText(category.getName());
            moviesAdapter.setMovies(category.getMovies());
        }
    }
}