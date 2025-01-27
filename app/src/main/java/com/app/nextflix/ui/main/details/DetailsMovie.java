package com.app.nextflix.ui.main.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import com.app.nextflix.R;
import com.app.nextflix.ui.main.PlayerActivity;

public class DetailsMovie extends AppCompatActivity {

    private VideoView movieTrailer;
    private TextView movieTitle;
    private TextView releaseYear;
    private TextView ageRating;
    private TextView duration;
    private TextView movieDescription;
    private TextView castNames;
    private TextView directorName;
    private Button playButton;
    private ImageButton backButton;
    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);

        try {
            initializeViews();
            setupDefaultMovieData();
            setupVideo();
            setupClickListeners();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing app", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupVideo() {
        try {
            videoPath = "android.resource://" + getPackageName() + "/raw/video_480";
            movieTrailer.setVideoURI(Uri.parse(videoPath));
            movieTrailer.start();
            movieTrailer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(this, "Error playing video", Toast.LENGTH_SHORT).show();
                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading video", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews() {
        movieTrailer = findViewById(R.id.movieTrailer);
        movieTitle = findViewById(R.id.movieTitle);
        releaseYear = findViewById(R.id.releaseYear);
        ageRating = findViewById(R.id.ageRating);
        duration = findViewById(R.id.duration);
        movieDescription = findViewById(R.id.movieDescription);
        castNames = findViewById(R.id.castNames);
        directorName = findViewById(R.id.directorName);
        playButton = findViewById(R.id.playButton);
        backButton = findViewById(R.id.backButton);
    }

    private void setupDefaultMovieData() {
        movieTitle.setText("Inception");
        releaseYear.setText("2010");
        ageRating.setText("+13");
        duration.setText("148m");
        movieDescription.setText("A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.");
        castNames.setText("Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page");
        directorName.setText("Christopher Nolan");
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("video_path", videoPath);
            intent.putExtra("name", "Inception");
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (movieTrailer != null && movieTrailer.isPlaying()) {
            movieTrailer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (movieTrailer != null) {
            movieTrailer.stopPlayback();
        }
    }
}