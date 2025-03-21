package com.app.nextflix.ui.main.details;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.nextflix.R;
import com.app.nextflix.models.Movie;
import com.app.nextflix.ui.admin.adapters.RecommendedMoviesAdapter;
import com.app.nextflix.ui.admin.movies.MovieFormDialog;
import com.app.nextflix.ui.common.MovieViewModel;
import com.app.nextflix.ui.common.NavBarManager;
import com.app.nextflix.ui.main.player.PlayerActivity;
import com.app.nextflix.utils.UrlUtils;


public class DetailsMovie extends AppCompatActivity {
    private static final String TAG = "DetailsMovie";

    private VideoView videoView;
    private TextView movieTitle;
    private TextView releaseYear;
    private TextView ageRating;
    private TextView duration;
    private TextView movieDescription;
    private TextView castNames;
    private TextView directorName;
    private Button playButton;
    private ImageButton editButton;
    private Movie currentMovie;
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

    private final ActivityResultLauncher<Intent> posterPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    Log.d(TAG, "Poster picked: " + uri);
                    MovieFormDialog dialog = MovieFormDialog.getCurrentDialog();
                    if (dialog != null) {
                        dialog.handleFileSelection(uri, "image/*", dialog::uploadPosterFile);
                    } else {
                        Log.e(TAG, "Dialog was null when trying to handle file selection");
                    }
                } else {
                    Log.d(TAG, "No image selected or selection cancelled");
                }
            }
    );
    private final ActivityResultLauncher<Intent> trailerPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    Log.d(TAG, "Trailer picked: " + uri);
                    MovieFormDialog dialog = MovieFormDialog.getCurrentDialog();
                    if (dialog != null) {
                        dialog.handleFileSelection(uri, "video/*", dialog::uploadTrailerFile);
                    } else {
                        Log.e(TAG, "Dialog was null when trying to handle trailer selection");
                    }
                } else {
                    Log.d(TAG, "No trailer selected or selection cancelled");
                }
            }
    );

    private final ActivityResultLauncher<Intent> moviePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    Log.d(TAG, "Video picked: " + uri);
                    MovieFormDialog dialog = MovieFormDialog.getCurrentDialog();
                    if (dialog != null) {
                        dialog.handleFileSelection(uri, "video/*", dialog::uploadVideoFile);
                    } else {
                        Log.e(TAG, "Dialog was null when trying to handle video selection");
                    }
                } else {
                    Log.d(TAG, "No video selected or selection cancelled");
                }
            }
    );

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
            currentMovie = movie;
            // Update text fields
            movieTitle.setText(movie.getName());
            releaseYear.setText(String.valueOf(movie.getReleaseYear()));
            ageRating.setText("+" + movie.getAgeAllow());
            duration.setText(movie.getDuration() + "m");
            movieDescription.setText(movie.getDescription());
            directorName.setText(movie.getDirector());

            // Handle actors list
            String actors = movie.getActors().stream()
                    .map(Movie.Actor::getName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            castNames.setText(actors);

            // Handle video URL
            String serverVideoUrl = movie.getVideoUrl();
            if (serverVideoUrl == null || serverVideoUrl.isEmpty()) {
                Log.e("DetailsMovie", "Invalid video URL");
                Toast.makeText(this, "Video not available", Toast.LENGTH_SHORT).show();
                return;
            }

            // Transform and use the server URL directly
            String transformedVideoUrl = UrlUtils.transformUrl(serverVideoUrl);
            Log.d("DetailsMovie", "Loading video from transformed URL: " + transformedVideoUrl);

            try {
                videoPath = transformedVideoUrl; // Save for player activity
                videoView.setVideoURI(Uri.parse(transformedVideoUrl));
                videoView.start();
            } catch (Exception e) {
                Log.e("DetailsMovie", "Error setting video URI: " + transformedVideoUrl, e);

                // Fallback to local resource if available
                String videoFileName = serverVideoUrl
                        .substring(serverVideoUrl.lastIndexOf('/') + 1)
                        .replace(".mp4", "")
                        .trim();

                currentVideoResId = getResources().getIdentifier(videoFileName, "raw", getPackageName());
                if (currentVideoResId != 0) {
                    String properVideoPath = "android.resource://" + getPackageName() + "/" + currentVideoResId;
                    videoPath = properVideoPath;
                    videoView.setVideoURI(Uri.parse(properVideoPath));
                    videoView.start();
                } else {
                    Log.e("DetailsMovie", "Video not found in resources: " + videoFileName);
                    Toast.makeText(this, "Video content not available", Toast.LENGTH_SHORT).show();
                }
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
        editButton = findViewById(R.id.editButton);

        if (editButton != null) {
            editButton.setVisibility(navBarManager.getCurrentUser().isAdmin() ?
                    View.VISIBLE : View.GONE);
        }
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
        editButton.setOnClickListener(v -> {
            if (currentMovie != null) {
                MovieFormDialog dialog = new MovieFormDialog(
                        this,
                        currentMovie,
                        posterPickerLauncher,
                        trailerPickerLauncher,
                        moviePickerLauncher
                );
                dialog.setOnMovieUpdatedListener(() -> {
                    viewModel.loadMovie(movieId);
                });
                // Add tag when showing dialog
                dialog.show("movieFormDialog");  // Make sure you're using a tag
            }
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