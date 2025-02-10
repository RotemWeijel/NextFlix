package com.app.nextflix.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "categories",
        foreignKeys = @ForeignKey(
                entity = CategoryEntity.class,
                parentColumns = "id",
                childColumns = "parentCategoryId",
                onDelete = ForeignKey.SET_NULL
        ))
public class CategoryEntity {
    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "promoted")
    private boolean promoted;

    @ColumnInfo(name = "sortOrder")
    private int sortOrder;

    @ColumnInfo(name = "parentCategoryId")
    private String parentCategoryId;

    @ColumnInfo(name = "displayInMenu")
    private boolean displayInMenu;

    @ColumnInfo(name = "movieCount")
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
}