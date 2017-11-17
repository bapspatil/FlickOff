package bapspatil.flickoff.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bapspatil.flickoff.BuildConfig;
import bapspatil.flickoff.R;
import bapspatil.flickoff.adapters.CastRecyclerViewAdapter;
import bapspatil.flickoff.model.Cast;
import bapspatil.flickoff.model.Crew;
import bapspatil.flickoff.model.Movie;
import bapspatil.flickoff.model.TMDBCreditsResponse;
import bapspatil.flickoff.network.RetrofitAPI;
import bapspatil.flickoff.utils.GlideApp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {


    private TextView mRatingTextView, mDateTextView, mTitleTextView, mPlotTextView, mDirectorTextView;
    private ImageView mPosterImageView, mBackdropImageView;
    private RecyclerView mCastRecyclerView;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (Build.VERSION.SDK_INT >= 21) {
            Slide slide = new Slide(Gravity.BOTTOM);
            getWindow().setEnterTransition(slide);
            postponeEnterTransition();
        }

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);

        mRatingTextView = findViewById(R.id.rating_value_tv);
        mDateTextView = findViewById(R.id.date_value_tv);
        mTitleTextView = findViewById(R.id.title_tv);
        mPlotTextView = findViewById(R.id.plot_tv);
        mPosterImageView = findViewById(R.id.poster_image_view);
        mCastRecyclerView = findViewById(R.id.cast_rv);
        mBackdropImageView = findViewById(R.id.backdrop_iv);
        mDirectorTextView = findViewById(R.id.director_value_tv);
        mDirectorTextView.setText("N/A"); // What if the director isn't available? Busy guy...

        Intent receivedIntent = getIntent();
        if (receivedIntent.hasExtra("movie")) {
            movie = receivedIntent.getParcelableExtra("movie");
            mRatingTextView.setText(movie.getRating());
            if (movie.getDate() != null && !movie.getDate().equals(""))
                mDateTextView.setText(prettifyDate(movie.getDate()));
            mTitleTextView.setText(movie.getTitle());
            mPlotTextView.setText(movie.getPlot());
            GlideApp.with(this)
                    .load(RetrofitAPI.POSTER_BASE_URL + movie.getPosterPath())
                    .centerCrop()
                    .into(mPosterImageView);
            GlideApp.with(this)
                    .load(RetrofitAPI.BACKDROP_BASE_URL + movie.getBackdropPath())
                    .placeholder(R.drawable.tmdb_placeholder_land)
                    .error(R.drawable.tmdb_placeholder_land)
                    .fallback(R.drawable.tmdb_placeholder_land)
                    .centerCrop()
                    .into(mBackdropImageView);
        }

        collapsingToolbarLayout.setTitle(movie.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.black));
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0)
                    mPosterImageView.setVisibility(View.GONE);
                else
                    mPosterImageView.setVisibility(View.VISIBLE);
            }
        });

        fetchCredits();

        startPostponedEnterTransition();
    }

    private void fetchCredits() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mCastRecyclerView.setLayoutManager(layoutManager);

        final ArrayList<Cast> castList = new ArrayList<>();
        final CastRecyclerViewAdapter mCastAdapter = new CastRecyclerViewAdapter(this, castList, new CastRecyclerViewAdapter.OnActorClickHandler() {
            @Override
            public void onActorClicked(String actorName) {
                try {
                    Uri uri = Uri.parse("https://www.google.com/search?q=" + actorName + " movies");
                    Intent actorMoviesIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(actorMoviesIntent);
                } catch (Exception e) {
                    // Who doesn't have Google? Or a browser?
                    e.printStackTrace();
                }
            }
        });
        mCastRecyclerView.setAdapter(mCastAdapter);

        RetrofitAPI retrofitAPI = RetrofitAPI.retrofit.create(RetrofitAPI.class);
        final Call<TMDBCreditsResponse> creditsCall = retrofitAPI.getCredits(movie.getId(), BuildConfig.TMDB_API_TOKEN);
        creditsCall.enqueue(new Callback<TMDBCreditsResponse>() {
            @Override
            public void onResponse(Call<TMDBCreditsResponse> call, Response<TMDBCreditsResponse> response) {
                TMDBCreditsResponse creditsResponse = response.body();

                // Get cast info
                castList.clear();
                if (creditsResponse.getCast().size() != 0) {
                    castList.addAll(creditsResponse.getCast());
                    mCastAdapter.notifyDataSetChanged();
                } else {
                    TextView mCastLabelTextView = findViewById(R.id.cast_label_tv);
                    mCastLabelTextView.setVisibility(View.GONE);
                    mCastRecyclerView.setVisibility(View.GONE);
                }

                // Get director info
                for(Crew crew: creditsResponse.getCrew()) {
                    if(crew.getJob().equals("Director")) {
                        mDirectorTextView.setText(crew.getName());
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<TMDBCreditsResponse> call, Throwable t) {
                // Why bother doing anything here?
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String prettifyDate(String jsonDate) {
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

}
