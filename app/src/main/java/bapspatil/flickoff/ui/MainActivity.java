package bapspatil.flickoff.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
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
import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;
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
        mContext = getApplicationContext();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        if(Build.VERSION.SDK_INT >= 21) {
            getWindow().setExitTransition(new Explode());
        }

        Toast.makeText(mContext, "App developed by Bapusaheb Patil", Toast.LENGTH_LONG).show();

        mProgressBar = findViewById(R.id.loading_indicator);
        mRecyclerView = findViewById(R.id.rv_movies);
        int columns = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            columns = 4;
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, columns));

        mAdapter = new MovieRecyclerViewAdapter(mContext, movieArray, this);
        ScaleInAnimatorAdapter<MovieRecyclerViewAdapter.MovieViewHolder> animatorAdapter = new ScaleInAnimatorAdapter<>(mAdapter, mRecyclerView);
        mRecyclerView.setAdapter(animatorAdapter);

        fetchMovies(POPULAR_TASK, null);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_popular:
                        fetchMovies(POPULAR_TASK, null);
                        break;
                    case R.id.action_rated:
                        fetchMovies(TOP_RATED_TASK, null);
                        break;
                    case R.id.action_upcoming:
                        fetchMovies(UPCOMING_TASK, null);
                        break;
                    case R.id.action_now:
                        fetchMovies(NOW_PLAYING_TASK, null);
                        break;
                    default:
                        fetchMovies(POPULAR_TASK, null);
                }
                return true;
            }
        });
        searchView = findViewById(R.id.search_view);
        searchView.setCursorDrawable(R.drawable.cursor_search);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
        RetrofitAPI retrofitAPI = RetrofitAPI.retrofit.create(RetrofitAPI.class);
        Call<TMDBResponse> call;
        switch (taskId) {
            case SEARCH_TASK:
                call = retrofitAPI.searchMovies(BuildConfig.TMDB_API_TOKEN, "en-US", 1, taskQuery);
                break;
            case POPULAR_TASK:
                call = retrofitAPI.fetchPopularMovies(BuildConfig.TMDB_API_TOKEN, "en-US", 1);
                break;
            case TOP_RATED_TASK:
                call = retrofitAPI.fetchTopRatedMovies(BuildConfig.TMDB_API_TOKEN, "en-US", 1);
                break;
            case UPCOMING_TASK:
                call = retrofitAPI.fetchUpcomingMovies(BuildConfig.TMDB_API_TOKEN, "en-US", 1);
                break;
            case NOW_PLAYING_TASK:
                call = retrofitAPI.fetchNowPlayingMovies(BuildConfig.TMDB_API_TOKEN, "en-US", 1);
                break;
            default:
                call = retrofitAPI.fetchPopularMovies(BuildConfig.TMDB_API_TOKEN, "en-US", 1);
        }
        call.enqueue(new Callback<TMDBResponse>() {
            @Override
            public void onResponse(Call<TMDBResponse> call, Response<TMDBResponse> response) {
                TMDBResponse tmdbResponse = response.body();
                movieArray.clear();
                movieArray.addAll(tmdbResponse.getResults());
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<TMDBResponse> call, Throwable t) {
                Toast.makeText(mContext, "Error!", Toast.LENGTH_LONG).show();
                mRecyclerView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
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
    public void onBackPressed() {
        if (searchView.isSearchOpen())
            searchView.closeSearch();
        else
            super.onBackPressed();
    }
}
