package com.nenton.popularmovies.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nenton.popularmovies.R;
import com.nenton.popularmovies.data.pojo.Review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by serge on 06.04.2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> reviews;

    public ReviewsAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mAuthor.setText(reviews.get(position).getAuthor());
        holder.mContent.setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (reviews == null) {
            return 0;
        }
        return reviews.size();
    }

    public void updateReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mAuthor;
        private TextView mContent;

        public ViewHolder(View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.review_author);
            mContent = itemView.findViewById(R.id.review_content);
        }
    }
}
