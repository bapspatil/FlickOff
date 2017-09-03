package bapspatil.flickoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {


    private TextView mRatingTextView, mDateTextView, mTitleTextView, mPlotTextView;
    private ImageView mPosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mRatingTextView = (TextView) findViewById(R.id.rating_value_tv);
        mDateTextView = (TextView) findViewById(R.id.date_value_tv);
        mTitleTextView = (TextView) findViewById(R.id.title_tv);
        mPlotTextView = (TextView) findViewById(R.id.plot_tv);
        mPosterImageView = (ImageView) findViewById(R.id.poster_image_view);

        Movie movie = new Movie();
        Intent receivedIntent = getIntent();
        if(receivedIntent.hasExtra("movie")) {
            movie = receivedIntent.getParcelableExtra("movie");
            mRatingTextView.setText(movie.getRating());
            mDateTextView.setText(movie.getDate());
            mTitleTextView.setText(movie.getTitle());
            mPlotTextView.setText(movie.getPlot());
            Picasso.with(getApplicationContext()).load(movie.getPosterPath()).into(mPosterImageView);
        }
    }
}
