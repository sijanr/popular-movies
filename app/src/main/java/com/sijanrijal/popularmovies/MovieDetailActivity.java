package com.sijanrijal.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sijanrijal.popularmovies.database.Favorite;
import com.sijanrijal.popularmovies.database.FavoriteDatabase;
import com.sijanrijal.popularmovies.model.MovieInfo;
import com.sijanrijal.popularmovies.model.Reviews;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView backDropImage;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton floatingActionButton;

    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mMoviePlot;

    private TextView mReleaseDate;
    private TextView mGenre;
    private TextView mRating;

    private FavoriteDatabase favoriteDatabase;
    private ReviewListAdapter mAdapter;
    private TextView errorReviewMessage;

    private static final String TAG = "MovieDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        favoriteDatabase = FavoriteDatabase.getInstance(getApplicationContext());

        backDropImage = findViewById(R.id.backdrop_poster_iv);

        Toolbar mToolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        errorReviewMessage = findViewById(R.id.error_review);
        errorReviewMessage.setVisibility(View.GONE);

        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        floatingActionButton = findViewById(R.id.rating_fab);

        mMoviePoster = findViewById(R.id.movie_detail_poster);
        mMovieTitle = findViewById(R.id.movie_title);
        mMoviePlot = findViewById(R.id.movie_plot_tv);

        mRating = findViewById(R.id.rating_tv);
        mGenre = findViewById(R.id.genre_tv);
        mReleaseDate = findViewById(R.id.release_tv);

        RecyclerView recyclerView = findViewById(R.id.reviews_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new ReviewListAdapter(new ArrayList<Reviews.ReviewsContent>());
        recyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("MOVIE")) {
                setData((MovieInfo) intent.getParcelableExtra("MOVIE"));
            }
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String movieTitle = mMovieTitle.getText().toString();
                final String movieGenre = mGenre.getText().toString();
                final String release = mReleaseDate.getText().toString();
                final double rating = Double.parseDouble(mRating.getText().toString());
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        favoriteDatabase.favoritesDAO().insertMovie(
                                new Favorite(movieTitle, movieGenre, release, rating)
                        );
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Added to your favorites", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }
        });


    }

    /**
     * Get the reviews of the selected movie by making a network call to the API
     **/
    private void getReviews(int movieId) {
        String URL = MainActivity.BASE_URL + "movie/" + movieId + "/reviews?api_key="
                + getString(R.string.API_KEY);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        errorReviewMessage.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    call.cancel();
                    errorReviewMessage.setVisibility(View.VISIBLE);
                }

                final String jsonString = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            parseReviews(jsonString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }

    /**
     * Parse the reviews json and populate the reviews section of the UI
     **/
    private void parseReviews(String jsonReviews) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Reviews> jsonAdapter = moshi.adapter(Reviews.class);
        Reviews reviews = jsonAdapter.fromJson(jsonReviews);
        if (reviews != null && reviews.results.size() > 0) {
            mAdapter.setReviews(reviews.results);
        } else {
            errorReviewMessage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Populate the UI with the data
     **/
    private void setData(MovieInfo movie) {

        //set the toolbar's title, image and rating
        mCollapsingToolbarLayout.setTitle(movie.title);
        Glide.with(this).load(MovieAdapter.IMAGE_URL.replace("w500", "w780") + movie.poster_path).into(backDropImage);

        Glide.with(this).load(MovieAdapter.IMAGE_URL + movie.poster_path).into(mMoviePoster);
        mMovieTitle.setText(movie.title);
        mMoviePlot.setText(movie.overview);

        setDate(movie.release_date);
        setGenre(movie.genre_ids);
        mRating.setText(String.valueOf(movie.vote_average));
        getReviews(movie.id);

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