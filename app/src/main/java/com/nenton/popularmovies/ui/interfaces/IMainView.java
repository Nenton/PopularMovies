package com.nenton.popularmovies.ui.interfaces;

/**
 * Created by serge on 04.03.2018.
 */

public interface IMainView extends IRootView {
    void networkQuery(int query);
    void showError(String message);
    void startDetailActivity(String json);
}
