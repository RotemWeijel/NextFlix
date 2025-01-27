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

import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.nextflix.R;

public class PlayerActivity extends AppCompatActivity {

    private VideoView videoview;
    private TextView textView;
    private SeekBar seekBar;
    private ImageButton playbutton;
    private final float[] speeds = {0.5f, 0.75f, 1f, 1.25f, 1.5f, 1.75f, 2f};
    private int currentSpeedIndex = 2;
    private MediaPlayer mediaPlayer;

    ImageButton playbackspeed;
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //start();
        videoview=findViewById(R.id.video_view);
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/video_480"));
        textView=findViewById(R.id.series_title);

        textView.setText("Tahrhen 2 episode 4");
        videoview.start();

        playbackspeed=findViewById(R.id.playback_speed);
        videoview.setOnPreparedListener(mp -> {
            mediaPlayer = mp;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PlaybackParams params = mediaPlayer.getPlaybackParams();
                params.setSpeed(speeds[currentSpeedIndex]);
                mediaPlayer.setPlaybackParams(params);
            }
            mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);


            updateseekbar();
        });
        playbackspeed.setOnClickListener(v->updateSpeed());

        playbutton=findViewById(R.id.play_pause_button);
        playbutton.setOnClickListener(v -> play());



        //update seek bar
        seekBar=findViewById(R.id.video_seekbar);



        //jump 10 below
        ImageButton forwardButton = findViewById(R.id.forward_button);
        forwardButton.setOnClickListener(v -> below10sec());


// Rewind 10 seconds
        ImageButton rewindButton = findViewById(R.id.rewind_button);
        rewindButton.setOnClickListener(v -> jump10sec());



        //press on back
        ImageButton backbutton=findViewById(R.id.close_button);
        backbutton.setOnClickListener(v->{
            videoview.stopPlayback();
            finish();
        });
        //press on mute
        ImageButton mutebutton=findViewById(R.id.mute_toggle);
        mutebutton.setOnClickListener(v->{
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                mutebutton.setImageResource(R.drawable.novol);
            } else {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2, 0);
                mutebutton.setImageResource(R.drawable.mute);
            }
        });




    }

    private void updateSpeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        } else {
            Toast.makeText(this, "Speed control is not supported on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateseekbar(){
        int duration = videoview.getDuration();
        seekBar.setMax(duration);

        int totalSeconds = duration / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        TextView end = findViewById(R.id.end_time);
        end.setText(String.format("%d:%02d", minutes, seconds));


        Handler handler = new Handler();
        Runnable updateSeekBar = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if (videoview.isPlaying()) {
                    int currentPosition = videoview.getCurrentPosition();
                    seekBar.setProgress(currentPosition);

                    // Update start time
                    int currentSeconds = currentPosition / 1000;
                    int minutes = currentSeconds / 60;
                    int seconds = currentSeconds % 60;
                    TextView startTimeView = findViewById(R.id.start_time);
                    startTimeView.setText(String.format("%d:%02d", minutes, seconds));
                    int remainingTime = videoview.getDuration() - currentPosition;
                    int remainingSeconds = remainingTime / 1000;
                    int remainingMinutes = remainingSeconds / 60;
                    int remainingSecondsDisplay = remainingSeconds % 60;
                    TextView endTimeView = findViewById(R.id.end_time);
                    endTimeView.setText(String.format("%d:%02d", remainingMinutes, remainingSecondsDisplay));
                }
                handler.postDelayed(this, 1000);
            }
        };

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoview.seekTo(progress);
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

    @SuppressLint("DefaultLocale")
    private void jump10sec (){
        int currentPosition = videoview.getCurrentPosition();
        int newPosition = Math.max(currentPosition - 10000, 0);
        videoview.seekTo(newPosition);
        seekBar.setProgress(newPosition);

        // Update start time
        int currentSeconds = newPosition / 1000;
        int minutes = currentSeconds / 60;
        int seconds = currentSeconds % 60;
        TextView startTimeView = findViewById(R.id.start_time);
        startTimeView.setText(String.format("%d:%02d", minutes, seconds));

        // Update end time (remaining time)
        TextView endTimeView = findViewById(R.id.end_time);
        int remainingTime = videoview.getDuration() - newPosition;
        int remainingSeconds = remainingTime / 1000;
        int remainingMinutes = remainingSeconds / 60;
        int remainingSecondsDisplay = remainingSeconds % 60;
        endTimeView.setText(String.format("%d:%02d", remainingMinutes, remainingSecondsDisplay));
    }

    @SuppressLint("DefaultLocale")
    private void below10sec(){
        int currentPosition = videoview.getCurrentPosition();
        int newPosition = Math.min(currentPosition + 10000, videoview.getDuration());
        videoview.seekTo(newPosition);
        seekBar.setProgress(newPosition);

        // Update start time
        int currentSeconds = newPosition / 1000;
        int minutes = currentSeconds / 60;
        int seconds = currentSeconds % 60;
        TextView startTimeView = findViewById(R.id.start_time);
        startTimeView.setText(String.format("%d:%02d", minutes, seconds));

        // Update end time (remaining time)
        TextView endTimeView = findViewById(R.id.end_time);
        int remainingTime = videoview.getDuration() - newPosition;
        int remainingSeconds = remainingTime / 1000;
        int remainingMinutes = remainingSeconds / 60;
        int remainingSecondsDisplay = remainingSeconds % 60;
        endTimeView.setText(String.format("%d:%02d", remainingMinutes, remainingSecondsDisplay));

    }
    private void start(){
        textView=findViewById(R.id.series_title);
        String name_video=getIntent().getStringExtra("name");
        textView.setText(name_video);
        playbutton=findViewById(R.id.play_pause_button);
        playbutton.setImageResource(R.drawable.resume);
        videoview=findViewById(R.id.video_view);
        String video_path=getIntent().getStringExtra("video_path");
        videoview.setVideoURI(Uri.parse(video_path));
        videoview.start();
    }
    private void play(){
        videoview=findViewById(R.id.video_view);
        playbutton=findViewById(R.id.play_pause_button);
        if(videoview.isPlaying()){
            videoview.pause();
            playbutton.setImageResource(R.drawable.play);
        }
        else {
            videoview.start();
            playbutton.setImageResource(R.drawable.resume);
        }
    }
}