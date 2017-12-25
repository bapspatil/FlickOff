package bapspatil.flickoff.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import bapspatil.flickoff.BuildConfig;
import bapspatil.flickoff.R;
import bapspatil.flickoff.adapters.MovieRecyclerViewAdapter;
import bapspatil.flickoff.model.Movie;
import bapspatil.flickoff.model.TMDBResponse;
import bapspatil.flickoff.network.RetrofitAPI;
import bapspatil.flickoff.utils.NetworkUtils;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ItemClickListener {
    private static final int SEARCH_TASK = 0, POPULAR_TASK = 1, TOP_RATED_TASK = 2, UPCOMING_TASK = 3, NOW_PLAYING_TASK = 4;

    private MovieRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ArrayList<Movie> movieArray = new ArrayList<>();
    private Context mContext;
    private Toolbar toolbar;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.LEFT);
            getWindow().setExitTransition(slide);
        }

        mContext = getApplicationContext();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        Toast.makeText(mContext, "App developed by Bapusaheb Patil", Toast.LENGTH_LONG).show();

        mProgressBar = findViewById(R.id.loading_indicator);
        mRecyclerView = findViewById(R.id.rv_movies);
        int columns = 2;
        final GridLayoutManager layoutManager = new GridLayoutManager(mContext, columns);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MovieRecyclerViewAdapter(mContext, movieArray, this);
        fetchMovies(POPULAR_TASK, null);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_popular:
                        mRecyclerView.smoothScrollToPosition(0);
                        fetchMovies(POPULAR_TASK, null);
                        break;
                    case R.id.action_rated:
                        mRecyclerView.smoothScrollToPosition(0);
                        fetchMovies(TOP_RATED_TASK, null);
                        break;
                    case R.id.action_upcoming:
                        mRecyclerView.smoothScrollToPosition(0);
                        fetchMovies(UPCOMING_TASK, null);
                        break;
                    case R.id.action_now:
                        mRecyclerView.smoothScrollToPosition(0);
                        fetchMovies(NOW_PLAYING_TASK, null);
                        break;
                }
                return true;
            }
        });
        searchView = findViewById(R.id.search_view);
        searchView.setCursorDrawable(R.drawable.cursor_search);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                mRecyclerView.smoothScrollToPosition(0);
                fetchMovies(SEARCH_TASK, query);
                searchView.closeSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                bottomNavigationView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));

    }

    @Override
    public void onItemClick(int position, ImageView posterImageView) {
        Movie movie;
        movie = movieArray.get(position);
        Intent startDetailsActivity = new Intent(mContext, DetailsActivity.class);
        startDetailsActivity.putExtra("movie", movie);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, posterImageView, "posterTransition");
        startActivity(startDetailsActivity, options.toBundle());
    }

    private void fetchMovies(int taskId, String taskQuery) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        RetrofitAPI retrofitAPI = NetworkUtils.getCacheEnabledRetrofit(mContext).create(RetrofitAPI.class);
        Call<TMDBResponse> call;
        switch (taskId) {
            case SEARCH_TASK:
                call = retrofitAPI.searchMovies(BuildConfig.TMDB_API_TOKEN, "en-US", 1, taskQuery);
                break;
            case POPULAR_TASK:
                call = retrofitAPI.getMovies("popular", BuildConfig.TMDB_API_TOKEN, "en-US", 1);
                break;
            case TOP_RATED_TASK:
                call = retrofitAPI.getMovies("top_rated", BuildConfig.TMDB_API_TOKEN, "en-US", 1);
                break;
            case UPCOMING_TASK:
                call = retrofitAPI.getMovies("upcoming", BuildConfig.TMDB_API_TOKEN, "en-US", 1);
                break;
            case NOW_PLAYING_TASK:
                call = retrofitAPI.getMovies("now_playing", BuildConfig.TMDB_API_TOKEN, "en-US", 1);
                break;
            default:
                call = retrofitAPI.getMovies("popular", BuildConfig.TMDB_API_TOKEN, "en-US", 1);
        }
        call.enqueue(new Callback<TMDBResponse>() {
            @Override
            public void onResponse(Call<TMDBResponse> call, Response<TMDBResponse> response) {
                TMDBResponse tmdbResponse = response.body();
                movieArray.clear();
                if (tmdbResponse != null) {
                    movieArray.addAll(tmdbResponse.getResults());
                    mAdapter.notifyDataSetChanged();
                }
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<TMDBResponse> call, Throwable t) {
                Toast.makeText(mContext, "Error!", Toast.LENGTH_LONG).show();
                mRecyclerView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about_me:
                Intent intentToAboutMe = new Intent(this, AboutMeActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intentToAboutMe, options.toBundle());
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen())
            searchView.closeSearch();
        else
            super.onBackPressed();
    }
}
