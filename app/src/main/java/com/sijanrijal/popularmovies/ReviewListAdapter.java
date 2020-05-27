package com.sijanrijal.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_reviews, parent, false);
        return new ViewHolder(view);
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

        private TextView reviewer;
        private TextView review;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewer = itemView.findViewById(R.id.reviewer_name);
            review = itemView.findViewById(R.id.review);
        }

        public void setReviews(int position) {
            reviewer.setText(reviewList.get(position).author);
            review.setText(reviewList.get(position).content);
        }
    }
}
