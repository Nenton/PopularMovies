package com.nenton.popularmovies.utilities;

/**
 * Created by serge on 04.03.2018.
 */

public interface AppConfig {
    String BASE_URL = "https://api.themoviedb.org/";
    String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185/";

    String API_KEY = ""; // INSERT API KEY

    int QUERY_POPULAR = 151;
    int QUERY_TOP_RATED = 251;
}
