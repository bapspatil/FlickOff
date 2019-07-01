package bapspatil.flickoff.ui.main

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import androidx.databinding.ObservableField
import android.view.View
import android.widget.ImageView
import bapspatil.flickoff.model.Movie

class MovieItemViewModel(private val mMovie: Movie, private val mListener: MovieItemViewModelListener) {

    val poster: ObservableField<String> = ObservableField(mMovie.posterPath)

    fun onItemClick(view: View) {
        mListener.onItemClick(mMovie, view as ImageView)
    }

    interface MovieItemViewModelListener {
        fun onItemClick(movie: Movie, imageView: ImageView)
    }
}
