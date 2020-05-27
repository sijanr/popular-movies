package com.sijanrijal.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
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
import com.sijanrijal.popularmovies.databinding.ActivityMovieDetailBinding;
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

    private FavoriteDatabase favoriteDatabase;
    private ReviewListAdapter mAdapter;

    private ActivityMovieDetailBinding binding;

    private static final String TAG = "MovieDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        favoriteDatabase = FavoriteDatabase.getInstance(getApplicationContext());


        setSupportActionBar(binding.toolbarDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.included.errorReview.setVisibility(View.GONE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.included.reviewsRecyclerView.setLayoutManager(layoutManager);
        binding.included.reviewsRecyclerView.setHasFixedSize(true);
        mAdapter = new ReviewListAdapter(new ArrayList<Reviews.ReviewsContent>());
        binding.included.reviewsRecyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("MOVIE")) {
                setData((MovieInfo) intent.getParcelableExtra("MOVIE"));
            }
        }

        binding.ratingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String movieTitle = binding.included.movieTitle.getText().toString();
                final String movieGenre = binding.included.genreTv.getText().toString();
                final String release = binding.included.releaseTv.getText().toString();
                final double rating = Double.parseDouble(binding.included.ratingTv.getText().toString());
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
                        binding.included.errorReview.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    call.cancel();
                    binding.included.errorReview.setVisibility(View.VISIBLE);
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
            binding.included.errorReview.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Populate the UI with the data
     **/
    private void setData(MovieInfo movie) {

        //set the toolbar's title, image and rating
        binding.collapsingToolbar.setTitle(movie.title);
        Glide.with(this).load(MovieAdapter.IMAGE_URL.replace("w500", "w780") + movie.poster_path).into(binding.backdropPosterIv);

        Glide.with(this).load(MovieAdapter.IMAGE_URL + movie.poster_path).into(binding.included.movieDetailPoster);
        binding.included.movieTitle.setText(movie.title);
        binding.included.moviePlotTv.setText(movie.overview);

        setDate(movie.release_date);
        setGenre(movie.genre_ids);
        binding.included.ratingTv.setText(String.valueOf(movie.vote_average));
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
        binding.included.releaseTv.setText(formattedDate);
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

            binding.included.genreTv.setText(movieGenre.toString());
        }
    }
}