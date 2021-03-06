package com.nenton.popularmovies.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.nenton.popularmovies.R;
import com.nenton.popularmovies.data.pojo.Movie;
import com.nenton.popularmovies.ui.activities.MainActivity;
import com.nenton.popularmovies.ui.views.ImageViewCustom;
import com.nenton.popularmovies.network.res.MoviesResponseJson;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.nenton.popularmovies.utilities.AppConfig.BASE_IMAGE_URL;

/**
 * Created by serge on 26.02.2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Movie> mMoviesJson;
    private Context mContext;
    private MainActivity mActivity;

    public MainAdapter(MainActivity mainActivity) {
        mActivity = mainActivity;
    }

    public void updateData(List<Movie> moviesJson) {
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
        holder.bindView(BASE_IMAGE_URL + mMoviesJson.get(position).getPosterPath());
    }

    @Override
    public int getItemCount() {
        return mMoviesJson != null ? mMoviesJson.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageViewCustom poster;

        ViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.item_main_adapter_iv);
            poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie result = mMoviesJson.get(getAdapterPosition());
                    String json = new Gson().toJson(result);
                    mActivity.startDetailActivity(result);
                }
            });
        }

        void bindView(String path) {
            Picasso.with(mContext)
                    .load(path)
                    .fit()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(poster);
        }
    }
}
