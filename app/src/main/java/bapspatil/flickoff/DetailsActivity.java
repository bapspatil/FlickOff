package bapspatil.flickoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {


    private TextView mRatingTextView, mDateTextView, mTitleTextView, mPlotTextView;
    private ImageView mPosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        mRatingTextView = findViewById(R.id.rating_value_tv);
        mDateTextView = findViewById(R.id.date_value_tv);
        mTitleTextView = findViewById(R.id.title_tv);
        mPlotTextView = findViewById(R.id.plot_tv);
        mPosterImageView = findViewById(R.id.poster_image_view);

        Movie movie;
        Intent receivedIntent = getIntent();
        if(receivedIntent.hasExtra("movie")) {
            movie = receivedIntent.getParcelableExtra("movie");
            mRatingTextView.setText(movie.getRating());
            mDateTextView.setText(movie.getDate());
            mTitleTextView.setText(movie.getTitle());
            mPlotTextView.setText(movie.getPlot());
            Glide.with(getApplicationContext()).load(movie.getPosterPath()).centerCrop().into(mPosterImageView);
        }
    }
}
