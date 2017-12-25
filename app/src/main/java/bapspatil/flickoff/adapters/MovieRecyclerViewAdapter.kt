package bapspatil.flickoff.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import bapspatil.flickoff.R
import bapspatil.flickoff.model.Movie
import bapspatil.flickoff.network.RetrofitAPI
import bapspatil.flickoff.utils.GlideApp
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.rv_movie_item.view.*
import java.util.*


class MovieRecyclerViewAdapter(private val mContext: Context, private val mMoviesArrayList: ArrayList<Movie>?, private val mClickListener: ItemClickListener?) : RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(position: Int, posterImageView: ImageView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.rv_movie_item, viewGroup, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val theMovie = mMoviesArrayList!![position]
        GlideApp.with(mContext)
                .load(RetrofitAPI.POSTER_BASE_URL + theMovie.posterPath)
                .error(R.drawable.tmdb_placeholder)
                .fallback(R.drawable.tmdb_placeholder)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.posterImageView)
    }

    override fun getItemCount(): Int {
        return mMoviesArrayList?.size ?: 0
    }

    inner class MovieViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val posterImageView: ImageView = itemView.posterImageView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mClickListener?.onItemClick(adapterPosition, posterImageView)
        }
    }
}
