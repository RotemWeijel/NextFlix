package com.app.nextflix.ui.main.player;


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
import androidx.lifecycle.ViewModelProvider;

import com.app.nextflix.R;
import com.app.nextflix.ui.common.PlayerViewModel;


public class PlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView seriesTitle;
    private SeekBar videoSeekbar;
    private ImageButton playPauseButton;
    private final float[] speeds = {0.5f, 0.75f, 1f, 1.25f, 1.5f, 1.75f, 2f};
    private int currentSpeedIndex = 2;
    private MediaPlayer mediaPlayer;
    private final Handler handler = new Handler();
    private PlayerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_fragment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        viewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        initializeViews();
        setupVideo();
        setupControls();
        setupViewModelObservers();
    }

    private void setupViewModelObservers() {
        viewModel.getCurrentPosition().observe(this, position -> {
            updateTimeDisplay(position);
            if (!videoSeekbar.isPressed()) {
                videoSeekbar.setProgress(position.intValue());
            }
        });

        viewModel.getIsPlaying().observe(this, isPlaying -> {
            playPauseButton.setImageResource(isPlaying ? R.drawable.resume : R.drawable.play);
            if (isPlaying && !videoView.isPlaying()) {
                videoView.start();
            } else if (!isPlaying && videoView.isPlaying()) {
                videoView.pause();
            }
        });

        viewModel.getIsMuted().observe(this, isMuted -> {
            ImageButton muteButton = findViewById(R.id.mute_toggle);
            muteButton.setImageResource(isMuted ? R.drawable.novol : R.drawable.mute);
        });
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
        int videoResId = getIntent().getIntExtra("video_res_id", 0);
        String movieId = getIntent().getStringExtra("movie_id");
        String title = getIntent().getStringExtra("name");


        if (videoResId != 0) {
            String properVideoPath = "android.resource://" + getPackageName() + "/" + videoResId;
            videoView.setVideoURI(Uri.parse(properVideoPath));
            videoView.setOnPreparedListener(this::onVideoPrepared);
            seriesTitle.setText(title);
            videoView.start();
            viewModel.setPlaying(true);
            if (movieId != null) {
                viewModel.markMovieAsRecommended(movieId);
            }
        } else {
            Toast.makeText(this, "No video available for this content", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(this::finish, 2000);
        }
    }

    private void onVideoPrepared(MediaPlayer mp) {
        mediaPlayer = mp;
        setupVideoDisplay(mp);
        viewModel.updateDuration(videoView.getDuration());
        setupSeekbar();
    }

    private void setupVideoDisplay(MediaPlayer mp) {
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
    }

    private void setupControls() {
        setupCloseButton();
        setupPlayPauseButton();
        setupSeekButtons();
        setupMuteButton();
        setupSpeedButton();
        setupSeekbar();
    }

    private void setupCloseButton() {
        ImageButton closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> {
            videoView.stopPlayback();
            finish();
        });
    }

    private void setupPlayPauseButton() {
        playPauseButton.setOnClickListener(v -> togglePlayPause());
    }

    private void setupSeekButtons() {

        findViewById(R.id.forward_button).setOnClickListener(v -> seekForward());
        findViewById(R.id.rewind_button).setOnClickListener(v -> seekBackward());
    }

    private void setupMuteButton() {
        ImageButton muteButton = findViewById(R.id.mute_toggle);
        muteButton.setOnClickListener(v -> toggleMute());
    }

    private void setupSpeedButton() {
        ImageButton playbackSpeedButton = findViewById(R.id.playback_speed);
        playbackSpeedButton.setOnClickListener(v -> updateSpeed());
    }

    private void setupSeekbar() {
        videoSeekbar.setMax(videoView.getDuration());
        videoSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                    viewModel.updatePosition(progress);
                    updateTimeDisplay(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    int currentPosition = videoView.getCurrentPosition();
                    viewModel.updatePosition(currentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void updateTimeDisplay(long position) {
        TextView startTimeView = findViewById(R.id.start_time);
        TextView endTimeView = findViewById(R.id.end_time);

        startTimeView.setText(viewModel.formatTime(position));
        long remainingTime = videoView.getDuration() - position;
        endTimeView.setText(viewModel.formatTime(remainingTime));
    }

    private void togglePlayPause() {
        boolean isPlaying = videoView.isPlaying();
        if (isPlaying) {
            videoView.pause();
        } else {
            videoView.start();
        }
        viewModel.setPlaying(!isPlaying);
    }

    private void seekForward() {
        int currentPosition = videoView.getCurrentPosition();
        int newPosition = Math.min(currentPosition + 10000, videoView.getDuration());
        videoView.seekTo(newPosition);
        viewModel.updatePosition(newPosition);
    }

    private void seekBackward() {
        int currentPosition = videoView.getCurrentPosition();
        int newPosition = Math.max(currentPosition - 10000, 0);
        videoView.seekTo(newPosition);
        viewModel.updatePosition(newPosition);
    }

    private void toggleMute() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        boolean isMuted = viewModel.getIsMuted().getValue() != null && viewModel.getIsMuted().getValue();

        if (!isMuted) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2, 0);
        }
        viewModel.setMuted(!isMuted);
    }

    @SuppressLint("ObsoleteSdkInt")
    private void updateSpeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mediaPlayer != null) {
            currentSpeedIndex = (currentSpeedIndex + 1) % speeds.length;
            float newSpeed = speeds[currentSpeedIndex];

            PlaybackParams params = mediaPlayer.getPlaybackParams();
            params.setSpeed(newSpeed);
            mediaPlayer.setPlaybackParams(params);
            viewModel.setSpeed(newSpeed);

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
            viewModel.setPlaying(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause();
            viewModel.setPlaying(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        videoView.stopPlayback();
    }
}