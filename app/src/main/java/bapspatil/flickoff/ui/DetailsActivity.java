package bapspatil.flickoff.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
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
import bapspatil.flickoff.model.Movie;
import bapspatil.flickoff.model.TMDBCreditsResponse;
import bapspatil.flickoff.network.RetrofitAPI;
import bapspatil.flickoff.utils.GlideApp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {


    private TextView mRatingTextView, mDateTextView, mTitleTextView, mPlotTextView;
    private ImageView mPosterImageView;
    private RecyclerView mCastRecyclerView;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= 21) {
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.excludeTarget(android.R.id.statusBarBackground, true);
            slide.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setEnterTransition(slide);
        }

        mRatingTextView = findViewById(R.id.rating_value_tv);
        mDateTextView = findViewById(R.id.date_value_tv);
        mTitleTextView = findViewById(R.id.title_tv);
        mPlotTextView = findViewById(R.id.plot_tv);
        mPosterImageView = findViewById(R.id.poster_image_view);
        mCastRecyclerView = findViewById(R.id.cast_rv);

        Intent receivedIntent = getIntent();
        if (receivedIntent.hasExtra("movie")) {
            movie = receivedIntent.getParcelableExtra("movie");
            mRatingTextView.setText(movie.getRating());
            if (movie.getDate() != null && !movie.getDate().equals(""))
                mDateTextView.setText(prettifyDate(movie.getDate()));
            mTitleTextView.setText(movie.getTitle());
            mPlotTextView.setText(movie.getPlot());
            GlideApp.with(getApplicationContext())
                    .load(RetrofitAPI.POSTER_BASE_URL + movie.getPosterPath())
                    .centerCrop()
                    .into(mPosterImageView);
        }

        fetchCredits();
    }

    private void fetchCredits() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mCastRecyclerView.setLayoutManager(layoutManager);

        final ArrayList<Cast> castList = new ArrayList<>();
        final CastRecyclerViewAdapter mCastAdapter = new CastRecyclerViewAdapter(this, castList);
        mCastRecyclerView.setAdapter(mCastAdapter);

        RetrofitAPI retrofitAPI = RetrofitAPI.retrofit.create(RetrofitAPI.class);
        Call<TMDBCreditsResponse> creditsCall = retrofitAPI.getCredits(movie.getId(), BuildConfig.TMDB_API_TOKEN);
        creditsCall.enqueue(new Callback<TMDBCreditsResponse>() {
            @Override
            public void onResponse(Call<TMDBCreditsResponse> call, Response<TMDBCreditsResponse> response) {
                TMDBCreditsResponse creditsResponse = response.body();
                castList.clear();
                castList.addAll(creditsResponse.getCast());
                mCastAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TMDBCreditsResponse> call, Throwable t) {
                // Why bother doing anything here?
            }
        });
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
