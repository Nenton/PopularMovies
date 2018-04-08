package com.nenton.popularmovies.ui.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nenton.popularmovies.R;
import com.nenton.popularmovies.data.content.MovieContract;
import com.nenton.popularmovies.data.pojo.Movie;
import com.nenton.popularmovies.data.pojo.Review;
import com.nenton.popularmovies.network.RestService;
import com.nenton.popularmovies.network.ServiceGenerator;
import com.nenton.popularmovies.network.res.ReviewsResponseJson;
import com.nenton.popularmovies.network.res.VideosResponseJson;
import com.nenton.popularmovies.ui.adapters.ReviewsAdapter;
import com.nenton.popularmovies.ui.adapters.TrailersAdapter;
import com.nenton.popularmovies.ui.interfaces.IDetailsView;
import com.nenton.popularmovies.ui.views.ImageViewCustom;
import com.nenton.popularmovies.utilities.AppConfig;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nenton.popularmovies.utilities.AppConfig.BASE_IMAGE_URL;

public class DetailsActivity extends AppCompatActivity implements IDetailsView {

    public static final String EXTRA_KEY = "EXTRA_KEY";

    @BindView(R.id.details_poster_iv)
    ImageViewCustom mImageView;
    @BindView(R.id.details_year_tv)
    TextView mYearMovie;
    @BindView(R.id.details_rating_tv)
    TextView mRating;
    @BindView(R.id.details_favorite_btn)
    Button mFav;
    @BindView(R.id.details_descriptions)
    TextView mDesc;
    @BindView(R.id.details_trailers_rv)
    RecyclerView mTrailers;
    @BindView(R.id.details_reviews_rv)
    RecyclerView mReviews;

    private RestService mRestService;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private Movie result;

    @OnClick(R.id.details_favorite_btn)
    public void onClickFavorite() {
        loadMovieInDb(result);
    }

    private void loadMovieInDb(Movie result) {
        if (!isFavoriteMovie(result.getId())) {
            insertMovieToFavorite(result);
            buttonIsFavorite(true);
        } else {
            deleteMovieFromFavorite(result.getId());
            buttonIsFavorite(false);
        }
    }

    private boolean isFavoriteMovie(int id) {
        Log.e("isFavoriteMovie", Integer.toString(id));
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry._ID},
                MovieContract.MovieEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null);

        return cursor != null && cursor.getCount() > 0;
    }

    private boolean insertMovieToFavorite(Movie result) {
        Log.e("insertMovieToFavorite", Integer.toString(result.getId()));
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID, result.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, result.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, result.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, result.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, result.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, result.getOverview());

        Uri insert = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        return insert != null;
    }

    private boolean deleteMovieFromFavorite(int id) {
        Log.e("deleteMovieFromFavorite", Integer.toString(id));
        String stringId = Integer.toString(id);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        return getContentResolver().delete(uri, null, null) > 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mRestService = ServiceGenerator.createService(RestService.class);
        ButterKnife.bind(this);
        result = getIntent().getParcelableExtra(EXTRA_KEY);
        initActivity(result);
        initTrailers();
        initTrailersNetwork(result.getId());
        initReviewsNetwork(result.getId());
    }

    private void initTrailersNetwork(int id) {
        mRestService.videosMovie(String.valueOf(id), AppConfig.API_KEY)
                .enqueue(new Callback<VideosResponseJson>() {
                    @Override
                    public void onResponse(Call<VideosResponseJson> call, Response<VideosResponseJson> response) {
                        if (response.code() == 200) {
                            ArrayList<String> keys = new ArrayList<>();
                            for (VideosResponseJson.Result result : response.body().getResults()) {
                                keys.add(result.getKey());
                            }
                            mTrailersAdapter.updateTrailers(keys);
                        }
                    }

                    @Override
                    public void onFailure(Call<VideosResponseJson> call, Throwable t) {

                    }
                });
    }

    private void initReviewsNetwork(int id) {
        mRestService.reviewsMovie(String.valueOf(id), AppConfig.API_KEY)
                .enqueue(new Callback<ReviewsResponseJson>() {
                    @Override
                    public void onResponse(Call<ReviewsResponseJson> call, Response<ReviewsResponseJson> response) {
                        if (response.code() == 200) {
                            List<Review> reviews = new ArrayList<>();
                            for (ReviewsResponseJson.Result result : response.body().getResults()) {
                                reviews.add(new Review(result.getAuthor(), result.getContent()));
                            }
                            mReviewsAdapter.updateReviews(reviews);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewsResponseJson> call, Throwable t) {

                    }
                });
    }

    private void initActivity(Movie result) {
        SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatOut = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        try {
            Date parseDate = formatIn.parse(result.getReleaseDate());
            mYearMovie.setText(formatOut.format(parseDate));
        } catch (ParseException e) {
            e.printStackTrace();
            mYearMovie.setText("");
        }

        String rating = result.getVoteAverage() + "/10";
        mRating.setText(rating);
        mDesc.setText(result.getOverview());
        setTitle(result.getTitle());

        Picasso.with(this)
                .load(BASE_IMAGE_URL + result.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(mImageView);

    }

    private void initTrailers() {
        LinearLayoutManager managerTrailer = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailersAdapter = new TrailersAdapter();

        mTrailers.setNestedScrollingEnabled(false);
        mTrailers.setLayoutManager(managerTrailer);
        mTrailers.setAdapter(mTrailersAdapter);

        LinearLayoutManager managerReviews = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewsAdapter = new ReviewsAdapter();

        mReviews.setNestedScrollingEnabled(false);
        mReviews.setLayoutManager(managerReviews);
        mReviews.setAdapter(mReviewsAdapter);

        buttonIsFavorite(isFavoriteMovie(result.getId()));
    }

    private void buttonIsFavorite(boolean is){
        if (is){
            mFav.setText("Movie in favorite");
            mFav.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent),PorterDuff.Mode.MULTIPLY);
        } else {
            mFav.setText("Mark as favorite");
            mFav.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),PorterDuff.Mode.MULTIPLY);
        }
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
