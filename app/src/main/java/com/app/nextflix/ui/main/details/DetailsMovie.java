package com.app.nextflix.ui.main.details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.nextflix.R;
import com.app.nextflix.models.Movie;
import com.app.nextflix.ui.admin.adapters.RecommendedMoviesAdapter;
import com.app.nextflix.ui.common.MovieViewModel;
import com.app.nextflix.ui.common.NavBarManager;
import com.app.nextflix.ui.main.player.PlayerActivity;


public class DetailsMovie extends AppCompatActivity {

    private VideoView videoView;

    private TextView movieTitle;
    private TextView releaseYear;
    private TextView ageRating;
    private TextView duration;
    private TextView movieDescription;
    private TextView castNames;
    private TextView directorName;
    private Button playButton;
    private ImageButton muteButton;
    private boolean isMuted = false;
    private ImageButton backButton;
    private String videoPath;
    private MovieViewModel viewModel;
    private RecommendedMoviesAdapter recommendedAdapter;
    private View loadingView;
    private MediaPlayer mediaPlayer;
    private String movieId;
    private int currentVideoResId;
 private NavBarManager navBarManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);

         navBarManager= new NavBarManager(this);
        navBarManager.setupNavBars();

        movieId = getIntent().getStringExtra("movie_id");
        if (movieId == null) {
            Toast.makeText(this, "Error: Movie ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.init(this);

        try {
            initializeViews();
            setupObservers();
            setupClickListeners();
            setupVideoPlayer();
            setupMuteButton();
            setupRecommendMovies();
            viewModel.loadMovie(movieId);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing app", Toast.LENGTH_SHORT).show();
        }
    }
    private void setupRecommendMovies() {
        RecyclerView recyclerView=findViewById(R.id.moreLikeThisRecyclerView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recommendedAdapter=new RecommendedMoviesAdapter(movie -> {
            Intent intent=new Intent(this,DetailsMovie.class);
            intent.putExtra("movie_id",movie.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(recommendedAdapter);
    }

    private void setupObservers(){
        viewModel.getMovieData().observe(this, this::updateMovieData);
        viewModel.getError().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show());
        viewModel.getLoading().observe(this, isLoading ->
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE));
        viewModel.getRecommendedMovies().observe(this, movies ->
                recommendedAdapter.setMovies(movies));

    }

    @SuppressLint("SetTextI18n")
    private void updateMovieData(Movie movie) {
        if (movie == null) {
            Log.e("DetailsMovie", "Received null movie object");
            Toast.makeText(this, "Error loading movie data", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            movieTitle.setText(movie.getName());
            releaseYear.setText(String.valueOf(movie.getReleaseYear()));
            ageRating.setText("+" + movie.getAgeAllow());
            duration.setText(movie.getDuration() + "m");
            movieDescription.setText(movie.getDescription());
            directorName.setText(movie.getDirector());
            String actors = movie.getActors().stream()
                    .map(Movie.Actor::getName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            castNames.setText(actors);
            String serverVideoUrl = movie.getVideoUrl();
            if (serverVideoUrl == null || serverVideoUrl.isEmpty()) {
                Log.e("DetailsMovie", "Invalid video URL");
                Toast.makeText(this, "Video not available", Toast.LENGTH_SHORT).show();
                return;
            }
            String videoFileName = serverVideoUrl
                    .replace("/", "")
                    .replace(".mp4", "")
                    .trim();

            currentVideoResId = getResources().getIdentifier(videoFileName, "raw", getPackageName());
            if (currentVideoResId != 0) {
                String properVideoPath = "android.resource://" + getPackageName() + "/" + currentVideoResId;
                videoPath = properVideoPath; // Save for player activity
                videoView.setVideoURI(Uri.parse(properVideoPath));
                videoView.start();
            } else {
                Log.e("DetailsMovie", "Video resource not found: " + videoFileName);
                Toast.makeText(this, "Video content not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("DetailsMovie", "Error updating movie data", e);
            Toast.makeText(this, "Error loading movie content", Toast.LENGTH_SHORT).show();
        }
}

    private void initializeViews() {
        videoView = findViewById(R.id.movieTrailer);
        movieTitle = findViewById(R.id.movieTitle);
        releaseYear = findViewById(R.id.releaseYear);
        ageRating = findViewById(R.id.ageRating);
        duration = findViewById(R.id.duration);
        movieDescription = findViewById(R.id.movieDescription);
        castNames = findViewById(R.id.castNames);
        directorName = findViewById(R.id.directorName);
        playButton = findViewById(R.id.playButton);
        backButton = findViewById(R.id.backButton);
        loadingView = findViewById(R.id.loadingView);
        muteButton=findViewById(R.id.muteButton);
    }


    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("video_res_id",currentVideoResId);
            intent.putExtra("video_path", videoPath);
            String name=movieTitle.getText().toString();
            intent.putExtra("name", name);
            startActivity(intent);
        });


    }
    private void setupVideoPlayer() {
        videoView = findViewById(R.id.movieTrailer);
        videoView.setOnPreparedListener(mp -> {
            mediaPlayer = mp;
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(0f, 0f);

            float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
            float screenRatio = videoView.getWidth() / (float) videoView.getHeight();
            float scaleX = videoRatio / screenRatio;

            if (scaleX >= 1f) {
                videoView.setScaleX(scaleX);
            } else {
                videoView.setScaleY(1f / scaleX);
            }
        });
    }

    private void setupMuteButton() {
        muteButton = findViewById(R.id.muteButton);
        muteButton.setOnClickListener(v -> {
            isMuted = !isMuted;
            if (mediaPlayer != null) {
                float volume = isMuted ? 0f : 1f;
                mediaPlayer.setVolume(volume, volume);
                muteButton.setImageResource(isMuted ?
                        R.drawable.ic_volume_off : R.drawable.ic_volume_on);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null && !videoView.isPlaying() && movieId != null) {
            viewModel.loadMovie(movieId);
        }
    }

    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}