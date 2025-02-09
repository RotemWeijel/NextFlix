package com.app.nextflix.ui.admin.movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.app.nextflix.R;
import com.app.nextflix.data.local.AppDatabase;
import com.app.nextflix.data.local.dao.CategoryDao;
import com.app.nextflix.data.local.entity.CategoryEntity;
import com.app.nextflix.data.remote.api.MovieApi;
import com.app.nextflix.data.remote.api.RetrofitClient;
import com.app.nextflix.data.remote.api.WebServiceApi;
import com.app.nextflix.data.repositories.CategoryRepository;
import com.app.nextflix.models.Movie;
import com.app.nextflix.security.TokenManager;
import com.app.nextflix.ui.admin.categories.CategoryManagementViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Note! this page required adjustment for your IP
public class MovieFormDialog {
    private static final String TAG = "MovieFormDialog";
    private static final int MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB limit

    private Context context;
    private final AlertDialog.Builder builder;
    private View dialogView;
    private AlertDialog dialog;
    private String initialCategoryId;

    private Movie movieToEdit;
    private boolean isEditMode = false;


    // Form fields
    private EditText nameInput;
    private EditText descriptionInput;
    private EditText durationInput;
    private EditText releaseYearInput;
    private EditText ageAllowInput;
    private EditText directorInput;
    private EditText languageInput;
    private ImageView posterPreview;
    private TextView trailerUrlText;
    private TextView videoUrlText;
    private Button uploadPosterButton;
    private Button uploadTrailerButton;
    private Button uploadMovieButton;

    // Repository and managers
    private CategoryRepository categoryRepository;

    // File picker launchers
    private ActivityResultLauncher<Intent> posterPickerLauncher;
    private ActivityResultLauncher<Intent> trailerPickerLauncher;
    private ActivityResultLauncher<Intent> moviePickerLauncher;

    // Actors and Categories
    private LinearLayout actorsContainer;
    private Button addActorButton;
    private LinearLayout categoriesContainer;
    private List<EditText> actorEditTexts = new ArrayList<>();
    private List<CategoryEntity> availableCategories = new ArrayList<>();
    private Set<String> selectedCategoryIds = new HashSet<>();

    // URLs for uploaded files
    private String imageUrl;
    private String trailerUrl;
    private String videoUrl;

