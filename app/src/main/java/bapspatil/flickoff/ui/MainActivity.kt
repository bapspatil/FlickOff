package bapspatil.flickoff.ui

import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.transition.Slide
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import bapspatil.flickoff.BuildConfig
import bapspatil.flickoff.R
import bapspatil.flickoff.adapters.MovieRecyclerViewAdapter
import bapspatil.flickoff.model.Movie
import bapspatil.flickoff.model.TMDBResponse
import bapspatil.flickoff.network.RetrofitAPI
import bapspatil.flickoff.utils.NetworkUtils
import com.miguelcatalan.materialsearchview.MaterialSearchView
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), MovieRecyclerViewAdapter.ItemClickListener {

    companion object {
        private val SEARCH_TASK = 0
        private val POPULAR_TASK = 1
        private val TOP_RATED_TASK = 2
        private val UPCOMING_TASK = 3
        private val NOW_PLAYING_TASK = 4
    }

    private var mAdapter: MovieRecyclerViewAdapter? = null
    private val movieArray = ArrayList<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.exitTransition = Slide(Gravity.LEFT)
        }

        mainToolbar!!.setLogo(R.mipmap.ic_launcher)
        setSupportActionBar(mainToolbar)

        longToast("App developed by Bapusaheb Patil")

        val columns = 2
        val layoutManager = GridLayoutManager(applicationContext, columns)
        moviesRecyclerView!!.layoutManager = layoutManager

        mAdapter = MovieRecyclerViewAdapter(applicationContext, movieArray, this)
        fetchMovies(POPULAR_TASK, null)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_popular -> {
                    moviesRecyclerView!!.smoothScrollToPosition(0)
                    fetchMovies(POPULAR_TASK, null)
                }
                R.id.action_rated -> {
                    moviesRecyclerView!!.smoothScrollToPosition(0)
                    fetchMovies(TOP_RATED_TASK, null)
                }
                R.id.action_upcoming -> {
                    moviesRecyclerView!!.smoothScrollToPosition(0)
                    fetchMovies(UPCOMING_TASK, null)
                }
                R.id.action_now -> {
                    moviesRecyclerView!!.smoothScrollToPosition(0)
                    fetchMovies(NOW_PLAYING_TASK, null)
                }
            }
            true
        }

        searchView!!.setCursorDrawable(R.drawable.cursor_search)
        searchView!!.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                moviesRecyclerView!!.smoothScrollToPosition(0)
                fetchMovies(SEARCH_TASK, query)
                searchView!!.closeSearch()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView!!.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                bottomNavigation.visibility = View.INVISIBLE
            }

            override fun onSearchViewClosed() {
                bottomNavigation.visibility = View.VISIBLE
            }
        })

        moviesRecyclerView!!.adapter = ScaleInAnimationAdapter(mAdapter)

    }

    override fun onItemClick(position: Int, posterImageView: ImageView) {
        val movie: Movie = movieArray[position]

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, posterImageView, "posterTransition")
        startActivity(intentFor<DetailsActivity>("movie" to movie), options.toBundle())
    }

    private fun fetchMovies(taskId: Int, taskQuery: String?) {
        moviesRecyclerView!!.visibility = View.INVISIBLE
        progressBar!!.visibility = View.VISIBLE
        val retrofitAPI = NetworkUtils.getCacheEnabledRetrofit(applicationContext).create(RetrofitAPI::class.java)
        val call: Call<TMDBResponse>
        call = when (taskId) {
            SEARCH_TASK -> retrofitAPI.searchMovies(BuildConfig.TMDB_API_TOKEN, "en-US", 1, taskQuery!!)
            POPULAR_TASK -> retrofitAPI.getMovies("popular", BuildConfig.TMDB_API_TOKEN, "en-US", 1)
            TOP_RATED_TASK -> retrofitAPI.getMovies("top_rated", BuildConfig.TMDB_API_TOKEN, "en-US", 1)
            UPCOMING_TASK -> retrofitAPI.getMovies("upcoming", BuildConfig.TMDB_API_TOKEN, "en-US", 1)
            NOW_PLAYING_TASK -> retrofitAPI.getMovies("now_playing", BuildConfig.TMDB_API_TOKEN, "en-US", 1)
            else -> retrofitAPI.getMovies("popular", BuildConfig.TMDB_API_TOKEN, "en-US", 1)
        }
        call.enqueue(object : Callback<TMDBResponse> {
            override fun onResponse(call: Call<TMDBResponse>, response: Response<TMDBResponse>) {
                val tmdbResponse: TMDBResponse? = response.body()
                movieArray.clear()
                if (tmdbResponse != null) {
                    movieArray.addAll(tmdbResponse.results)
                    mAdapter!!.notifyDataSetChanged()
                }
                moviesRecyclerView!!.visibility = View.VISIBLE
                progressBar!!.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<TMDBResponse>, t: Throwable) {
                longToast("Couldn't fetch movies! Check your internet connection.")
                moviesRecyclerView!!.visibility = View.INVISIBLE
                progressBar!!.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val item = menu.findItem(R.id.action_search)
        searchView!!.setMenuItem(item)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about_me -> {
                val options = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
                startActivity(intentFor<AboutMeActivity>(), options.toBundle())
                true
            }
            else -> true
        }
    }

    override fun onBackPressed() {
        if (searchView!!.isSearchOpen)
            searchView!!.closeSearch()
        else
            super.onBackPressed()
    }

}
