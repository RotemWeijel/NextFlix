package com.app.nextflix.models;

import com.app.nextflix.data.local.entity.CategoryEntity;
import com.google.gson.annotations.SerializedName;

public class CategoryResponse {
    @SerializedName("_id")
    private String id;

    private String name;
    private String description;
    private boolean promoted;
    private int sortOrder;
    private String parentCategoryId;
    private boolean displayInMenu;
    private int movieCount;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public boolean isDisplayInMenu() {
        return displayInMenu;
    }

    public void setDisplayInMenu(boolean displayInMenu) {
        this.displayInMenu = displayInMenu;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }

    public CategoryEntity toCategoryEntity() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setPromoted(this.promoted);
        entity.setSortOrder(this.sortOrder);
        entity.setParentCategoryId(this.parentCategoryId);
        entity.setDisplayInMenu(this.displayInMenu);
        entity.setMovieCount(this.movieCount);
        return entity;
    }
}