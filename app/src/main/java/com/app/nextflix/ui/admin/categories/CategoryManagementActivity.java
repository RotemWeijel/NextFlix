package com.app.nextflix.ui.admin.categories;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.app.nextflix.R;
import com.app.nextflix.data.local.AppDatabase;
import com.app.nextflix.data.local.dao.CategoryDao;
import com.app.nextflix.data.local.entity.CategoryEntity;
import com.app.nextflix.data.repositories.CategoryRepository;
import com.app.nextflix.security.TokenManager;
import com.app.nextflix.ui.admin.adapters.CategoryAdapter;
import com.app.nextflix.ui.admin.movies.MovieFormDialog;
import com.app.nextflix.ui.common.NavBarManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class CategoryManagementActivity extends AppCompatActivity implements CategoryAdapter.CategoryClickListener {
    private static final String TAG = "CategoryManagement";
    private CategoryManagementViewModel viewModel;
    private CategoryAdapter adapter;
    private NavBarManager navBarManager;

    // Views
    private TextView feedbackText;
    private MaterialButton addCategoryButton;
    private RecyclerView categoriesRecyclerView;
    private View loadingView;
    private MovieFormDialog movieFormDialog;

    // File picker launchers
    private ActivityResultLauncher<Intent> posterPickerLauncher;
    private ActivityResultLauncher<Intent> trailerPickerLauncher;
    private ActivityResultLauncher<Intent> moviePickerLauncher;
    private void initializeFilePickers() {
        posterPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d(TAG, "Received result from poster picker");
                    if (!canHandleFilePicker()) return;

                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Log.d(TAG, "Result is OK and has data");
                        Uri selectedFile = result.getData().getData();
                        if (selectedFile != null) {
                            Log.d(TAG, "Selected file URI: " + selectedFile.toString());
                            movieFormDialog.handleFileSelection(selectedFile, "image/*", movieFormDialog::uploadPosterFile);
                        } else {
                            Log.e(TAG, "Selected file is null");
                            Toast.makeText(this, "Could not get selected file", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Result not OK or no data. ResultCode: " + result.getResultCode());
                    }
                }
        );

        trailerPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d(TAG, "Received result from poster picker");
                    if (!canHandleFilePicker()) return;

                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Log.d(TAG, "Result is OK and has data");
                        Uri selectedFile = result.getData().getData();
                        if (selectedFile != null) {
                            Log.d(TAG, "Selected file URI: " + selectedFile.toString());
                            movieFormDialog.handleFileSelection(selectedFile, "video/*", movieFormDialog::uploadTrailerFile);
                        } else {
                            Log.e(TAG, "Selected file is null");
                            Toast.makeText(this, "Could not get selected file", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Result not OK or no data. ResultCode: " + result.getResultCode());
                    }
                }
        );

        moviePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d(TAG, "Received result from poster picker");
                    if (!canHandleFilePicker()) return;

                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Log.d(TAG, "Result is OK and has data");
                        Uri selectedFile = result.getData().getData();
                        if (selectedFile != null) {
                            Log.d(TAG, "Selected file URI: " + selectedFile.toString());
                            movieFormDialog.handleFileSelection(selectedFile, "video/*", movieFormDialog::uploadVideoFile);
                        } else {
                            Log.e(TAG, "Selected file is null");
                            Toast.makeText(this, "Could not get selected file", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Result not OK or no data. ResultCode: " + result.getResultCode());
                    }
                }
        );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);

        // Set app bar color
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        appBarLayout.setBackgroundColor(Color.BLACK);

        // Initialize NavBarManager
        navBarManager = new NavBarManager(this);
        navBarManager.setupNavBars();

        initViews();
        setupViewModel();
        setupRecyclerView();
        setupListeners();
        setupObservers();
        initializeFilePickers();
    }

    private void initViews() {
        feedbackText = findViewById(R.id.feedbackText);
        addCategoryButton = findViewById(R.id.addCategoryButton);
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        loadingView = findViewById(R.id.loadingView);
    }

    private void setupRecyclerView() {
        adapter = new CategoryAdapter(this);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriesRecyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        addCategoryButton.setOnClickListener(v -> showCategoryDialog(null));
    }

    private void setupObservers() {
        viewModel.getCategories().observe(this, categories -> {
            if (categories != null) {
                adapter.setCategories(categories);
            }
        });

        viewModel.getFeedback().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    private void setupViewModel() {
        CategoryDao categoryDao = AppDatabase.getInstance(this).categoryDao();
        TokenManager tokenManager = TokenManager.getInstance(this);
        CategoryRepository repository = new CategoryRepository(categoryDao, tokenManager);
        CategoryViewModelFactory factory = new CategoryViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(CategoryManagementViewModel.class);
    }

    private void showCategoryDialog(CategoryEntity category) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_category_form, null);
        CategoryFormHelper formHelper = new CategoryFormHelper(this, dialogView, adapter.getCategories());

        if (category != null) {
            formHelper.populateForm(category);
            Button addMovieButton = dialogView.findViewById(R.id.addMovieButton);

            if (addMovieButton != null) {
                addMovieButton.setOnClickListener(v -> {
                    MovieFormDialog dialog = new MovieFormDialog(
                            this,
                            category.getId(),
                            posterPickerLauncher,
                            trailerPickerLauncher,
                            moviePickerLauncher
                    );
                    // Store the reference
                    this.movieFormDialog = dialog;
                    dialog.show("categotyManagement");
                });
            }
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(category == null ? R.string.add_category : R.string.edit_category)
                .setView(dialogView)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.netflix_red));
        negativeButton.setTextColor(ContextCompat.getColor(this, R.color.netflix_red));

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (!formHelper.validateForm()) {
                Toast.makeText(this, R.string.please_fill_required_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                CategoryEntity formData = formHelper.getCategoryData(category);
                if (category != null) {
                    formData.setId(category.getId());
                    viewModel.updateCategory(formData);
                } else {
                    viewModel.createCategory(formData);
                }
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(this, R.string.error_processing_form, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean canHandleFilePicker() {
        if (movieFormDialog == null) {
            Log.e(TAG, "MovieFormDialog is null, cannot handle file picking");
            Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onEditClick(CategoryEntity category) {
        showCategoryDialog(category);
    }

    @Override
    public void onDeleteClick(CategoryEntity category) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_category)
                .setMessage(R.string.delete_category_confirmation)
                .setPositiveButton(R.string.delete, (dialog, which) ->
                        viewModel.deleteCategory(category))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieFormDialog = null; // Clean up reference
    }
}