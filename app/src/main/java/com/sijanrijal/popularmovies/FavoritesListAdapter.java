package com.sijanrijal.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sijanrijal.popularmovies.database.Favorite;

import java.util.List;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {

    private List<Favorite> mFavoriteMovies;

    @NonNull
    @Override
    public FavoritesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favorites, parent, false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesListAdapter.ViewHolder holder, int position) {
        holder.setTexts();
    }

    @Override
    public int getItemCount() {
        if(mFavoriteMovies != null) {
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

        private TextView movieTitle;
        private TextView rating;
        private TextView genre;
        private TextView releaseDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movieTitle = itemView.findViewById(R.id.movie_title_favorites);
            genre = itemView.findViewById(R.id.genre_favorite);
            rating = itemView.findViewById(R.id.rating_favorite);
            releaseDate = itemView.findViewById(R.id.release_favorite);
        }

        public void setTexts() {
            Favorite movie = mFavoriteMovies.get(getAdapterPosition());
            if(movie != null) {
                movieTitle.setText(movie.getMovieTitle());
                rating.setText(String.valueOf(movie.getMovieRating()));
                genre.setText(movie.getMovieGenres());
                releaseDate.setText(movie.getMovieRelease());
            }
        }
    }
}
