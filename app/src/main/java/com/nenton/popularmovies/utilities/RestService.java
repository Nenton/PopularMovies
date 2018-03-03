package com.nenton.popularmovies.utilities;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by serge on 03.03.2018.
 */

public interface RestService {

    @GET("3/movie/popular/")
    Call<MoviesJson> popularMovie(@Query("api_key") String api);

    @GET("3/movie/top_rated/")
    Call<MoviesJson> topRatedMovie(@Query("api_key") String api);
}
