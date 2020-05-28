package com.sijanrijal.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sijanrijal.popularmovies.databinding.ListItemTrailersBinding;
import com.sijanrijal.popularmovies.model.Trailer;

import java.util.List;

public class TrailersListAdapter extends RecyclerView.Adapter<TrailersListAdapter.ViewHolder> {
    private static final String TAG = "TrailersListAdapter";
    private static final String trailerURL = "https://www.youtube.com/watch/";
    private List<Trailer.TrailerLink> trailerLists;
    private OnListItemClick clickActivity;

    public TrailersListAdapter(List<Trailer.TrailerLink> trailer, OnListItemClick clickActivity) {
        trailerLists = trailer;
        this.clickActivity = clickActivity;
    }

    public interface OnListItemClick {
        void onClick(String key);
    }

    @NonNull
    @Override
    public TrailersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemTrailersBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.list_item_trailers, parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersListAdapter.ViewHolder holder, int position) {
        holder.setupViews();
    }

    @Override
    public int getItemCount() {
        return trailerLists.size();
    }

    public void setTrailers(List<Trailer.TrailerLink> list) {
        trailerLists = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemTrailersBinding binding;
        public ViewHolder(@NonNull ListItemTrailersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String key = trailerLists.get(getAdapterPosition()).key;
                    clickActivity.onClick(key);
                }
            });
        }

        public void setupViews() {
            String text = "Watch trailer " + (getAdapterPosition() +1);
            binding.textView.setText(text);
        }
    }
}
