package com.sijanrijal.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.sijanrijal.popularmovies.model.MovieInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView backDropImage;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ExtendedFloatingActionButton mExtendedFloatingActionButton;

    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mMoviePlot;

    private TextView mReleaseDate;
    private TextView mGenre;
    private TextView mRating;

    private static final String TAG = "MovieDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        backDropImage = findViewById(R.id.backdrop_poster_iv);

        Toolbar mToolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        mExtendedFloatingActionButton = findViewById(R.id.rating_fab);
        AppBarLayout mAppBar = findViewById(R.id.app_bar);

        mMoviePoster = findViewById(R.id.movie_detail_poster);
        mMovieTitle = findViewById(R.id.movie_title);
        mMoviePlot = findViewById(R.id.movie_plot_tv);

        mRating = findViewById(R.id.rating_tv);
        mGenre = findViewById(R.id.genre_tv);
        mReleaseDate = findViewById(R.id.release_tv);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("MOVIE")) {
                setData((MovieInfo) intent.getParcelableExtra("MOVIE"));
            }
        }
    }

    /**
     * Populate the UI with the data
     **/
    private void setData(MovieInfo movie) {

        //set the toolbar's title, image and rating
        mCollapsingToolbarLayout.setTitle(movie.title);
        Glide.with(this).load(MovieAdapter.IMAGE_URL.replace("w500", "w780") + movie.poster_path).into(backDropImage);
        mExtendedFloatingActionButton.setText(String.valueOf(movie.vote_average));

        Glide.with(this).load(MovieAdapter.IMAGE_URL + movie.poster_path).into(mMoviePoster);
        mMovieTitle.setText(movie.title);
        mMoviePlot.setText(movie.overview);

        setDate(movie.release_date);
        setGenre(movie.genre_ids);
        mRating.setText(String.valueOf(movie.vote_average));

    }

    /**
     * Set the date in (Month, Year) format
     **/
    private void setDate(String date) {
        String[] dateArray = date.split("-");

        Calendar calendar = new GregorianCalendar(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]) - 1, Integer.parseInt(dateArray[2]));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String formattedDate = simpleDateFormat.format(calendar.getTime());
        mReleaseDate.setText(formattedDate);
    }


    /**
     * Get the genre of the movie
     **/
    private void setGenre(int[] movieGenreIds) {
        if (movieGenreIds != null && movieGenreIds.length > 0) {

            List<String> genres = MainActivity.getGenres(movieGenreIds);

            StringBuilder movieGenre = new StringBuilder();

            for (int i = 0; i < genres.size(); i++) {
                movieGenre.append(genres.get(i));
                if (i != genres.size() - 1) {
                    movieGenre.append(", ");
                }
            }

            mGenre.setText(movieGenre.toString());
        }
    }
}