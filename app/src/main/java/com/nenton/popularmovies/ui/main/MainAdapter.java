package com.nenton.popularmovies.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nenton.popularmovies.R;
import com.nenton.popularmovies.utilities.ImageViewCustom;
import com.nenton.popularmovies.utilities.MoviesJson;
import com.nenton.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by serge on 26.02.2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private MoviesJson mMoviesJson;
    private Context mContext;

    public MainAdapter() {
    }

    public void updateData(MoviesJson moviesJson) {
        this.mMoviesJson = moviesJson;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(NetworkUtils.BASE_IMAGE_URL + mMoviesJson.getResults().get(position).getPosterPath());
    }

    @Override
    public int getItemCount() {
        if (mMoviesJson != null){
            return mMoviesJson.getResults().size();
        } else {
            return 0;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageViewCustom poster;

        public ViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.item_main_adapter_iv);
        }

        public void bindView(String path) {
            Picasso.with(mContext)
                    .load(path)
                    .fit()
                    .into(poster);
        }
    }
}
