package com.nenton.popularmovies.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serge on 03.03.2018.
 */

public class MoviesJson {
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;


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
        private int voteCount;
        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("video")
        @Expose
        private boolean video;
        @SerializedName("vote_average")
        @Expose
        private float voteAverage;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("popularity")
        @Expose
        private float popularity;
        @SerializedName("poster_path")
        @Expose
        private String posterPath;
        @SerializedName("original_language")
        @Expose
        private String originalLanguage;
        @SerializedName("original_title")
        @Expose
        private String originalTitle;
        @SerializedName("genre_ids")
        @Expose
        private List<Integer> genreIds = null;
        @SerializedName("backdrop_path")
        @Expose
        private String backdropPath;
        @SerializedName("adult")
        @Expose
        private boolean adult;
        @SerializedName("overview")
        @Expose
        private String overview;
        @SerializedName("release_date")
        @Expose
        private String releaseDate;

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