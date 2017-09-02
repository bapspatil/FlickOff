package bapspatil.flickoff;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

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
    private String sortingTypeSelected = "popularity.desc";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        int columns = 2;
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, columns));

        mAdapter = new MovieRecyclerViewAdapter(mContext, movieArray, this);
        SlideInBottomAnimatorAdapter animatorAdapter = new SlideInBottomAnimatorAdapter(mAdapter, mRecyclerView);
        mRecyclerView.setAdapter(animatorAdapter);

        GetTheMoviesTask getTheMoviesTask = new GetTheMoviesTask();
        getTheMoviesTask.execute(sortingTypeSelected);
    }


    @Override
    public void onItemClick(int position) {
        // TODO Open Details Screen here
    }


    private class GetTheMoviesTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... sortType) {
            if(Connection.hasNetwork(mContext)) {
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
        protected void onPostExecute(String jsonResponse) {

            mProgressBar.setVisibility(View.INVISIBLE);
            try {
                    JSONObject jsonMoviesObject = new JSONObject(jsonResponse);
                    JSONArray jsonMoviesArray = jsonMoviesObject.getJSONArray("results");
                    for (int i = 0; i < jsonMoviesObject.getInt("total_results"); i++) {
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
                e.printStackTrace();
            }

        }
    }
}
