package com.app.nextflix.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayerViewModel extends ViewModel {
    private final MutableLiveData<Long> currentPosition = new MutableLiveData<>(0L);
    private final MutableLiveData<Long> duration = new MutableLiveData<>(0L);
    private final MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);
    private final MutableLiveData<Float> currentSpeed = new MutableLiveData<>(1.0f);
    private final MutableLiveData<Boolean> isMuted = new MutableLiveData<>(false);

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

    // Helper method to format time
    public String formatTime(long timeInMillis) {
        int seconds = (int) (timeInMillis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}
