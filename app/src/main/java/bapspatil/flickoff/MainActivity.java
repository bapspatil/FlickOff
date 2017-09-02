package bapspatil.flickoff;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;

import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ItemClickListener {

    private MovieRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ArrayList<Movie> movieArray = new ArrayList<>();
    private String MOVIE_URL = "http://api.themoviedb.org/3/discover/movie";
    private String MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/w500";
    private String sortingTypeSelected;
    private Context mContext;
    private boolean popularOrNot;
    public GetTheMoviesTask getTheMoviesTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        Toast.makeText(mContext, "App developed by Bapusaheb Patil", Toast.LENGTH_SHORT).show();

        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        Spinner mSpinner = (Spinner) findViewById(R.id.sort_spinner);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        int columns = 2;
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, columns));

        mAdapter = new MovieRecyclerViewAdapter(mContext, movieArray, this);
        SlideInBottomAnimatorAdapter animatorAdapter = new SlideInBottomAnimatorAdapter(mAdapter, mRecyclerView);
        mRecyclerView.setAdapter(animatorAdapter);

        popularOrNot = true;
        getTheMoviesTask = new GetTheMoviesTask();
        getTheMoviesTask.execute(popularOrNot);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (selected.equals("Most Popular")) {
                    popularOrNot = true;
                } else {
                    popularOrNot = false;
                }
                getTheMoviesTask.cancel(true);
                getTheMoviesTask = new GetTheMoviesTask();
                getTheMoviesTask.execute(popularOrNot);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onItemClick(int position) {
        Movie movie;
        movie = movieArray.get(position);
        Intent startDetailsActivity = new Intent(mContext, DetailsActivity.class);
        startDetailsActivity.putExtra("movie", movie);
        startActivity(startDetailsActivity);
    }

    private class GetTheMoviesTask extends AsyncTask<Boolean, Void, String> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(Boolean... params) {
            if (params[0])
                sortingTypeSelected = "popularity.desc";
            else
                sortingTypeSelected = "vote_average.desc";
            if (Connection.hasNetwork(mContext)) {

                Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                        .appendQueryParameter("api_key", BuildConfig.TMDB_API_TOKEN)
                        .appendQueryParameter("sort_by", sortingTypeSelected)
                        .appendQueryParameter("year", "2017")
                        .build();

                String jsonResponse;
                try {
                    jsonResponse = Connection.getResponseFromHttpUrl(new URL(builtUri.toString()));
                    return jsonResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                Toast.makeText(mContext, "No Internet connection!", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            movieArray.clear();
            mProgressBar.setVisibility(View.INVISIBLE);
            try {
                JSONObject jsonMoviesObject = new JSONObject(jsonResponse);
                JSONArray jsonMoviesArray = jsonMoviesObject.getJSONArray("results");
                for (int i = 0; i < jsonMoviesArray.length(); i++) {
                    JSONObject jsonMovie = jsonMoviesArray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setPosterPath(MOVIE_POSTER_URL + jsonMovie.getString("poster_path"));
                    movie.setTitle(jsonMovie.getString("title"));
                    movie.setPlot(jsonMovie.getString("overview"));
                    movie.setDate(jsonMovie.getString("release_date"));
                    movie.setId(jsonMovie.getInt("id"));
                    movie.setRating(jsonMovie.getString("vote_average"));
                    movieArray.add(movie);
                    mAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                Toast.makeText(mContext, "Error in the movie data fetched!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

    }
}
