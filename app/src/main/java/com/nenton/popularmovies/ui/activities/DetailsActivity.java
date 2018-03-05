package com.nenton.popularmovies.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nenton.popularmovies.R;
import com.nenton.popularmovies.ui.views.ImageViewCustom;
import com.nenton.popularmovies.network.MoviesJson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nenton.popularmovies.utilities.AppConfig.BASE_IMAGE_URL;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_KEY = "EXTRA_KEY";

    @BindView(R.id.details_poster_iv)
    ImageViewCustom mImageView;
    @BindView(R.id.details_year_tv)
    TextView mYearMovie;
    @BindView(R.id.details_time_tv)
    TextView mTimeMovie;
    @BindView(R.id.details_rating_tv)
    TextView mRating;
    @BindView(R.id.details_favorite_btn)
    Button mFav;
    @BindView(R.id.details_descriptions)
    TextView mDesc;
    @BindView(R.id.details_trailers_rv)
    RecyclerView mTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        String string = getIntent().getExtras().getString(EXTRA_KEY);
        MoviesJson.Result result = new Gson().fromJson(string, MoviesJson.Result.class);
        initActivity(result);
    }

    private void initActivity(MoviesJson.Result result) {
        SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatOut = new SimpleDateFormat("yyyy", Locale.getDefault());
        try {
            Date parseDate = formatIn.parse(result.getReleaseDate());
            mYearMovie.setText(formatOut.format(parseDate));
        } catch (ParseException e) {
            e.printStackTrace();
            mYearMovie.setText("");
        }
//        mTimeMovie.setText();
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
}
