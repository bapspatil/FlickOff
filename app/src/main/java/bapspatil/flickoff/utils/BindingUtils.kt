package bapspatil.flickoff.utils

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import bapspatil.flickoff.adapters.CastRecyclerViewAdapter
import bapspatil.flickoff.adapters.MovieRecyclerViewAdapter
import bapspatil.flickoff.model.Cast
import bapspatil.flickoff.model.Movie
import java.util.*

object BindingUtils {

    @BindingAdapter("adapter")
    fun addCastItems(recyclerView: RecyclerView, casts: ArrayList<Cast>) {
        val adapter = recyclerView.adapter as CastRecyclerViewAdapter
        adapter?.clearItems()
        adapter?.addItems(casts)
    }

    @BindingAdapter("adapter")
    fun addMovieItems(recyclerView: RecyclerView, movies: ArrayList<Movie>) {
        val adapter = recyclerView.adapter as MovieRecyclerViewAdapter
        adapter?.clearItems()
        adapter?.addItems(movies)
    }

}// Not publicly instantiable
