package com.nenton.popularmovies.utilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by serge on 03.03.2018.
 */

public class MoviesJson {
    @SerializedName("page")
    @Expose
    public int page;
    @SerializedName("total_results")
    @Expose
    public int totalResults;
    @SerializedName("total_pages")
    @Expose
    public int totalPages;
    @SerializedName("results")
    @Expose
    public List<Result> results = null;


    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Result> getResults() {
        return results;
    }

    public class Result {

        @SerializedName("vote_count")
        @Expose
        public int voteCount;
        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("video")
        @Expose
        public boolean video;
        @SerializedName("vote_average")
        @Expose
        public float voteAverage;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("popularity")
        @Expose
        public float popularity;
        @SerializedName("poster_path")
        @Expose
        public String posterPath;
        @SerializedName("original_language")
        @Expose
        public String originalLanguage;
        @SerializedName("original_title")
        @Expose
        public String originalTitle;
        @SerializedName("genre_ids")
        @Expose
        public List<Integer> genreIds = null;
        @SerializedName("backdrop_path")
        @Expose
        public String backdropPath;
        @SerializedName("adult")
        @Expose
        public boolean adult;
        @SerializedName("overview")
        @Expose
        public String overview;
        @SerializedName("release_date")
        @Expose
        public String releaseDate;

        public int getVoteCount() {
            return voteCount;
        }

        public int getId() {
            return id;
        }

        public boolean isVideo() {
            return video;
        }

        public float getVoteAverage() {
            return voteAverage;
        }

        public String getTitle() {
            return title;
        }

        public float getPopularity() {
            return popularity;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public boolean isAdult() {
            return adult;
        }

        public String getOverview() {
            return overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }
    }
}