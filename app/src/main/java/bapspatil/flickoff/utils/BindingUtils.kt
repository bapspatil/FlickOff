package bapspatil.flickoff.utils

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import bapspatil.flickoff.R
import bapspatil.flickoff.adapters.CastRecyclerViewAdapter
import bapspatil.flickoff.model.Cast
import bapspatil.flickoff.model.Movie
import bapspatil.flickoff.network.RetrofitAPI
import bapspatil.flickoff.ui.main.MovieRecyclerViewAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.util.*

object BindingUtils {

    @BindingAdapter("adapter")
    @JvmStatic
    fun addCastItems(recyclerView: RecyclerView, casts: ArrayList<Cast>) {
        var adapter: CastRecyclerViewAdapter? = recyclerView.adapter as CastRecyclerViewAdapter?
        adapter?.clearItems()
        adapter?.addItems(casts)
    }

    @BindingAdapter("adapter")
    @JvmStatic
    fun addMovieItems(recyclerView: RecyclerView, movies: ArrayList<Movie>) {
        var adapter: MovieRecyclerViewAdapter? = recyclerView.adapter as MovieRecyclerViewAdapter?
        adapter?.clearItems()
        adapter?.addItems(movies)
    }

    @BindingAdapter("poster")
    @JvmStatic
    fun setMoviePoster(imageView: ImageView, posterPath: String) {
        GlideApp.with(imageView.context)
                .load(RetrofitAPI.POSTER_BASE_URL + posterPath)
                .error(R.drawable.tmdb_placeholder)
                .fallback(R.drawable.tmdb_placeholder)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
    }
} // Not publicly instantiable
