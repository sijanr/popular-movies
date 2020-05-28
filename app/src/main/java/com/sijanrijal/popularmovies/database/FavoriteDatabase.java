package com.sijanrijal.popularmovies.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Favorite.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {

    private static final String TAG = "FavoriteDatabase";
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorites";
    private static FavoriteDatabase favoriteDatabase;

    public static FavoriteDatabase getInstance(Context context) {
        if(favoriteDatabase == null) {
            synchronized (LOCK) {
                Log.d(TAG, "getInstance: Creating the database");
                favoriteDatabase = Room.databaseBuilder(
                        context.getApplicationContext(),
                        FavoriteDatabase.class,
                        DATABASE_NAME)
                        .build();
            }
        }
        return favoriteDatabase;
    }

    public abstract FavoritesDAO favoritesDAO();
}
