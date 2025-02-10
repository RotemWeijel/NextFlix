package com.app.nextflix.ui.main.search;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.app.nextflix.data.repositories.MovieRepository;

public class SearchViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final MovieRepository movieRepository;

    public SearchViewModelFactory(Application application, MovieRepository movieRepository) {
        this.application = application;
        this.movieRepository = movieRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(application, movieRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}