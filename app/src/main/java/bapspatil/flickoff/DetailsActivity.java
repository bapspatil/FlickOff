package bapspatil.flickoff;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

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

        if(Build.VERSION.SDK_INT >= 21) {
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.excludeTarget(android.R.id.statusBarBackground,true);
            slide.excludeTarget(android.R.id.navigationBarBackground,true);
            getWindow().setEnterTransition(slide);
        }

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
            GlideApp.with(getApplicationContext()).load(movie.getPosterPath()).centerCrop().into(mPosterImageView);
        }
    }
}
