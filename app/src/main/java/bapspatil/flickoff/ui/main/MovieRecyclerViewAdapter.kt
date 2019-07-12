package bapspatil.flickoff.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import bapspatil.flickoff.databinding.ItemMovieBinding
import bapspatil.flickoff.model.Movie
import bapspatil.flickoff.ui.details.DetailsActivity
import bapspatil.flickoff.ui.base.BaseViewHolder

class MovieRecyclerViewAdapter(private val mMoviesArrayList: ArrayList<Movie>?) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        val itemMovieBinding = ItemMovieBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return MovieViewHolder(itemMovieBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return mMoviesArrayList?.size ?: 0
    }

    inner class MovieViewHolder(itemMovieBinding: ItemMovieBinding) : BaseViewHolder(itemMovieBinding.root), MovieItemViewModel.MovieItemViewModelListener {
        private var mItemMovieBinding: ItemMovieBinding = itemMovieBinding
        private lateinit var mMovieItemViewModel: MovieItemViewModel

        override fun onItemClick(movie: Movie, imageView: ImageView) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(mItemMovieBinding.root.context as AppCompatActivity, imageView, "posterTransition")
            val intent = Intent(mItemMovieBinding.root.context, DetailsActivity::class.java)
            intent.putExtra("movie", movie)
            startActivity(mItemMovieBinding.root.context, intent, options.toBundle())
        }

        override fun onBind(position: Int) {
            val theMovie = mMoviesArrayList!![position]
            mMovieItemViewModel = MovieItemViewModel(theMovie, this)
            mItemMovieBinding.viewModel = mMovieItemViewModel
            mItemMovieBinding.executePendingBindings()
        }
    }

    fun clearItems() {
        mMoviesArrayList?.clear()
    }

    fun addItems(moviesList: ArrayList<Movie>) {
        mMoviesArrayList?.addAll(moviesList)
        notifyDataSetChanged()
    }
}
