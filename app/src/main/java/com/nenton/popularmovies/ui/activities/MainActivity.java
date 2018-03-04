package com.nenton.popularmovies.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nenton.popularmovies.R;
import com.nenton.popularmovies.network.ServiceGenerator;
import com.nenton.popularmovies.ui.interfaces.IMainView;
import com.nenton.popularmovies.ui.interfaces.IRootView;
import com.nenton.popularmovies.network.MoviesJson;
import com.nenton.popularmovies.network.RestService;
import com.nenton.popularmovies.ui.adapters.MainAdapter;
import com.nenton.popularmovies.utilities.NetworkStatusChecker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nenton.popularmovies.utilities.AppConfig.API_KEY;
import static com.nenton.popularmovies.utilities.AppConfig.QUERY_POPULAR;
import static com.nenton.popularmovies.utilities.AppConfig.QUERY_TOP_RATED;

public class MainActivity extends AppCompatActivity implements IMainView {
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
    private Callback<MoviesJson> mCallbackNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRestService = ServiceGenerator.createService(RestService.class);
        ButterKnife.bind(this);
        showLoad();
        initRecyclerView();
        initNetworkQuery();
        networkQuery(QUERY_POPULAR);
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
        mCallbackNetwork = new Callback<MoviesJson>() {
            @Override
            public void onResponse(@NonNull Call<MoviesJson> call, @NonNull Response<MoviesJson> response) {
                if (response.code() == 200) {
                    mainAdapter.updateData(response.body());
                }
                hideLoad();
            }

            @Override
            public void onFailure(@NonNull Call<MoviesJson> call, @NonNull Throwable t) {
                hideLoad();
            }
        };
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
                    showError("Oops, something went wrong, please try again later");
                    break;
            }
        } else {
            showError("Network unavailable, please try later");
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
                break;
            }
            case R.id.item_popular: {
                networkQuery(QUERY_POPULAR);
                break;
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
    public void startDetailActivity(String json) {
        showLoad();
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_KEY, json);
        startActivity(intent);
    }
}
