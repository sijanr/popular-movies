package com.sijanrijal.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.sijanrijal.popularmovies.databinding.ActivityMainBinding;
import com.sijanrijal.popularmovies.databinding.ContentMainBinding;
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

    public final static String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String TRENDING_URL = "trending/movie/day?api_key=";
    private static final String POPULAR_URL = "movie/popular?api_key=";
    private static final String TOP_RATED_URL = "movie/top_rated?api_key=";

    private static final String CURRENT_SORT_KEY = "SORT";
    public static final String TRENDING_MOVIES = "TRENDING";
    public static final String POPULAR_MOVIES = "POPULAR";
    public static final String TOP_RATED_MOVIES = "TOP RATED";
    public static final String FAVORITE_MOVIES = "FAVORITE";
    private static Genre moviesGenre;
    private static final String TAG = "MainActivity";
    private String mCurrentMoviesSort;

    //Adapter to display the movies from the sort selection
    private MovieAdapter mMovieAdapter;

    //Adapter to display favorite movies
    private FavoritesListAdapter mFavoriteListAdapter;

    private FavoriteDatabase favoriteDatabase;

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        favoriteDatabase = FavoriteDatabase.getInstance(getApplicationContext());


        setSupportActionBar(mainBinding.mainToolbar);


        mainBinding.included.recyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(new ArrayList<MovieInfo>(), this);
        mFavoriteListAdapter = new FavoritesListAdapter();
        setupRecyclerView(TRENDING_MOVIES);
        setupViewModel();


        //display the menu when the user wants to change the change the way movies in the list are sorted
        mainBinding.included.sortIv.setOnClickListener(new View.OnClickListener() {
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
                mCurrentMoviesSort = savedInstanceState.getString(CURRENT_SORT_KEY, TRENDING_MOVIES);
            }
        } else {
            mCurrentMoviesSort = TRENDING_MOVIES;
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

    /**
     * Setup viewmodel to query database every time it's changed
     **/
    private void setupViewModel() {
        FavoritesViewModel viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);

        //observe the change in the database
        viewModel.getFavoriteMovies().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> movies) {
                Log.d(TAG, "onChanged: Receiving database update from LiveData");
                mFavoriteListAdapter.setFavoriteMovies(movies);
            }
        });

    }


    /**
     * Fetch genre and list of movies from user-provided sort type using the Internet
     **/
    private void fetchData() {
        // connect to the network and get the trending movie objects
        mainBinding.included.progressbar.setVisibility(View.VISIBLE);
        fetchGenre();
        fetchFromNetwork(mCurrentMoviesSort);

    }


    // Handle the user selection of the movies sorting method
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_popular:
                mCurrentMoviesSort = POPULAR_MOVIES;
                fetchFromNetwork(mCurrentMoviesSort);
                setupRecyclerView(mCurrentMoviesSort);
                return true;

            case R.id.sort_top_rated:
                mCurrentMoviesSort = TOP_RATED_MOVIES;
                fetchFromNetwork(mCurrentMoviesSort);
                setupRecyclerView(TOP_RATED_MOVIES);
                return true;

            case R.id.sort_trending:
                mCurrentMoviesSort = TRENDING_MOVIES;
                fetchFromNetwork(mCurrentMoviesSort);
                setupRecyclerView(TRENDING_MOVIES);
                return true;

            case R.id.sort_favorites:
                mainBinding.included.headerTv.setText(getString(R.string.favorites_header));
                setupRecyclerView(FAVORITE_MOVIES);
                return true;

            default:
                return false;
        }
    }


    /**
     * Set RecyclerView's layout manager and adapter
     **/
    private void setupRecyclerView(String sortBy) {
        if (sortBy.equals(FAVORITE_MOVIES)) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            mainBinding.included.recyclerView.setLayoutManager(linearLayoutManager);
            mainBinding.included.recyclerView.setAdapter(mFavoriteListAdapter);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,
                    GridLayoutManager.VERTICAL, false);
            mainBinding.included.recyclerView.setLayoutManager(gridLayoutManager);
            mainBinding.included.recyclerView.setAdapter(mMovieAdapter);
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
            case TRENDING_MOVIES:
                stringBuilder.append(TRENDING_URL);
                break;
            case POPULAR_MOVIES:
                stringBuilder.append(POPULAR_URL);
                break;
            case TOP_RATED_MOVIES:
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
                        mainBinding.included.progressbar.setVisibility(View.INVISIBLE);
                        mainBinding.included.headerTv.setText(R.string.connection_error);
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
                        mainBinding.included.progressbar.setVisibility(View.INVISIBLE);
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
                mainBinding.included.headerTv.setText(getString(R.string.header_text_main));
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