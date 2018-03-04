package com.nenton.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by serge on 04.03.2018.
 */

public class NetworkStatusChecker {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activityNetwork = cm.getActiveNetworkInfo();
        return activityNetwork != null && activityNetwork.isConnectedOrConnecting();
    }
}
