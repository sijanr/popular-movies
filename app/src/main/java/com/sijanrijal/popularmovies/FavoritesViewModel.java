package com.sijanrijal.popularmovies;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sijanrijal.popularmovies.database.Favorite;
import com.sijanrijal.popularmovies.database.FavoriteDatabase;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private static final String TAG = "FavoritesViewModel";

    private LiveData<List<Favorite>> favoriteMovies;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        FavoriteDatabase favoriteDatabase = FavoriteDatabase.getInstance(this.getApplication());
        Log.d(TAG, "FavoritesViewModel: Actively retrieving the tasks from the database");
        favoriteMovies = favoriteDatabase.favoritesDAO().loadFavoriteMovies();
    }

    public LiveData<List<Favorite>> getFavoriteMovies() {
        return favoriteMovies;
    }
}
