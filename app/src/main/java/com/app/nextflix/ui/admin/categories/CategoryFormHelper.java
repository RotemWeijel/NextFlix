package com.app.nextflix.ui.admin.categories;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nextflix.R;
import com.app.nextflix.data.local.entity.CategoryEntity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CategoryFormHelper {
    private static final String TAG = "CategoryFormHelper";
    private final TextInputEditText nameInput;
    private final TextInputEditText descriptionInput;
    private final TextInputEditText sortOrderInput;
    private final CheckBox promotedCheckbox;
    private final CheckBox displayInMenuCheckbox;
    private final Spinner parentCategorySpinner;
    private List<CategoryEntity> allCategories;

    public CategoryFormHelper(Context context, View formView, List<CategoryEntity> categories) {
        nameInput = formView.findViewById(R.id.nameInput);
        descriptionInput = formView.findViewById(R.id.descriptionInput);
        sortOrderInput = formView.findViewById(R.id.sortOrderInput);
        promotedCheckbox = formView.findViewById(R.id.promotedCheckbox);
        displayInMenuCheckbox = formView.findViewById(R.id.displayInMenuCheckbox);
        parentCategorySpinner = formView.findViewById(R.id.parentCategorySpinner);
        this.allCategories = categories != null ? categories : new ArrayList<>();

        setupParentCategorySpinner(context);
    }

    private void setupParentCategorySpinner(Context context) {
        List<String> spinnerItems = new ArrayList<>();
        // Changed first item text to be the prompt
        spinnerItems.add("Select Parent Category");

        // Add all categories except the current one being edited
        for (CategoryEntity category : allCategories) {
            spinnerItems.add(category.getName());
            Log.d(TAG, "Adding category to spinner: " + category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                spinnerItems
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parentCategorySpinner.setAdapter(adapter);

        // Add this to show "Select Parent Category" as a prompt
        parentCategorySpinner.setPrompt("Select Parent Category");
    }

    public CategoryEntity getCategoryData() {
        CategoryEntity category = new CategoryEntity();

        // Log the data we're collecting
        Log.d(TAG, "Collecting form data:");

        String name = nameInput.getText().toString().trim();
        Log.d(TAG, "Name: " + name);
        category.setName(name);

        String description = descriptionInput.getText().toString().trim();
        Log.d(TAG, "Description: " + description);
        category.setDescription(description);

        String sortOrderStr = sortOrderInput.getText().toString().trim();
        int sortOrder = Integer.parseInt(sortOrderStr);
        Log.d(TAG, "Sort Order: " + sortOrder);
        category.setSortOrder(sortOrder);

        boolean promoted = promotedCheckbox.isChecked();
        Log.d(TAG, "Promoted: " + promoted);
        category.setPromoted(promoted);

        boolean displayInMenu = displayInMenuCheckbox.isChecked();
        Log.d(TAG, "Display in Menu: " + displayInMenu);
        category.setDisplayInMenu(displayInMenu);

        int selectedPosition = parentCategorySpinner.getSelectedItemPosition();
        if (selectedPosition > 0) {
            CategoryEntity parentCategory = allCategories.get(selectedPosition - 1);
            String parentId = parentCategory.getId();
            Log.d(TAG, "Parent Category Selected - Name: " + parentCategory.getName() +
                    ", ID: " + parentId);
            category.setParentCategoryId(parentId);
        } else {
            Log.d(TAG, "No parent category selected");
            category.setParentCategoryId(null);
        }

        return category;
    }

    public void populateForm(CategoryEntity category) {
        if (category == null) return;

        nameInput.setText(category.getName());
        descriptionInput.setText(category.getDescription());
        sortOrderInput.setText(String.valueOf(category.getSortOrder()));
        promotedCheckbox.setChecked(category.isPromoted());
        displayInMenuCheckbox.setChecked(category.isDisplayInMenu());

        // Set parent category in spinner if it exists
        if (category.getParentCategoryId() != null) {
            for (int i = 0; i < allCategories.size(); i++) {
                if (allCategories.get(i).getId().equals(category.getParentCategoryId())) {
                    parentCategorySpinner.setSelection(i + 1); // +1 because first item is "No Parent"
                    break;
                }
            }
        }
    }

    public boolean validateForm() {
        boolean isValid = true;
        String errorMessage = "This field is required";

        // Validate name
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            nameInput.setError(errorMessage);
            isValid = false;
        }

        // Validate description
        String description = descriptionInput.getText().toString().trim();
        if (description.isEmpty()) {
            descriptionInput.setError(errorMessage);
            isValid = false;
        }

        // Validate sort order
        String sortOrderStr = sortOrderInput.getText().toString().trim();
        if (sortOrderStr.isEmpty()) {
            sortOrderInput.setError(errorMessage);
            isValid = false;
        } else {
            try {
                Integer.parseInt(sortOrderStr);
            } catch (NumberFormatException e) {
                sortOrderInput.setError("Must be a valid number");
                isValid = false;
            }
        }

        // Make parent category optional, but validate if something is selected
        int selectedPosition = parentCategorySpinner.getSelectedItemPosition();
        if (selectedPosition == 0) {
            // User hasn't made a selection, that's fine - it means no parent
            Log.d(TAG, "No parent category selected - optional field");
        } else {
            Log.d(TAG, "Parent category selected at position: " + selectedPosition);
        }

        return isValid;
    }
}