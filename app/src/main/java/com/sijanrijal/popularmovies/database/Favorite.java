package com.sijanrijal.popularmovies.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class Favorite {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "movie_title")
    private String movieTitle;

    @ColumnInfo(name = "movie_genre")
    private String movieGenres;

    @ColumnInfo(name = "movie_release")
    private String movieRelease;

    @ColumnInfo(name = "movie_rating")
    private double movieRating;

    @Ignore
    public Favorite(String movieTitle, String movieGenres, String movieRelease, double movieRating) {
        this.movieTitle = movieTitle;
        this.movieGenres = movieGenres;
        this.movieRelease = movieRelease;
        this.movieRating = movieRating;
    }

    public Favorite(int id, String movieTitle, String movieGenres, String movieRelease, double movieRating) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.movieGenres = movieGenres;
        this.movieRelease = movieRelease;
        this.movieRating = movieRating;
    }

    public int getId() {
        return id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieGenres() {
        return movieGenres;
    }

    public String getMovieRelease() {
        return movieRelease;
    }

    public double getMovieRating() {
        return movieRating;
    }
}
