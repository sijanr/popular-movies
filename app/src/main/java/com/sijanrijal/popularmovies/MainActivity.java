package com.sijanrijal.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sijanrijal.popularmovies.database.Favorite;
import com.sijanrijal.popularmovies.database.FavoriteDatabase;
import com.sijanrijal.popularmovies.model.Genre;
import com.sijanrijal.popularmovies.model.MovieInfo;
import com.sijanrijal.popularmovies.model.Movie;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener,
        MovieAdapter.ListItemClickListener {

    private final static String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String TRENDING_URL = "trending/movie/day?api_key=";
    private static final String POPULAR_URL = "movie/popular?api_key=";
    private static final String TOP_RATED_URL = "movie/top_rated?api_key=";

    private static final String CURRENT_SORT_KEY = "SORT";
    private static Genre moviesGenre;
    private static final String TAG = "MainActivity";
    private String mCurrentMoviesSort;

    private TextView mHeaderTextView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mProgressBar;

    private FavoriteDatabase favoriteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        favoriteDatabase = FavoriteDatabase.getInstance(getApplicationContext());

        ImageView mSortImageView = findViewById(R.id.sort_iv);
        mHeaderTextView = findViewById(R.id.header_tv);
        mProgressBar = findViewById(R.id.progressbar);
        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(new ArrayList<MovieInfo>(), this);
        mRecyclerView.setAdapter(mMovieAdapter);


        //display the menu when the user wants to change the change the way movies in the list are sorted
        mSortImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_sort, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(MainActivity.this);

                MenuBuilder menuBuilder = (MenuBuilder) popupMenu.getMenu();
                MenuPopupHelper menuPopupHelper = new MenuPopupHelper(MainActivity.this,
                        menuBuilder, v);
                menuPopupHelper.setForceShowIcon(true);
                menuPopupHelper.show();
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_SORT_KEY)) {
                mCurrentMoviesSort = savedInstanceState.getString(CURRENT_SORT_KEY, "TRENDING");
            }
        } else {
            mCurrentMoviesSort = "TRENDING";
        }
        fetchData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(CURRENT_SORT_KEY, mCurrentMoviesSort);
        super.onSaveInstanceState(outState);
    }


    private void fetchData() {
        // connect to the network and get the trending movie objects
        mProgressBar.setVisibility(View.VISIBLE);
        mHeaderTextView.setText(getString(R.string.header_text_main));
        fetchGenre();
        fetchFromNetwork(mCurrentMoviesSort);

    }


    // Handle the user selection of the movies sorting method
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_popular:
                mCurrentMoviesSort = "POPULAR";
                fetchFromNetwork(mCurrentMoviesSort);
                return true;

            case R.id.sort_top_rated:
                mCurrentMoviesSort = "TOP RATED";
                fetchFromNetwork(mCurrentMoviesSort);
                return true;

            case R.id.sort_trending:
                mCurrentMoviesSort = "TRENDING";
                fetchFromNetwork(mCurrentMoviesSort);
                return true;

            case R.id.sort_favorites:
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
                return true;

            default:
                return false;
        }
    }

    /**
     * Make the network call to the the TMDB to get the the list of movies trending this week
     **/
    void fetchFromNetwork(final String sortType) {
        OkHttpClient client = new OkHttpClient();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BASE_URL);
        switch (sortType) {
            case "TRENDING":
                stringBuilder.append(TRENDING_URL);
                break;
            case "POPULAR":
                stringBuilder.append(POPULAR_URL);
                break;
            case "TOP RATED":
                stringBuilder.append(TOP_RATED_URL);
                break;
        }
        stringBuilder.append(getString(R.string.API_KEY));

        String completeURL = stringBuilder.toString();

        Request request = new Request.Builder()
                .url(completeURL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mHeaderTextView.setText(R.string.connection_error);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException();
                }

                final String jsonString = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        parseFromJson(jsonString, sortType);
                    }
                });

            }
        });
    }


    /**
     * Make the network call to the the TMDB to get the the genres collection
     **/
    private void fetchGenre() {
        OkHttpClient client = new OkHttpClient();

        String urlString = BASE_URL +
                "genre/movie/list?api_key=" +
                getString(R.string.API_KEY);
        Request request = new Request.Builder()
                .url(urlString)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException();

                final String genreJSON = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseFromJson(genreJSON, "GENRE");
                    }
                });
            }
        });

    }

    /**
     * Fetch the movies from the json received from the network request
     *
     * @param jsonString JSON string that contains the information from the network calls.
     * @param jsonType   Type of JSON to be parsed (Genre, Trending, Popular or Top Rated)
     **/
    void parseFromJson(String jsonString, String jsonType) {

        if (jsonString != null && jsonString.length() > 0) {
            if (jsonType.equals("GENRE")) {
                parseGenreFromJSON(jsonString);
            } else {
                parseMoviesFromJSON(jsonString);
            }
        } else {
            Log.d(TAG, "fetchFromJson: Failed to parse json");
        }
    }

    /**
     * Parse the JSON that contains the trending movies of the day
     *
     * @param jsonString JSON that contains the trending movies of the day
     **/
    private void parseMoviesFromJSON(String jsonString) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Movie> jsonAdapter = moshi.adapter(Movie.class);

        try {
            Movie movie = jsonAdapter.fromJson(jsonString);
            if (movie != null) {
                mMovieAdapter.setMoviesList(movie.results);
            }
        } catch (IOException e) {
            Log.d(TAG, "fetchFromJson: Failed to parse json");
            e.printStackTrace();
        }
    }

    /**
     * Parse the Genre collection to check for genre IDs assigned to movies
     **/
    private void parseGenreFromJSON(String jsonString) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Genre> jsonAdapter = moshi.adapter(Genre.class);

        try {
            moviesGenre = jsonAdapter.fromJson(jsonString);
            Log.d(TAG, "parseGenreJSON: moviesGenre" + moviesGenre);
        } catch (IOException e) {
            Log.d(TAG, "fetchGenreJSON: Failed to parse genre");
            e.printStackTrace();
        }
    }

    /**
     * Go to the detail activity when a movie is clicked
     **/
    @Override
    public void onListItemClick(MovieInfo movieInfo) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra("MOVIE", movieInfo);
        startActivity(intent);
    }

    /**
     * Get the genres of the movie from its genre id
     *
     * @param genreId Genre Id of the movie
     **/
    public static List<String> getGenres(int[] genreId) {

        List<String> genreString = new ArrayList<>();
        if (genreId != null && genreId.length > 0) {
            if (moviesGenre.genres != null) {
                for (int id : genreId) {
                    for (Genre.Attributes genreAttributes : moviesGenre.genres) {
                        if (id == genreAttributes.id) {
                            genreString.add(genreAttributes.name);
                        }
                    }
                }
            }
        }
        return genreString;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            fetchData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}