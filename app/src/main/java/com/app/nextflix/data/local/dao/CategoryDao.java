package com.app.nextflix.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.app.nextflix.data.local.entity.CategoryEntity;
import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY sortOrder ASC")
    LiveData<List<CategoryEntity>> getAllCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryEntity category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoryEntity> categories);

    @Update
    void update(CategoryEntity category);

    @Delete
    void delete(CategoryEntity category);

    @Query("SELECT * FROM categories WHERE id = :id")
    LiveData<CategoryEntity> getCategoryById(String id);

    @Query("SELECT COUNT(*) FROM categories")
    int getCategoryCount();
}