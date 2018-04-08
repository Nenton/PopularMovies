package com.nenton.popularmovies.utilities;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by serge on 08.04.2018.
 */

public class ApplicationMovie extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
