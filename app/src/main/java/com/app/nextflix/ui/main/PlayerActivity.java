package com.app.nextflix.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

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

    @SuppressLint({"DefaultLocale", "SetTextI18x"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_fragment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        videoView = findViewById(com.app.nextflix.R.id.video_view);
        seriesTitle = findViewById(com.app.nextflix.R.id.series_title);
        videoSeekbar = findViewById(com.app.nextflix.R.id.video_seekbar);
        playPauseButton = findViewById(com.app.nextflix.R.id.play_pause_button);
        ImageButton playbackSpeed = findViewById(com.app.nextflix.R.id.playback_speed);

        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/video_480"));
        seriesTitle.setText("Tahrhen 2 episode 4");
        videoView.start();

        videoView.setOnPreparedListener(mp -> {
            mediaPlayer = mp;
            PlaybackParams params = mediaPlayer.getPlaybackParams();
            params.setSpeed(speeds[currentSpeedIndex]);
            mediaPlayer.setPlaybackParams(params);
            mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            updateSeekbar();
        });

        playbackSpeed.setOnClickListener(v -> updateSpeed());
        playPauseButton.setOnClickListener(v -> play());

        ImageButton forwardButton = findViewById(com.app.nextflix.R.id.forward_button);
        forwardButton.setOnClickListener(v -> below10sec());

        ImageButton rewindButton = findViewById(com.app.nextflix.R.id.rewind_button);
        rewindButton.setOnClickListener(v -> jump10sec());

        ImageButton closeButton = findViewById(com.app.nextflix.R.id.close_button);
        closeButton.setOnClickListener(v -> {
            videoView.stopPlayback();
            finish();
        });

        ImageButton muteButton = findViewById(com.app.nextflix.R.id.mute_toggle);
        muteButton.setOnClickListener(v -> {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                muteButton.setImageResource(R.drawable.novol);
            } else {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2, 0);
                muteButton.setImageResource(R.drawable.mute);
            }
        });
    }

    private void updateSpeed() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            currentSpeedIndex = (currentSpeedIndex + 1) % speeds.length;
            float newSpeed = speeds[currentSpeedIndex];

            PlaybackParams params = mediaPlayer.getPlaybackParams();
            params.setSpeed(newSpeed);
            mediaPlayer.setPlaybackParams(params);

            Toast.makeText(this, "Speed: " + newSpeed + "x", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Video is not playing.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSeekbar() {
        int duration = videoView.getDuration();
        videoSeekbar.setMax(duration);

        int totalSeconds = duration / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        TextView end = findViewById(com.app.nextflix.R.id.end_time);
        end.setText(String.format("%d:%02d", minutes, seconds));

        Handler handler = new Handler();
        Runnable updateSeekBar = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    int currentPosition = videoView.getCurrentPosition();
                    videoSeekbar.setProgress(currentPosition);

                    int currentSeconds = currentPosition / 1000;
                    int minutes = currentSeconds / 60;
                    int seconds = currentSeconds % 60;
                    TextView startTimeView = findViewById(com.app.nextflix.R.id.start_time);
                    startTimeView.setText(String.format("%d:%02d", minutes, seconds));
                    int remainingTime = videoView.getDuration() - currentPosition;
                    int remainingSeconds = remainingTime / 1000;
                    int remainingMinutes = remainingSeconds / 60;
                    int remainingSecondsDisplay = remainingSeconds % 60;
                    TextView endTimeView = findViewById(com.app.nextflix.R.id.end_time);
                    endTimeView.setText(String.format("%d:%02d", remainingMinutes, remainingSecondsDisplay));
                }
                handler.postDelayed(this, 1000);
            }
        };

        videoSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.post(updateSeekBar);
            }
        });

        handler.post(updateSeekBar);
    }

    private void jump10sec() {
        int currentPosition = videoView.getCurrentPosition();
        int newPosition = Math.max(currentPosition - 10000, 0);
        videoView.seekTo(newPosition);
        videoSeekbar.setProgress(newPosition);

        int currentSeconds = newPosition / 1000;
        int minutes = currentSeconds / 60;
        int seconds = currentSeconds % 60;
        TextView startTimeView = findViewById(com.app.nextflix.R.id.start_time);
        startTimeView.setText(String.format("%d:%02d", minutes, seconds));

        TextView endTimeView = findViewById(com.app.nextflix.R.id.end_time);
        int remainingTime = videoView.getDuration() - newPosition;
        int remainingSeconds = remainingTime / 1000;
        int remainingMinutes = remainingSeconds / 60;
        int remainingSecondsDisplay = remainingSeconds % 60;
        endTimeView.setText(String.format("%d:%02d", remainingMinutes, remainingSecondsDisplay));
    }

    private void below10sec() {
        int currentPosition = videoView.getCurrentPosition();
        int newPosition = Math.min(currentPosition + 10000, videoView.getDuration());
        videoView.seekTo(newPosition);
        videoSeekbar.setProgress(newPosition);

        int currentSeconds = newPosition / 1000;
        int minutes = currentSeconds / 60;
        int seconds = currentSeconds % 60;
        TextView startTimeView = findViewById(com.app.nextflix.R.id.start_time);
        startTimeView.setText(String.format("%d:%02d", minutes, seconds));

        TextView endTimeView = findViewById(com.app.nextflix.R.id.end_time);
        int remainingTime = videoView.getDuration() - newPosition;
        int remainingSeconds = remainingTime / 1000;
        int remainingMinutes = remainingSeconds / 60;
        int remainingSecondsDisplay = remainingSeconds % 60;
        endTimeView.setText(String.format("%d:%02d", remainingMinutes, remainingSecondsDisplay));
    }

    private void start() {
        seriesTitle = findViewById(com.app.nextflix.R.id.series_title);
        String name_video = getIntent().getStringExtra("name");
        seriesTitle.setText(name_video);
        playPauseButton = findViewById(com.app.nextflix.R.id.play_pause_button);
        playPauseButton.setImageResource(R.drawable.resume);
        videoView = findViewById(com.app.nextflix.R.id.video_view);
        String video_path = getIntent().getStringExtra("video_path");
        videoView.setVideoURI(Uri.parse(video_path));
        videoView.start();
    }

    private void play() {
        videoView = findViewById(com.app.nextflix.R.id.video_view);
        playPauseButton = findViewById(com.app.nextflix.R.id.play_pause_button);
        if(videoView.isPlaying()) {
            videoView.pause();
            playPauseButton.setImageResource(R.drawable.play);
        } else {
            videoView.start();
            playPauseButton.setImageResource(R.drawable.resume);
        }
    }
}