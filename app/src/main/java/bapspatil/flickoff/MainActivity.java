package bapspatil.flickoff;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ItemClickListener {

    private MovieRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ArrayList<Movie> movieArray = new ArrayList<>();
    private String MOVIE_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular";
    private String MOVIE_URL_RATED = "http://api.themoviedb.org/3/movie/top_rated";
    private String MOVIE_URL_UPCOMING = "http://api.themoviedb.org/3/movie/upcoming";
    private String MOVIE_URL_NOW = "http://api.themoviedb.org/3/movie/now_playing";
    private String MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/w500";
    private Context mContext;
    public GetTheMoviesTask getTheMoviesTask;
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

        getTheMoviesTask = new GetTheMoviesTask();
        getTheMoviesTask.execute(MOVIE_URL_POPULAR);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String stringURL;
                switch (item.getItemId()) {
                    case R.id.action_popular:
                        stringURL = MOVIE_URL_POPULAR;
                        break;
                    case R.id.action_rated:
                        stringURL = MOVIE_URL_RATED;
                        break;
                    case R.id.action_upcoming:
                        stringURL = MOVIE_URL_UPCOMING;
                        break;
                    case R.id.action_now:
                        stringURL = MOVIE_URL_NOW;
                        break;
                    default:
                        stringURL = MOVIE_URL_POPULAR;
                }
                getTheMoviesTask.cancel(true);
                getTheMoviesTask = new GetTheMoviesTask();
                getTheMoviesTask.execute(stringURL);
                return true;
            }
        });
        searchView = findViewById(R.id.search_view);
        searchView.setCursorDrawable(R.drawable.cursor_search);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchTask searchTask = new SearchTask();
                searchTask.execute(query);
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


    private class GetTheMoviesTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            if (!Connection.hasNetwork(mContext)) {
                cancel(true);
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            Uri builtUri = Uri.parse(params[0]).buildUpon()
                    .appendQueryParameter("api_key", BuildConfig.TMDB_API_TOKEN)
                    .appendQueryParameter("language", "en-US")
                    .build();

            String jsonResponse;
            try {
                jsonResponse = Connection.getResponseFromHttpUrl(new URL(builtUri.toString()));
                return jsonResponse;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            movieArray.clear();
            try {
                JSONObject jsonMoviesObject = new JSONObject(jsonResponse);
                JSONArray jsonMoviesArray = jsonMoviesObject.getJSONArray("results");
                for (int i = 0; i < jsonMoviesArray.length(); i++) {
                    JSONObject jsonMovie = jsonMoviesArray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setPosterPath(MOVIE_POSTER_URL + jsonMovie.getString("poster_path"));
                    movie.setTitle(jsonMovie.getString("title"));
                    movie.setPlot(jsonMovie.getString("overview"));
                    movie.setDate(convertIntoProperDateFormat(jsonMovie.getString("release_date")));
                    movie.setId(jsonMovie.getInt("id"));
                    movie.setRating(jsonMovie.getString("vote_average"));
                    movieArray.add(movie);
                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Toast.makeText(mContext, "Error in the movie data fetched!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

    }

    private class SearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            if (!Connection.hasNetwork(mContext)) {
                cancel(true);
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Uri builtUri = Uri.parse("https://api.themoviedb.org/3/search/movie").buildUpon()
                    .appendQueryParameter("api_key", BuildConfig.TMDB_API_TOKEN)
                    .appendQueryParameter("language", "en-US")
                    .appendQueryParameter("query", strings[0])
                    .build();
            String jsonResponse;
            try {
                jsonResponse = Connection.getResponseFromHttpUrl(new URL(builtUri.toString()));
                return jsonResponse;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            movieArray.clear();
            try {
                JSONObject jsonMoviesObject = new JSONObject(jsonResponse);
                JSONArray jsonMoviesArray = jsonMoviesObject.getJSONArray("results");
                if(jsonMoviesArray.length() == 0) {
                    Toast.makeText(mContext, "No movies found!", Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    return;
                }
                else {
                    for (int i = 0; i < jsonMoviesArray.length(); i++) {
                        JSONObject jsonMovie = jsonMoviesArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setPosterPath(MOVIE_POSTER_URL + jsonMovie.getString("poster_path"));
                        movie.setTitle(jsonMovie.getString("title"));
                        movie.setPlot(jsonMovie.getString("overview"));
                        movie.setDate(convertIntoProperDateFormat(jsonMovie.getString("release_date")));
                        movie.setId(jsonMovie.getInt("id"));
                        movie.setRating(jsonMovie.getString("vote_average"));
                        movieArray.add(movie);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(mContext, "Error in the movie data fetched!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private String convertIntoProperDateFormat(String jsonDate) {
        DateFormat sourceDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        Date date = null;
        try {
            date = sourceDateFormat.parse(jsonDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat destDateFormat = new SimpleDateFormat("MMM dd\nYYYY");
        String dateStr = destDateFormat.format(date);
        return dateStr;
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
