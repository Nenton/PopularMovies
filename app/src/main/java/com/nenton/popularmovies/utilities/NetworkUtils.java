package com.nenton.popularmovies.utilities;

import java.io.IOException;

/**
 * Created by serge on 19.02.2018.
 */

public class NetworkUtils {

    public static final String BASE_URL = "https://api.themoviedb.org/";
    public static final String API_KEY = "af9afc6bd406ee4d5f0cffbccb6e0fbd";
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w342/";

    public static RestService getRes() throws IOException {
        return ServiceGenerator.createService(RestService.class);
    }
}
