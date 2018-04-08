package com.nenton.popularmovies.ui.interfaces;

import com.nenton.popularmovies.data.pojo.Movie;
import com.nenton.popularmovies.network.res.MoviesResponseJson;

/**
 * Created by serge on 04.03.2018.
 */

public interface IMainView extends IRootView {
    void networkQuery(int query);
    void showError(String message);
    void startDetailActivity(Movie result);
}
