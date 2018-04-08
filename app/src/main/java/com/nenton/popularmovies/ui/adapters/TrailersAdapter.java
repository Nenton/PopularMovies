package com.nenton.popularmovies.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by serge on 04.03.2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private List<String> listKeyYoutube;

    public TrailersAdapter() {
        this.listKeyYoutube = new ArrayList<>();
    }

    public void updateTrailers(List<String> listKeyYoutube) {
        this.listKeyYoutube = listKeyYoutube;
        notifyDataSetChanged();
    }

    @Override
    public TrailersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailersAdapter.ViewHolder holder, int position) {
        holder.mTextTrailerView.setText(holder.mTextTrailerView.getText().toString() + " " + (position + 1));
        holder.mWebView.getSettings().setJavaScriptEnabled(true);
        holder.mWebView.loadUrl("https://www.youtube.com/embed/" + listKeyYoutube.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mWebView.setVisibility(holder.mWebView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                holder.mArrow.setImageResource(holder.mWebView.getVisibility() == View.VISIBLE ? R.drawable.ic_play_arrow_black_down_24dp : R.drawable.ic_play_arrow_black_24dp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listKeyYoutube.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextTrailerView;
        private WebView mWebView;
        private ImageView mArrow;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextTrailerView = itemView.findViewById(R.id.item_trailer_name);
            mWebView = itemView.findViewById(R.id.item_trailer_web_view);
            mArrow = itemView.findViewById(R.id.item_trailer_arrow);
        }
    }
}
