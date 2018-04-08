package com.nenton.popularmovies.network;

import com.nenton.popularmovies.network.res.MoviesResponseJson;
import com.nenton.popularmovies.network.res.ReviewsResponseJson;
import com.nenton.popularmovies.network.res.VideosResponseJson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by serge on 03.03.2018.
 */

public interface RestService {

    @GET("3/movie/popular")
    Call<MoviesResponseJson> popularMovie(@Query("api_key") String api);

    @GET("3/movie/top_rated")
    Call<MoviesResponseJson> topRatedMovie(@Query("api_key") String api);

    @GET("3/movie/{id}/reviews")
    Call<ReviewsResponseJson> reviewsMovie(@Path("id") String id, @Query("api_key") String api);

    @GET("3/movie/{id}/videos")
    Call<VideosResponseJson> videosMovie(@Path("id") String id, @Query("api_key") String api);
}