    public MovieFormDialog(Context context, String initialCategoryId,
                           ActivityResultLauncher<Intent> posterPickerLauncher,
                           ActivityResultLauncher<Intent> trailerPickerLauncher,
                           ActivityResultLauncher<Intent> moviePickerLauncher) {
        this.context = context;
        this.initialCategoryId = initialCategoryId;
        this.posterPickerLauncher = posterPickerLauncher;
        this.trailerPickerLauncher = trailerPickerLauncher;
        this.moviePickerLauncher = moviePickerLauncher;

        CategoryDao categoryDao = AppDatabase.getInstance(context).categoryDao();
        this.categoryRepository = new CategoryRepository(categoryDao, null);

        this.dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_movie_form, null);
        this.builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom))
                .setTitle(isEditMode ? R.string.edit_movie : R.string.add_new_movie)
                .setView(dialogView)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null);


        initializeViews();
        setupDefaultValues();
        setupListeners();
        loadAvailableCategories();
    }
    // Constructor for edit mode
    public MovieFormDialog(Context context, Movie movie,
                           ActivityResultLauncher<Intent> posterPickerLauncher,
                           ActivityResultLauncher<Intent> trailerPickerLauncher,
                           ActivityResultLauncher<Intent> moviePickerLauncher) {
        this.context = context;
        this.posterPickerLauncher = posterPickerLauncher;
        this.trailerPickerLauncher = trailerPickerLauncher;
        this.moviePickerLauncher = moviePickerLauncher;
        this.movieToEdit = movie;
        this.isEditMode = true;

        CategoryDao categoryDao = AppDatabase.getInstance(context).categoryDao();
        this.categoryRepository = new CategoryRepository(categoryDao, null);

        // Ensure we're using LayoutInflater with the correct context
        Context dialogContext = new ContextThemeWrapper(context, com.google.android.material.R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        this.dialogView = LayoutInflater.from(dialogContext).inflate(R.layout.dialog_movie_form, null);

        this.builder = new MaterialAlertDialogBuilder(dialogContext)
                .setTitle(R.string.edit_movie)
                .setView(dialogView)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null)
                .setNeutralButton(R.string.delete, null);

        initializeViews();
        setupDefaultValues();
        setupListeners();
        loadAvailableCategories();


        if (movieToEdit != null) {
            populateFieldsForEdit();
        }
    }
    private void initializeViews() {
        nameInput = dialogView.findViewById(R.id.movieNameInput);
        descriptionInput = dialogView.findViewById(R.id.movieDescriptionInput);
        durationInput = dialogView.findViewById(R.id.movieDurationInput);
        releaseYearInput = dialogView.findViewById(R.id.movieReleaseYearInput);
        ageAllowInput = dialogView.findViewById(R.id.movieAgeAllowInput);
        directorInput = dialogView.findViewById(R.id.movieDirectorInput);
        languageInput = dialogView.findViewById(R.id.movieLanguageInput);
        posterPreview = dialogView.findViewById(R.id.posterPreview);
        trailerUrlText = dialogView.findViewById(R.id.trailerUrlText);
        videoUrlText = dialogView.findViewById(R.id.videoUrlText);
        uploadPosterButton = dialogView.findViewById(R.id.uploadPosterButton);
        uploadTrailerButton = dialogView.findViewById(R.id.uploadTrailerButton);
        uploadMovieButton = dialogView.findViewById(R.id.uploadMovieButton);
        actorsContainer = dialogView.findViewById(R.id.actorsContainer);
        addActorButton = dialogView.findViewById(R.id.addActorButton);
        categoriesContainer = dialogView.findViewById(R.id.categoriesContainer);
    }
    private void populateFieldsForEdit() {
        nameInput.setText(movieToEdit.getName());
        descriptionInput.setText(movieToEdit.getDescription());
        durationInput.setText(String.valueOf(movieToEdit.getDuration()));
        releaseYearInput.setText(String.valueOf(movieToEdit.getReleaseYear()));
        ageAllowInput.setText(String.valueOf(movieToEdit.getAgeAllow()));
        directorInput.setText(movieToEdit.getDirector());
        languageInput.setText(movieToEdit.getLanguage());

        // Set existing URLs
        imageUrl = movieToEdit.getImageUrl();
        trailerUrl = movieToEdit.getTrailerUrl();
        videoUrl = movieToEdit.getVideoUrl();

        // Show existing URLs in UI
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String displayUrl = imageUrl.replace("localhost", "10.0.2.2");
            posterPreview.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(displayUrl)
                    .into(posterPreview);
        }

        if (trailerUrl != null && !trailerUrl.isEmpty()) {
            trailerUrlText.setText("Trailer: " + trailerUrl);
            trailerUrlText.setVisibility(View.VISIBLE);
        }

        if (videoUrl != null && !videoUrl.isEmpty()) {
            videoUrlText.setText("Video: " + videoUrl);
            videoUrlText.setVisibility(View.VISIBLE);
        }

        // Add existing actors
        actorsContainer.removeAllViews();
        actorEditTexts.clear();  // Clear the list first

        if (movieToEdit.getActors() != null && !movieToEdit.getActors().isEmpty()) {
            for (Movie.Actor actor : movieToEdit.getActors()) {
                EditText actorInput = new EditText(context);
                actorInput.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                actorInput.setText(actor.getName());
                actorInput.setTextColor(ContextCompat.getColor(context, android.R.color.black)); // Add this
                actorInput.setHintTextColor(ContextCompat.getColor(context, android.R.color.darker_gray)); // Add this
                actorsContainer.addView(actorInput);
                actorEditTexts.add(actorInput);
            }
        }

        // Pre-select categories
        selectedCategoryIds.addAll(movieToEdit.getCategories());
    }


    private void setupDefaultValues() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        releaseYearInput.setText(String.valueOf(currentYear));
        addActorInputField();
    }

    private void setupListeners() {
        uploadPosterButton.setOnClickListener(v -> handlePosterUpload());
        uploadTrailerButton.setOnClickListener(v -> handleTrailerUpload());
        uploadMovieButton.setOnClickListener(v -> handleMovieUpload());
        addActorButton.setOnClickListener(v -> addActorInputField());

    }

    private void handlePosterUpload() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        posterPickerLauncher.launch(intent);
    }

    private void handleTrailerUpload() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        trailerPickerLauncher.launch(intent);
    }

    private void handleMovieUpload() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        moviePickerLauncher.launch(intent);
    }


    public void handleFileSelection(Uri uri, String mimeType, FileUploadCallback callback) {
        Log.d(TAG, "Starting file selection handling for: " + mimeType);
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Processing file...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                Log.d(TAG, "Opening input stream");
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                if (inputStream == null) {
                    Log.e(TAG, "Failed to open input stream");
                    mainHandler.post(() -> {
                        Toast.makeText(context, R.string.error_opening_file, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    });
                    return;
                }

                Log.d(TAG, "Checking file size");
                long fileSize = inputStream.available();
                Log.d(TAG, "File size: " + fileSize);
                if (fileSize > MAX_FILE_SIZE) {
                    Log.e(TAG, "File too large: " + fileSize);
                    mainHandler.post(() -> {
                        Toast.makeText(context, R.string.error_file_too_large, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    });
                    inputStream.close();
                    return;
                }

                Log.d(TAG, "Creating temp file");
                File tempFile = createTempFile(uri);
                FileOutputStream fos = new FileOutputStream(tempFile);
                byte[] buffer = new byte[8192];
                int length;
                int totalRead = 0;
                while ((length = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                    totalRead += length;
                }
                Log.d(TAG, "Total bytes written to temp file: " + totalRead);
                fos.close();
                inputStream.close();

                Log.d(TAG, "File processing complete, calling callback");
                mainHandler.post(() -> {
                    progressDialog.dismiss();
                    callback.onFileReady(tempFile);
                });

            } catch (Exception e) {
                Log.e(TAG, "Error handling file selection", e);
                mainHandler.post(() -> {
                    Toast.makeText(context, R.string.error_processing_file, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
            } finally {
                executor.shutdown();
            }
        });
    }

    private File createTempFile(Uri uri) throws Exception {
        String fileName = getFileName(uri);
        File tempFile = File.createTempFile("upload_", fileName, context.getCacheDir());
        tempFile.deleteOnExit();
        return tempFile;
    }

    private String getFileName(Uri uri) {
        String result = "file";
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        return result;
    }

    public void uploadPosterFile(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        WebServiceApi webServiceApi = RetrofitClient.getWebServiceApi();
        webServiceApi.uploadImage(filePart).enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<UploadResponse> call, @NonNull Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    imageUrl = response.body().getUrl();
                    String displayUrl = imageUrl.replace("localhost", "10.0.2.2");

                    // Make sure to run UI updates on main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        // Show preview image
                        if (posterPreview != null) {
                            posterPreview.setVisibility(View.VISIBLE);
                            Glide.with(context)
                                    .load(displayUrl)
                                    .placeholder(R.drawable.placeholder_image)
                                    .error(R.drawable.error_image)
                                    .into(posterPreview);
                        }
                        Toast.makeText(context, R.string.upload_success, Toast.LENGTH_SHORT).show();
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, R.string.upload_failed, Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<UploadResponse> call, @NonNull Throwable t) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, R.string.network_error_upload, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void uploadTrailerFile(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("trailer", file.getName(), requestFile);

        WebServiceApi webServiceApi = RetrofitClient.getWebServiceApi();
        webServiceApi.uploadTrailer(filePart).enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<UploadResponse> call, @NonNull Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    trailerUrl = response.body().getUrl();
                    String displayUrl = trailerUrl.replace("localhost", "10.0.2.2");

                    // Show trailer URL
                    if (trailerUrlText != null) {
                        trailerUrlText.setText("Trailer: " + displayUrl);
                        trailerUrlText.setVisibility(View.VISIBLE);
                    }

                    Toast.makeText(context, R.string.upload_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.upload_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UploadResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, R.string.network_error_upload, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadVideoFile(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("video", file.getName(), requestFile);

        WebServiceApi webServiceApi = RetrofitClient.getWebServiceApi();
        webServiceApi.uploadVideo(filePart).enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<UploadResponse> call, @NonNull Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videoUrl = response.body().getUrl();
                    String displayUrl = videoUrl.replace("localhost", "10.0.2.2");

                    // Show video URL
                    if (videoUrlText != null) {
                        videoUrlText.setText("Video: " + displayUrl);
                        videoUrlText.setVisibility(View.VISIBLE);
                    }

                    Toast.makeText(context, R.string.upload_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.upload_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UploadResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, R.string.network_error_upload, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface FileUploadCallback {
        void onFileReady(File file);
    }

    private void loadAvailableCategories() {
        categoryRepository.getAllCategories().observeForever(categories -> {
            if (categories != null && !categories.isEmpty()) {
                availableCategories = categories;
                setupCategoriesSection();
            }
        });
    }

    private void setupCategoriesSection() {
        categoriesContainer.removeAllViews();

        for (CategoryEntity category : availableCategories) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(category.getName());
            checkBox.setTextColor(ContextCompat.getColor(context, android.R.color.black));

            // Check if this category is selected for the movie being edited
            if (isEditMode && movieToEdit != null &&
                    movieToEdit.getCategories() != null &&
                    movieToEdit.getCategories().contains(category.getId())) {
                checkBox.setChecked(true);
                selectedCategoryIds.add(category.getId());
            }

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedCategoryIds.add(category.getId());
                } else {
                    selectedCategoryIds.remove(category.getId());
                }
            });

            categoriesContainer.addView(checkBox);
        }
    }

    private void addActorInputField() {
        EditText actorEditText = new EditText(context);
        actorEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        actorEditText.setHint(R.string.actor_name_hint);

        actorsContainer.addView(actorEditText);
        actorEditTexts.add(actorEditText);
    }

    public void show() {
        dialog = builder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setTextColor(ContextCompat.getColor(context, R.color.netflix_red));
        negativeButton.setTextColor(ContextCompat.getColor(context, R.color.netflix_red));

        if (isEditMode) {
            Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            neutralButton.setTextColor(ContextCompat.getColor(context, R.color.netflix_red));
            neutralButton.setOnClickListener(v -> showDeleteConfirmation());
        }

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (validateForm()) {
                if (isEditMode) {
                    updateMovie();
                } else {
                    saveMovie();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;
        String errorMessage = context.getString(R.string.field_required);

        if (nameInput.getText().toString().trim().isEmpty()) {
            nameInput.setError(errorMessage);
            isValid = false;
        }

        if (descriptionInput.getText().toString().trim().isEmpty()) {
            descriptionInput.setError(errorMessage);
            isValid = false;
        }

        if (durationInput.getText().toString().trim().isEmpty()) {
            durationInput.setError(errorMessage);
            isValid = false;
        }

        if (directorInput.getText().toString().trim().isEmpty()) {
            directorInput.setError(errorMessage);
            isValid = false;
        }

        if (languageInput.getText().toString().trim().isEmpty()) {
            languageInput.setError(errorMessage);
            isValid = false;
        }

        if (selectedCategoryIds.isEmpty()) {
            Toast.makeText(context, R.string.select_category_warning, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Validate year
        try {
            int year = Integer.parseInt(releaseYearInput.getText().toString());
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year < 1888 || year > currentYear + 5) {
                releaseYearInput.setError(context.getString(R.string.invalid_year));
                isValid = false;
            }
        } catch (NumberFormatException e) {
            releaseYearInput.setError(context.getString(R.string.invalid_year));
            isValid = false;
        }

        // Validate age restriction
        try {
            int age = Integer.parseInt(ageAllowInput.getText().toString());
            if (age < 0) {
                ageAllowInput.setError(context.getString(R.string.invalid_age_restriction));
                isValid = false;
            }
        } catch (NumberFormatException e) {
            ageAllowInput.setError(context.getString(R.string.invalid_age_restriction));
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(context, R.string.please_fill_required_fields, Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }

    private void saveMovie() {
        List<Movie.Actor> actors = new ArrayList<>();
        for (EditText actorEditText : actorEditTexts) {
            String actorName = actorEditText.getText().toString().trim();
            if (!actorName.isEmpty()) {
                actors.add(new Movie.Actor(actorName));
            }
        }
        Log.d(TAG, "Creating movie with values:");
        Log.d(TAG, "Name: " + nameInput.getText().toString().trim());
        Log.d(TAG, "Description: " + descriptionInput.getText().toString().trim());
        Log.d(TAG, "Duration: " + durationInput.getText().toString().trim());
        Log.d(TAG, "Release Year: " + releaseYearInput.getText().toString().trim());
        Log.d(TAG, "Age Allow: " + ageAllowInput.getText().toString().trim());
        Log.d(TAG, "Director: " + directorInput.getText().toString().trim());
        Log.d(TAG, "Language: " + languageInput.getText().toString().trim());
        Log.d(TAG, "Image URL: " + imageUrl);
        Log.d(TAG, "Trailer URL: " + trailerUrl);
        Log.d(TAG, "Video URL: " + videoUrl);
        Log.d(TAG, "Actors: " + actors.size());
        Log.d(TAG, "Categories: " + selectedCategoryIds.size());

        Log.d(TAG, "About to create movie with these categories:");
        for (String categoryId : selectedCategoryIds) {
            Log.d(TAG, "Category ID format check - ID: " + categoryId +
                    ", Length: " + categoryId.length() +
                    ", Matches MongoDB format: " + categoryId.matches("^[0-9a-fA-F]{24}$"));
        }

        Movie movie = new Movie(
                null,
                nameInput.getText().toString().trim(),
                descriptionInput.getText().toString().trim(),
                Integer.parseInt(durationInput.getText().toString().trim()),
                Integer.parseInt(releaseYearInput.getText().toString().trim()),
                actors,
                new ArrayList<>(selectedCategoryIds),
                Integer.parseInt(ageAllowInput.getText().toString().trim()),
                directorInput.getText().toString().trim(),
                languageInput.getText().toString().trim(),
                imageUrl,
                trailerUrl,
                videoUrl
        );

        WebServiceApi webServiceApi = RetrofitClient.getWebServiceApi();
        webServiceApi.createMovie(movie).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Movie created successfully: " + response.body().getName());
                    Toast.makeText(context, R.string.movie_create_success, Toast.LENGTH_SHORT).show();


                    // Refresh categories to update movie counts
                    CategoryDao categoryDao = AppDatabase.getInstance(context).categoryDao();
                    CategoryRepository repository = new CategoryRepository(categoryDao, null);
                    repository.refreshCategories();

                    dialog.dismiss();
                } else {
                    try {
                        // Try to get detailed error message
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "No error body";
                        Log.e(TAG, "Error creating movie. Code: " + response.code() +
                                ", Error: " + errorBody);
                        String errorMsg = context.getString(R.string.movie_create_failed) +
                                " (" + response.code() + "): " + errorBody;
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to read error body", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                String errorMsg = context.getString(R.string.network_error) + t.getMessage();
                Log.e(TAG, errorMsg, t);
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void updateMovie() {
        Log.d(TAG, "Starting updateMovie...");

        Movie updatedMovie = new Movie(
                movieToEdit.getId(),
                nameInput.getText().toString().trim(),
                descriptionInput.getText().toString().trim(),
                Integer.parseInt(durationInput.getText().toString().trim()),
                Integer.parseInt(releaseYearInput.getText().toString().trim()),
                getActorsFromInputs(),
                new ArrayList<>(selectedCategoryIds),
                Integer.parseInt(ageAllowInput.getText().toString().trim()),
                directorInput.getText().toString().trim(),
                languageInput.getText().toString().trim(),
                imageUrl,
                trailerUrl,
                videoUrl
        );

        Log.d(TAG, "Updating movie with ID: " + movieToEdit.getId());
        Log.d(TAG, "API endpoint: api/movies/" + movieToEdit.getId());  // Add this log

        WebServiceApi webServiceApi = MovieApi.getInstance(context).getWebServiceApi();
        webServiceApi.updateMovie(movieToEdit.getId(), updatedMovie).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, R.string.movie_update_success, Toast.LENGTH_SHORT).show();
                    if (movieUpdatedListener != null) {
                        movieUpdatedListener.onMovieUpdated();
                    }
                    dialog.dismiss();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "No error body";
                        Log.e(TAG, "Error response body: " + errorBody);  // Add this log
                        Toast.makeText(context, R.string.movie_update_failed, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e(TAG, "Update network failure", t);
                Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom))
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.delete_movie_confirmation)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteMovie())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void deleteMovie() {
        WebServiceApi webServiceApi = MovieApi.getInstance(context).getWebServiceApi();
        webServiceApi.deleteMovie(movieToEdit.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, R.string.movie_delete_success, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    ((Activity) context).finish();
                } else {
                    Toast.makeText(context, R.string.movie_delete_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Movie.Actor> getActorsFromInputs() {
        List<Movie.Actor> actors = new ArrayList<>();
        for (EditText actorEditText : actorEditTexts) {
            String actorName = actorEditText.getText().toString().trim();
            if (!actorName.isEmpty()) {
                actors.add(new Movie.Actor(actorName));
            }
        }
        return actors;
    }

    public interface OnMovieUpdatedListener {
        void onMovieUpdated();
    }

    private OnMovieUpdatedListener movieUpdatedListener;

    public void setOnMovieUpdatedListener(OnMovieUpdatedListener listener) {
        this.movieUpdatedListener = listener;
    }
}