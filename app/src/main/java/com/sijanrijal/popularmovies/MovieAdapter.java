package com.sijanrijal.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sijanrijal.popularmovies.databinding.ListItemViewBinding;
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
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemViewBinding listItemViewBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.list_item_view, parent, false
        );
        return new ViewHolder(listItemViewBinding);

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
        private final ListItemViewBinding binding;
        private Context context;

        public ViewHolder(@NonNull ListItemViewBinding listItemViewBinding) {
            super(listItemViewBinding.getRoot());
            binding = listItemViewBinding;
            context = itemView.getContext();

            itemView.setOnClickListener(this);
        }

        public void setImage() {
            String imageURL = IMAGE_URL + mMoviesList.get(getAdapterPosition()).poster_path;
            Glide.with(context).load(imageURL).into(binding.moviePosterIv);
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
