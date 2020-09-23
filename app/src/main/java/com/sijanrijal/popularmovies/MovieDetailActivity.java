package com.sijanrijal.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sijanrijal.popularmovies.database.Favorite;
import com.sijanrijal.popularmovies.database.FavoriteDatabase;
import com.sijanrijal.popularmovies.databinding.ActivityMovieDetailBinding;
import com.sijanrijal.popularmovies.model.MovieInfo;
import com.sijanrijal.popularmovies.model.Reviews;
import com.sijanrijal.popularmovies.model.Trailer;
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

public class MovieDetailActivity extends AppCompatActivity implements TrailersListAdapter.OnListItemClick {

    private FavoriteDatabase favoriteDatabase;
    private ReviewListAdapter mAdapter;
    private TrailersListAdapter mTrailerListAdapter;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.included.trailersRecyclerView.setLayoutManager(linearLayoutManager);
        binding.included.trailersRecyclerView.setHasFixedSize(true);
        mTrailerListAdapter = new TrailersListAdapter(new ArrayList<Trailer.TrailerLink>(), this);
        binding.included.trailersRecyclerView.setAdapter(mTrailerListAdapter);

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
                        final String message;
                        String favorite = favoriteDatabase.favoritesDAO().getMovie(movieTitle);
                        if(favorite == null) {
                            favoriteDatabase.favoritesDAO().insertMovie(
                                    new Favorite(movieTitle, movieGenre, release, rating)
                            );
                            message="Added to your favorites";
                        } else {
                            message = "Already in your favorites";
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

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
    private void getReviewsAndTrailers(int movieId, final String resultType) {
        StringBuilder URL = new StringBuilder();
        URL.append(MainActivity.BASE_URL + "movie/").append(movieId);
        if(resultType.equals("REVIEWS")) {
            URL.append("/reviews");
        } else {
            URL.append("/videos");
        }
        URL.append("?api_key=").append(getString(R.string.API_KEY));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL.toString())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(resultType.equals("REVIEWS")) {
                            binding.included.errorReview.setVisibility(View.VISIBLE);
                        } else {
                            binding.included.errorTrailers.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    call.cancel();
                    if(resultType.equals("REVIEWS")) {
                        binding.included.errorReview.setVisibility(View.VISIBLE);
                    } else {
                        binding.included.errorTrailers.setVisibility(View.VISIBLE);
                    }
                }

                final String jsonString = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            parseReviews(jsonString, resultType);
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
    private void parseReviews(String jsonReviews, String resultType) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        if(resultType.equals("REVIEWS")) {
            JsonAdapter<Reviews> jsonAdapter = moshi.adapter(Reviews.class);
            Reviews reviews = jsonAdapter.fromJson(jsonReviews);
            if (reviews != null && reviews.results.size() > 0) {
                mAdapter.setReviews(reviews.results);
            } else {
                binding.included.errorReview.setVisibility(View.VISIBLE);
            }
        } else {
            JsonAdapter<Trailer> jsonAdapter = moshi.adapter(Trailer.class);
            Trailer trailer = jsonAdapter.fromJson(jsonReviews);
            if (trailer != null && trailer.results.size() > 0) {
                List<Trailer.TrailerLink> trailerList = new ArrayList<>();
                for (Trailer.TrailerLink trailerInfo : trailer.results) {
                    if(trailerInfo.type.equals("Trailer")) {
                        trailerList.add(trailerInfo);
                    }
                }
                mTrailerListAdapter.setTrailers(trailerList);
            } else {
                binding.included.errorTrailers.setVisibility(View.VISIBLE);
            }
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
        getReviewsAndTrailers(movie.id, "REVIEWS");
        getReviewsAndTrailers(movie.id, "TRAILERS");

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

    @Override
    public void onClick(String key) {
        Uri uri = new Uri.Builder().scheme("https")
                .authority("www.youtube.com")
                .appendPath("watch")
                .appendPath(key)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}