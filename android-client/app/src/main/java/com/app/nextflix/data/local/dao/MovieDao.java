package com.app.nextflix.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.nextflix.data.local.entity.MovieEntity;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    List<MovieEntity> getAllMovies();

    @Query("SELECT * FROM movies WHERE id=:movieId" )
    MovieEntity getMovie(String movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieEntity movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MovieEntity> movies);

    @Delete
    void delete(MovieEntity movie);

    @Update
    void update(MovieEntity movie);

    @Query("DELETE FROM movies")
    void deleteAll();

    @Query("SELECT * FROM movies WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    List<MovieEntity> searchMovies(String query);
}