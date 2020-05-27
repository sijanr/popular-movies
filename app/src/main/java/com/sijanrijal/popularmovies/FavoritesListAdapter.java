package com.sijanrijal.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sijanrijal.popularmovies.database.Favorite;
import com.sijanrijal.popularmovies.databinding.ListItemFavoritesBinding;

import java.util.List;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {

    private List<Favorite> mFavoriteMovies;

    @NonNull
    @Override
    public FavoritesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemFavoritesBinding listItemFavoritesBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.list_item_favorites, parent, false);
        return new ViewHolder(listItemFavoritesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesListAdapter.ViewHolder holder, int position) {
        holder.setTexts();
    }

    @Override
    public int getItemCount() {
        if (mFavoriteMovies != null) {
            return mFavoriteMovies.size();
        }
        return 0;
    }

    public void setFavoriteMovies(List<Favorite> movies) {
        mFavoriteMovies = movies;
        notifyDataSetChanged();
    }

    public List<Favorite> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemFavoritesBinding listItemFavoritesBinding;

        public ViewHolder(@NonNull ListItemFavoritesBinding listItemFavoritesBinding) {
            super(listItemFavoritesBinding.getRoot());
            this.listItemFavoritesBinding = listItemFavoritesBinding;
        }

        public void setTexts() {
            Favorite movie = mFavoriteMovies.get(getAdapterPosition());
            if (movie != null) {
                listItemFavoritesBinding.movieTitleFavorites.setText(movie.getMovieTitle());
                listItemFavoritesBinding.ratingFavorite.setText(String.valueOf(movie.getMovieRating()));
                listItemFavoritesBinding.genreFavorite.setText(movie.getMovieGenres());
                listItemFavoritesBinding.releaseFavorite.setText(movie.getMovieRelease());
            }
        }
    }
}
