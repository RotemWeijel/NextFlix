package com.app.nextflix.ui.auth.register;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.app.nextflix.ui.main.browse.BrowseActivity;
import com.app.nextflix.R;
import com.app.nextflix.ui.main.browse.BrowseActivity;

public class RegistrationSuccessActivity extends AppCompatActivity {
    private static final long PROGRESS_DURATION = 4500; // 4.5 seconds
    private ProgressBar progressBar;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_success);

        progressBar = findViewById(R.id.progressBar);
        handler = new Handler();

        // Animate progress bar
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(
                progressBar,
                "progress",
                0, 100
        );
        progressAnimator.setDuration(PROGRESS_DURATION);
        progressAnimator.start();

        // Navigate to MainActivity after delay
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, BrowseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }, PROGRESS_DURATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}