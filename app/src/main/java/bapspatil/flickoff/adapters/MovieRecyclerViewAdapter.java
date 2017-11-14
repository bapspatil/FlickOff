package bapspatil.flickoff.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

import bapspatil.flickoff.R;
import bapspatil.flickoff.model.Movie;
import bapspatil.flickoff.network.RetrofitAPI;
import bapspatil.flickoff.utils.GlideApp;


public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private ArrayList<Movie> mMoviesArrayList;
    private Context mContext;
    private ItemClickListener mClickListener;

    public interface ItemClickListener {
        void onItemClick(int position, CardView posterCardView);
    }

    public MovieRecyclerViewAdapter(Context context, ArrayList<Movie> movieArrayList, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.mMoviesArrayList = movieArrayList;
        this.mClickListener = itemClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_movie_item, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie theMovie = mMoviesArrayList.get(position);
        GlideApp.with(mContext)
                .load(RetrofitAPI.POSTER_BASE_URL + theMovie.getPosterPath())
                .placeholder(R.drawable.tmdb_placeholder)
                .error(R.drawable.tmdb_placeholder)
                .fallback(R.drawable.tmdb_placeholder)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        if (mMoviesArrayList == null) return 0;
        else return mMoviesArrayList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mPosterImageView;
        CardView mPosterCardView;

        MovieViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = itemView.findViewById(R.id.poster_image_view);
            mPosterCardView = itemView.findViewById(R.id.poster_card_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClick(getAdapterPosition(), mPosterCardView);
        }
    }
}
