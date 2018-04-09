package com.nenton.popularmovies.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nenton.popularmovies.R;
import com.nenton.popularmovies.data.content.MovieContract;
import com.nenton.popularmovies.data.pojo.Movie;
import com.nenton.popularmovies.network.ServiceGenerator;
import com.nenton.popularmovies.ui.interfaces.IMainView;
import com.nenton.popularmovies.network.res.MoviesResponseJson;
import com.nenton.popularmovies.network.RestService;
import com.nenton.popularmovies.ui.adapters.MainAdapter;
import com.nenton.popularmovies.utilities.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nenton.popularmovies.utilities.AppConfig.API_KEY;
import static com.nenton.popularmovies.utilities.AppConfig.QUERY_FAVORITE;
import static com.nenton.popularmovies.utilities.AppConfig.QUERY_POPULAR;
import static com.nenton.popularmovies.utilities.AppConfig.QUERY_TOP_RATED;

public class MainActivity extends AppCompatActivity implements IMainView, LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.movie_cards_rv)
    RecyclerView mMovieRV;
    @BindView(R.id.main_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.main_update_btn)
    Button mUpdateBtn;
    @BindView(R.id.main_error_tv)
    TextView mErrorMessage;
    @BindView(R.id.main_error_wrap)
    LinearLayout mErrorWrap;
    private MainAdapter mainAdapter;

    private RestService mRestService;
    private Callback<MoviesResponseJson> mCallbackNetwork;
    private int queryState;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String QUERY_STATE_KEY = "QUERY_STATE_KEY";
    private static final String SCROLL_POSITION_KEY = "SCROLL_POSITION_KEY";
    private static final int MOVIE_LOADER_ID = 5151;

    private int scrollPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRestService = ServiceGenerator.createService(RestService.class);
        ButterKnife.bind(this);
        showLoad();
        initRecyclerView();
        initNetworkQuery();
        onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(QUERY_STATE_KEY, queryState);
        outState.putInt(SCROLL_POSITION_KEY, ((LinearLayoutManager) mMovieRV.getLayoutManager()).findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            queryState = savedInstanceState.getInt(QUERY_STATE_KEY, -1);

            switch (queryState) {
                case QUERY_POPULAR:
                    networkQuery(QUERY_POPULAR);
                    break;
                case QUERY_TOP_RATED:
                    networkQuery(QUERY_TOP_RATED);
                    break;
                case QUERY_FAVORITE:
                    getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
                    break;
            }
            scrollPosition = savedInstanceState.getInt(SCROLL_POSITION_KEY, 0);
        } else {
            queryState = QUERY_POPULAR;
            networkQuery(QUERY_POPULAR);
        }
    }

    @OnClick(R.id.main_update_btn)
    void clickUpdate() {
        networkQuery(QUERY_POPULAR);
    }

    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mainAdapter = new MainAdapter(this);
        mMovieRV.setLayoutManager(manager);
        mMovieRV.setAdapter(mainAdapter);
    }

    private void initNetworkQuery() {
        if (mCallbackNetwork == null) {
            mCallbackNetwork = new Callback<MoviesResponseJson>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponseJson> call, @NonNull Response<MoviesResponseJson> response) {
                    if (response.code() == 200) {
                        mainAdapter.updateData(convertDataToList(response.body()));
                        mMovieRV.getLayoutManager().scrollToPosition(scrollPosition);
                        hideLoad();
                    } else {
                        showError(getString(R.string.response_code_error_message) + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponseJson> call, @NonNull Throwable t) {
                    showError(t.getMessage());
                }
            };
        }
    }


    @Override
    public void networkQuery(int query) {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            switch (query) {
                case QUERY_POPULAR:
                    mRestService.popularMovie(API_KEY).enqueue(mCallbackNetwork);
                    break;
                case QUERY_TOP_RATED:
                    mRestService.topRatedMovie(API_KEY).enqueue(mCallbackNetwork);
                    break;
                default:
                    hideLoad();
                    showError(getString(R.string.any_error_message));
                    break;
            }
        } else {
            showError(getString(R.string.network_error_message));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_top_rated: {
                networkQuery(QUERY_TOP_RATED);
                queryState = QUERY_TOP_RATED;
                break;
            }
            case R.id.item_popular: {
                networkQuery(QUERY_POPULAR);
                queryState = QUERY_POPULAR;
                break;
            }
            case R.id.item_favorite: {
                getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
                queryState = QUERY_FAVORITE;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoad() {
        mErrorWrap.setVisibility(View.INVISIBLE);
        mMovieRV.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoad() {
        mErrorWrap.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mMovieRV.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(String message) {
        mMovieRV.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        mErrorWrap.setVisibility(View.VISIBLE);
        mErrorMessage.setText(message);
    }

    @Override
    public void startDetailActivity(Movie result) {
        showLoad();
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_KEY, result);
        startActivity(intent);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) {
            showError("Movies in favorites are missing");
        } else {
            mainAdapter.updateData(convertDataToList(data));
            hideLoad();
        }
    }


    private List<Movie> convertDataToList(MoviesResponseJson body) {
        List<Movie> movies = new ArrayList<>();
        for (MoviesResponseJson.Result result : body.getResults()) {
            movies.add(new Movie(result.getId(),
                    result.getTitle(),
                    result.getVoteAverage(),
                    result.getPosterPath(),
                    result.getOverview(),
                    result.getReleaseDate()));
        }
        return movies;
    }

    private List<Movie> convertDataToList(Cursor data) {
        List<Movie> movies = new ArrayList<>();
        int idIndex = data.getColumnIndex(MovieContract.MovieEntry._ID);
        int idTitle = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int idVoteAverage = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
        int idPosterPath = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        int idOverview = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        int idReleaseDate = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            movies.add(new Movie(data.getInt(idIndex),
                    data.getString(idTitle),
                    data.getFloat(idVoteAverage),
                    data.getString(idPosterPath),
                    data.getString(idOverview),
                    data.getString(idReleaseDate)));
        }
        return movies;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mainAdapter.updateData(null);
    }
}
