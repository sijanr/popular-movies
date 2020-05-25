package com.sijanrijal.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sijanrijal.popularmovies.model.MovieInfo;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public final static String IMAGE_URL = "https://image.tmdb.org/t/p/w500";

    private List<MovieInfo> mMoviesList;

    private final ListItemClickListener mItemClickListener;

    public MovieAdapter(List<MovieInfo> moviesList, ListItemClickListener itemClickListener) {
        mMoviesList = moviesList;
        mItemClickListener = itemClickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(MovieInfo movieInfo);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_view, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setImage();
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMoviePoster;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.movie_poster_iv);
            context = itemView.getContext();

            itemView.setOnClickListener(this);
        }

        public void setImage() {
            String imageURL = IMAGE_URL + mMoviesList.get(getAdapterPosition()).poster_path;
            Glide.with(context).load(imageURL).into(mMoviePoster);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onListItemClick(mMoviesList.get(getAdapterPosition()));
        }
    }

    public void setMoviesList(List<MovieInfo> moviesList) {
        mMoviesList = moviesList;
        notifyDataSetChanged();
    }
}
