package com.app.nextflix.ui.admin.categories;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.nextflix.R;
import com.app.nextflix.data.local.AppDatabase;
import com.app.nextflix.data.local.dao.CategoryDao;
import com.app.nextflix.data.local.entity.CategoryEntity;
import com.app.nextflix.data.repositories.CategoryRepository;
import com.app.nextflix.security.TokenManager;
import com.app.nextflix.ui.admin.adapters.CategoryAdapter;
import com.app.nextflix.ui.common.NavBarManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;

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
            Log.d(TAG, "Received categories in Activity: " +
                    (categories != null ? categories.size() : "null"));
            if (categories != null) {
                Log.d(TAG, "Categories list: " + categories.toString());
                adapter.setCategories(categories);
                Log.d(TAG, "Categories set in adapter");
            }
        });

        viewModel.getFeedback().observe(this, message -> {
            Log.d(TAG, "Received feedback: " + message);
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            Log.d(TAG, "Loading state changed: " + isLoading);
            loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    private void setupViewModel() {
        Log.d(TAG, "Setting up ViewModel");
        CategoryDao categoryDao = AppDatabase.getInstance(this).categoryDao();
        TokenManager tokenManager = new TokenManager();
        CategoryRepository repository = new CategoryRepository(categoryDao, tokenManager);
        CategoryViewModelFactory factory = new CategoryViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(CategoryManagementViewModel.class);
        Log.d(TAG, "ViewModel setup complete");
    }

    private void showCategoryDialog(CategoryEntity category) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_category_form, null);

        // Get current list of categories from adapter
        List<CategoryEntity> currentCategories = adapter.getCategories();

        CategoryFormHelper formHelper = new CategoryFormHelper(this, dialogView, currentCategories);

        if (category != null) {
            formHelper.populateForm(category);
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(category == null ? R.string.add_category : R.string.edit_category)
                .setView(dialogView)
                .setPositiveButton(R.string.save, null)  // Set null initially
                .setNegativeButton(R.string.cancel, null)
                .create();

        dialog.show();

        // Get the positive button and set its click listener after showing the dialog
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            // First check if form is valid
            if (!formHelper.validateForm()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;  // Return early if validation fails
            }

            try {
                CategoryEntity formData = formHelper.getCategoryData();
                if (category != null) {
                    formData.setId(category.getId());
                    viewModel.updateCategory(formData);
                } else {
                    viewModel.createCategory(formData);
                }
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(this, "Error processing form data", Toast.LENGTH_SHORT).show();
            }
        });
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
}