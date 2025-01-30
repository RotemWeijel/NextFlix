package com.app.nextflix.ui.common;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.nextflix.data.local.AppDatabase;
import com.app.nextflix.data.local.dao.MovieDao;
import com.app.nextflix.data.remote.api.MovieApi;
import com.app.nextflix.data.repositories.MovieRepository;

public class PlayerViewModel extends ViewModel {
    private final MutableLiveData<Long> currentPosition = new MutableLiveData<>(0L);
    private final MutableLiveData<Long> duration = new MutableLiveData<>(0L);
    private final MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);
    private final MutableLiveData<Float> currentSpeed = new MutableLiveData<>(1.0f);
    private final MutableLiveData<Boolean> isMuted = new MutableLiveData<>(false);
    private MovieRepository movieRepository;

    public void init(Context context) {
        MovieApi movieApi = new MovieApi(context);
        MovieDao movieDao = AppDatabase.getInstance(context).movieDao();
        movieRepository = new MovieRepository(movieApi, movieDao);
    }

    // Getters for LiveData
    public LiveData<Long> getCurrentPosition() {
        return currentPosition;
    }

    public LiveData<Long> getDuration() {
        return duration;
    }

    public LiveData<Boolean> getIsPlaying() {
        return isPlaying;
    }

    public LiveData<Float> getCurrentSpeed() {
        return currentSpeed;
    }

    public LiveData<Boolean> getIsMuted() {
        return isMuted;
    }

    // Setters to update values
    public void updatePosition(long position) {
        currentPosition.setValue(position);
    }

    public void updateDuration(long newDuration) {
        duration.setValue(newDuration);
    }

    public void setPlaying(boolean playing) {
        isPlaying.setValue(playing);
    }

    public void setSpeed(float speed) {
        currentSpeed.setValue(speed);
    }

    public void setMuted(boolean muted) {
        isMuted.setValue(muted);
    }

    public String formatTime(long timeInMillis) {
        int seconds = (int) (timeInMillis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public void markMovieAsRecommended(String movieId) {
        if(movieRepository!=null){
            movieRepository.markMovieAsRecommended(movieId, new MovieRepository.RecommendCallback() {
                @Override
                public void onSuccess() {
                    Log.d("PlayerViewModel", "Successfully marked movie as recommended");
                }

                @Override
                public void onError(String message) {
                    Log.e("PlayerViewModel", "Error marking movie as recommended: " + message);
                }
            });
        }
    }}