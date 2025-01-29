package com.app.nextflix.ui.common;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.app.nextflix.data.remote.api.MovieApi;
import com.app.nextflix.data.repositories.MovieRepository;
import com.app.nextflix.models.Movie;
import com.app.nextflix.models.MovieCategory;
import java.util.List;
import java.util.Random;

public class CategoryMoviesViewModel extends ViewModel {
    private MovieApi movieApi;
    private final MutableLiveData<List<MovieCategory>> categorizedMovies = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Movie> heroMovie = new MutableLiveData<>();

    private static class MoviesCallbackImpl implements MovieApi.MoviesCallback {
        private final MutableLiveData<List<MovieCategory>> categorizedMovies;
        private final MutableLiveData<String> error;
        private final MutableLiveData<Boolean> loading;
        private final CategoryMoviesViewModel viewModel;

        MoviesCallbackImpl(CategoryMoviesViewModel viewModel) {
            this.categorizedMovies = viewModel.categorizedMovies;
            this.error = viewModel.error;
            this.loading = viewModel.loading;
            this.viewModel = viewModel;
        }

        @Override
        public void onMoviesLoaded(List<MovieCategory> moviesByCategory, String errorMessage) {
            if (errorMessage != null) {
                error.postValue(errorMessage);
            } else if (moviesByCategory != null) {
                categorizedMovies.postValue(moviesByCategory);

                if (!moviesByCategory.isEmpty()
                        && moviesByCategory.get(0) != null
                        && moviesByCategory.get(0).getMovies() != null
                        && !moviesByCategory.get(0).getMovies().isEmpty()) {
                    viewModel.loadHeroMovie(moviesByCategory.get(0).getMovies());
                }
            }
            loading.postValue(false);
        }
    }

    public void init(Context context) {
        movieApi = new MovieApi(context);
        loadCategorizedMovies();
    }

    public void loadCategorizedMovies() {
        loading.setValue(true);

        if (movieApi == null) {
            error.setValue("MovieApi not initialized");
            loading.setValue(false);
            return;
        }

        movieApi.getAllMovies(new MoviesCallbackImpl(this));
    }

    private void loadHeroMovie(List<Movie> movies) {
        if (movies.isEmpty()) return;
        int randomIndex = new Random().nextInt(movies.size());
        Movie selectedMovie = movies.get(randomIndex);
        heroMovie.setValue(selectedMovie);
    }

    public MutableLiveData<List<MovieCategory>> getCategorizedMovies() {
        return categorizedMovies;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Movie> getHeroMovie() {
        return heroMovie;
    }
}