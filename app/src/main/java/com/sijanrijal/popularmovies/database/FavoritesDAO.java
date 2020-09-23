package com.sijanrijal.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoritesDAO {

    @Query("SELECT * FROM favorite ORDER BY id")
    LiveData<List<Favorite>> loadFavoriteMovies();

    @Insert
    void insertMovie(Favorite movie);

    @Delete
    void deleteFavorite(Favorite movie);

    @Query("SELECT movie_title FROM favorite WHERE movie_title = :movieTitle")
    String getMovie(String movieTitle);
}
