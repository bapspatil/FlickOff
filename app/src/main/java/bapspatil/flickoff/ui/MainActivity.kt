package bapspatil.flickoff.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
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
import bapspatil.flickoff.ui.about.AboutActivity
import bapspatil.flickoff.utils.NetworkUtils
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MovieRecyclerViewAdapter.ItemClickListener {

    companion object {
        private val SEARCH_TASK = 0
        private val POPULAR_TASK = 1
        private val TOP_RATED_TASK = 2
        private val UPCOMING_TASK = 3
        private val NOW_PLAYING_TASK = 4
        private val VOICE_RECOGNITION_REQUEST_CODE = 13
    }

    private var mAdapter: MovieRecyclerViewAdapter? = null
    private val movieArray = ArrayList<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.exitTransition = Slide(Gravity.LEFT)
        window.statusBarColor = Color.parseColor("#FFFFFF")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = Color.parseColor("#FFFFFF")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

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

        searchView!!.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String?) {
                moviesRecyclerView!!.smoothScrollToPosition(0)
                fetchMovies(SEARCH_TASK, currentQuery)
                searchView!!.clearQuery()
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
            }
        })
        searchView!!.setOnMenuItemClickListener { item: MenuItem? ->
            when (item?.itemId) {
                R.id.action_about_me -> {
                    val options = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
                    startActivity(intentFor<AboutActivity>(), options.toBundle())
                }
                R.id.action_voice -> {
                    startVoiceRecognition()
                }
            }
        }

        moviesRecyclerView!!.adapter = ScaleInAnimationAdapter(mAdapter)
        moviesRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {// Scrolled up
                    bottomNavigation.visibility = View.GONE
                } else {
                    bottomNavigation.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onItemClick(position: Int, posterImageView: ImageView) {
        val movie: Movie = movieArray[position]

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, posterImageView, "posterTransition")
        startActivity(intentFor<DetailsActivity>("movie" to movie), options.toBundle())
    }

    private fun fetchMovies(taskId: Int, taskQuery: CharSequence?) {
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

        return true
    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_about_me -> {
//                val options = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
//                startActivity(intentFor<AboutActivity>(), options.toBundle())
//                true
//            }
//            else -> true
//        }
//    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...")
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val matches: ArrayList<String>? = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches != null) {
                if(matches.isNotEmpty()) {
                    val query = matches[0]
                    fetchMovies(SEARCH_TASK, query)
                    longToast("Searching for $query...")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (searchView!!.isSearchBarFocused)
            searchView!!.clearQuery()
        else
            super.onBackPressed()
    }

}
