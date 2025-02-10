package com.app.nextflix.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.app.nextflix.data.local.converter.Converters;
import com.app.nextflix.data.local.dao.CategoryDao;
import com.app.nextflix.data.local.dao.MovieDao;
import com.app.nextflix.data.local.entity.CategoryEntity;
import com.app.nextflix.data.local.entity.MovieEntity;


@Database(
        entities={
                MovieEntity.class,
                CategoryEntity.class

        },version=3
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "netflix-server";
    private static AppDatabase instance;
    public abstract MovieDao movieDao();

    public abstract CategoryDao categoryDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

