package com.example.socialmedia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Post.class}, version = 2)  // Increment the version number
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PostDao postDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()  // Handle schema migrations
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

