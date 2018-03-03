package com.nenton.popularmovies.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nenton.popularmovies.R;
import com.nenton.popularmovies.ui.interfaces.IRootView;
import com.nenton.popularmovies.utilities.MoviesJson;
import com.nenton.popularmovies.utilities.NetworkUtils;
import com.nenton.popularmovies.utilities.RestService;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements IRootView {

    //    private static final String URL_KEY = "URL_KEY";
//    private static final int NETWORK_QUERY = 51;
    private static final int QUERY_POPULAR = 151;
    private static final int QUERY_TOP_RATED = 251;

    @BindView(R.id.movie_cards_rv)
    RecyclerView mMovieRV;

    private MainAdapter mainAdapter;
    private RestService mRestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            mRestService = NetworkUtils.getRes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ButterKnife.bind(this);
        initRecyclerView();
        initNetworkQuery(QUERY_POPULAR);
//        showMessage("START");
    }

    private void initNetworkQuery(int query) {

        Callback<MoviesJson> callback = new Callback<MoviesJson>() {
            @Override
            public void onResponse(@NonNull Call<MoviesJson> call, @NonNull Response<MoviesJson> response) {
                if (response.code() == 200) {
                    mainAdapter.updateData(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesJson> call, @NonNull Throwable t) {

            }
        };


        switch (query) {
            case QUERY_POPULAR:
                mRestService.popularMovie(NetworkUtils.API_KEY).enqueue(callback);
                break;
            case QUERY_TOP_RATED:
                mRestService.topRatedMovie(NetworkUtils.API_KEY).enqueue(callback);
                break;
            default:
//                showError();
                return;
        }

    }

    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mainAdapter = new MainAdapter();

        mMovieRV.setLayoutManager(manager);
        mMovieRV.setAdapter(mainAdapter);
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
                initNetworkQuery(QUERY_TOP_RATED);
                break;
            }
            case R.id.item_popular: {
                initNetworkQuery(QUERY_POPULAR);
                break;
            }
            default: {
//                showError();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoad() {

    }

    @Override
    public void hideLoad() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
