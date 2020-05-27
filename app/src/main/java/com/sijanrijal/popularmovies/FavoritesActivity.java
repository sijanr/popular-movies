package com.sijanrijal.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.sijanrijal.popularmovies.database.Favorite;
import com.sijanrijal.popularmovies.database.FavoriteDatabase;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private static final String TAG = "FavoritesActivity";

    private RecyclerView mRecyclerView;
    private FavoritesListAdapter mAdapter;
    private FavoriteDatabase mDatabase;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        toolbar = findViewById(R.id.toolbar_favorites);
        setSupportActionBar(toolbar);

        mDatabase = FavoriteDatabase.getInstance(getApplicationContext());

        mRecyclerView = findViewById(R.id.recycler_view_favorites);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new FavoritesListAdapter();
        setupViewModel();
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Favorite> movies = mAdapter.getFavoriteMovies();
                        mDatabase.favoritesDAO().deleteFavorite(movies.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);


    }

    /**
     * Setup viewmodel to query database every time it's changed
     * **/
    private void setupViewModel() {
        FavoritesViewModel viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);

        //observe the change in the database
        viewModel.getFavoriteMovies().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> movies) {
                Log.d(TAG, "onChanged: Receiving database update from LiveData");
                mAdapter.setFavoriteMovies(movies);
            }
        });

    }
}