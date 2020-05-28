package com.sijanrijal.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.sijanrijal.popularmovies.database.Favorite;
import com.sijanrijal.popularmovies.database.FavoriteDatabase;
import com.sijanrijal.popularmovies.databinding.ActivityFavoritesBinding;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private static final String TAG = "FavoritesActivity";


    private FavoritesListAdapter mAdapter;
    private FavoriteDatabase mDatabase;

    private ActivityFavoritesBinding activityFavoritesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFavoritesBinding = DataBindingUtil.setContentView(this,R.layout.activity_favorites);
        setSupportActionBar(activityFavoritesBinding.toolbarFavorites);

        mDatabase = FavoriteDatabase.getInstance(getApplicationContext());

        activityFavoritesBinding.recyclerViewFavorites.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        activityFavoritesBinding.recyclerViewFavorites.setLayoutManager(layoutManager);
        mAdapter = new FavoritesListAdapter();
        setupViewModel();
        activityFavoritesBinding.recyclerViewFavorites.setAdapter(mAdapter);

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
        }).attachToRecyclerView(activityFavoritesBinding.recyclerViewFavorites);


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