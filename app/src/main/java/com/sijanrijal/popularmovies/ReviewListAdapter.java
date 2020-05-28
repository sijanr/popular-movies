package com.sijanrijal.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sijanrijal.popularmovies.databinding.ListItemReviewsBinding;
import com.sijanrijal.popularmovies.model.Reviews;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private List<Reviews.ReviewsContent> reviewList;

    public ReviewListAdapter(List<Reviews.ReviewsContent> reviews) {
        reviewList = reviews;
    }

    public void setReviews(List<Reviews.ReviewsContent> reviews) {
        reviewList = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemReviewsBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.list_item_reviews, parent, false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setReviews(position);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ListItemReviewsBinding binding;

        public ViewHolder(@NonNull ListItemReviewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setReviews(int position) {
            binding.reviewerName.setText(reviewList.get(position).author);
            binding.review.setText(reviewList.get(position).content);
        }
    }
}
