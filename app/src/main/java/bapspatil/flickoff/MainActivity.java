package bapspatil.flickoff;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ItemClickListener {

    private MovieRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Movie> movieArray = null;
    public String MOVIE_URL = "https://api.themoviedb.org/3/discover/movie";
    public String MOVIE_POSTER_URL = "https://image.tmdb.org/t/p/w185";
    public String sortingTypeSelected = "popularity.desc";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        int columns = 2;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, columns));

        GetTheMoviesTask getTheMoviesTask = new GetTheMoviesTask();
        mAdapter = new MovieRecyclerViewAdapter(this, movieArray, this);
        getTheMoviesTask.execute(sortingTypeSelected);

        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onItemClick(int position) {
        // TODO Open Details Screen here
    }


    private class GetTheMoviesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... sortType) {
            if (Connection.hasNetwork(getApplicationContext())) {
                String sortingCriteria = sortType[0];

                Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                        .appendQueryParameter("api_key", BuildConfig.TMDB_API_TOKEN)
                        .appendQueryParameter("sort_by", sortingCriteria)
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
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            if (jsonResponse != null) {
                movieArray.clear();
                try {
                    JSONObject jsonMoviesObject = new JSONObject(jsonResponse);
                    JSONArray jsonMoviesArray = jsonMoviesObject.getJSONArray("results");
                    for (int i = 0; i <= jsonMoviesArray.length(); i++) {
                        JSONObject jsonMovie = jsonMoviesArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setPosterPath(MOVIE_POSTER_URL + jsonMovie.getString("poster_path"));
                        movie.setTitle(jsonMovie.getString("title"));
                        movie.setPlot(jsonMovie.getString("overview"));
                        movie.setDate(jsonMovie.getString("release_date"));
                        movie.setId(jsonMovie.getInt("id"));
                        movie.setRating(jsonMovie.getInt("vote_count"));
                        movieArray.add(movie);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
