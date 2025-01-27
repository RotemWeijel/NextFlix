package com.app.nextflix.ui.auth.landing;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LandingViewModel extends ViewModel {
    private final MutableLiveData<NavigationEvent> navigationEvent = new MutableLiveData<>();

    public LiveData<NavigationEvent> getNavigationEvent() {
        return navigationEvent;
    }

    public void onGetStartedClick() {
        navigationEvent.setValue(NavigationEvent.REGISTER);
    }

    public void onSignInClick() {
        navigationEvent.setValue(NavigationEvent.LOGIN);
    }

    public enum NavigationEvent {
        LOGIN,
        REGISTER
    }
}