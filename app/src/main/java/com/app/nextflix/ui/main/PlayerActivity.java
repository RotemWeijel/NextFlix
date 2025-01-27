package com.app.nextflix.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.nextflix.R;

public class PlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView seriesTitle;
    private SeekBar videoSeekbar;
    private ImageButton playPauseButton;
    private final float[] speeds = {0.5f, 0.75f, 1f, 1.25f, 1.5f, 1.75f, 2f};
    private int currentSpeedIndex = 2;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean isMuted = false;

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_fragment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        initializeViews();
        setupVideo();
        setupControls();
    }

    private void initializeViews() {
        videoView = findViewById(R.id.video_view);
        seriesTitle = findViewById(R.id.series_title);
        videoSeekbar = findViewById(R.id.video_seekbar);
        playPauseButton = findViewById(R.id.play_pause_button);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setBackground(null);
    }

    private void setupVideo() {
        String videoPath = getIntent().getStringExtra("video_path");
        if (videoPath == null) {
            videoPath = "android.resource://" + getPackageName() + "/raw/video_480";
        }

        videoView.setVideoURI(Uri.parse(videoPath));
        String title = getIntent().getStringExtra("name");
        seriesTitle.setText(title != null ? title : "Tahrhen 2 episode 4");

        videoView.setOnPreparedListener(this::onVideoPrepared);
        videoView.start();
    }

    private void onVideoPrepared(MediaPlayer mp) {
        mediaPlayer = mp;
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        float heightRatio = (float) screenHeight / (float) videoHeight;
        float widthRatio = (float) screenWidth / (float) videoWidth;
        float scaleFactor = Math.max(heightRatio, widthRatio);

        ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        layoutParams.width = (int) (videoWidth * scaleFactor);
        layoutParams.height = (int) (videoHeight * scaleFactor);
        videoView.setLayoutParams(layoutParams);

        mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        updateSeekbar();
    }

    private void setupControls() {
        ImageButton closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> {
            videoView.stopPlayback();
            finish();
        });

        playPauseButton.setOnClickListener(v -> togglePlayPause());

        ImageButton forwardButton = findViewById(R.id.forward_button);
        forwardButton.setOnClickListener(v -> seekForward());

        ImageButton rewindButton = findViewById(R.id.rewind_button);
        rewindButton.setOnClickListener(v -> seekBackward());

        ImageButton muteButton = findViewById(R.id.mute_toggle);
        muteButton.setOnClickListener(v -> toggleMute());

        ImageButton playbackSpeedButton = findViewById(R.id.playback_speed);
        playbackSpeedButton.setOnClickListener(v -> updateSpeed());

        setupSeekbar();
    }

    private void setupSeekbar() {
        videoSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                    updateTimeDisplay(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateTimeRunnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.post(updateTimeRunnable);
            }
        });
    }

    private void updateSeekbar() {
        int duration = videoView.getDuration();
        videoSeekbar.setMax(duration);
        updateTimeDisplay(0);

        handler.post(updateTimeRunnable);
    }

    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (videoView.isPlaying()) {
                int currentPosition = videoView.getCurrentPosition();
                videoSeekbar.setProgress(currentPosition);
                updateTimeDisplay(currentPosition);
                handler.postDelayed(this, 1000);
            }
        }
    };

    @SuppressLint("DefaultLocale")
    private void updateTimeDisplay(int currentPosition) {
        TextView startTimeView = findViewById(R.id.start_time);
        TextView endTimeView = findViewById(R.id.end_time);

        int currentSeconds = currentPosition / 1000;
        int minutes = currentSeconds / 60;
        int seconds = currentSeconds % 60;
        startTimeView.setText(String.format("%d:%02d", minutes, seconds));

        int remainingTime = videoView.getDuration() - currentPosition;
        int remainingSeconds = remainingTime / 1000;
        int remainingMinutes = remainingSeconds / 60;
        int remainingSecondsDisplay = remainingSeconds % 60;
        endTimeView.setText(String.format("%d:%02d", remainingMinutes, remainingSecondsDisplay));
    }

    private void togglePlayPause() {
        if (videoView.isPlaying()) {
            videoView.pause();
            playPauseButton.setImageResource(R.drawable.play);
            handler.removeCallbacks(updateTimeRunnable);
        } else {
            videoView.start();
            playPauseButton.setImageResource(R.drawable.resume);
            handler.post(updateTimeRunnable);
        }
    }

    private void seekForward() {
        int currentPosition = videoView.getCurrentPosition();
        int newPosition = Math.min(currentPosition + 10000, videoView.getDuration());
        videoView.seekTo(newPosition);
        videoSeekbar.setProgress(newPosition);
        updateTimeDisplay(newPosition);
    }

    private void seekBackward() {
        int currentPosition = videoView.getCurrentPosition();
        int newPosition = Math.max(currentPosition - 10000, 0);
        videoView.seekTo(newPosition);
        videoSeekbar.setProgress(newPosition);
        updateTimeDisplay(newPosition);
    }

    private void toggleMute() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ImageButton muteButton = findViewById(R.id.mute_toggle);

        if (!isMuted) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            muteButton.setImageResource(R.drawable.novol);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2, 0);
            muteButton.setImageResource(R.drawable.mute);
        }
        isMuted = !isMuted;
    }

    private void updateSpeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mediaPlayer != null) {
            currentSpeedIndex = (currentSpeedIndex + 1) % speeds.length;
            float newSpeed = speeds[currentSpeedIndex];

            PlaybackParams params = mediaPlayer.getPlaybackParams();
            params.setSpeed(newSpeed);
            mediaPlayer.setPlaybackParams(params);

            Toast.makeText(this, "Speed: " + newSpeed + "x", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Playback speed control is not supported on this device",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying()) {
            videoView.start();
        }
        handler.post(updateTimeRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause();
        }
        handler.removeCallbacks(updateTimeRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimeRunnable);
        videoView.stopPlayback();
    }
}