package bapspatil.flickoff.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.transition.Slide
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bapspatil.flickoff.BR
import bapspatil.flickoff.R
import bapspatil.flickoff.databinding.ActivityMainBinding
import bapspatil.flickoff.ui.about.AboutActivity
import bapspatil.flickoff.ui.base.BaseActivity
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import jp.wasabeef.recyclerview.animators.LandingAnimator
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {

    var mMainViewModel: MainViewModel? = null
    @Inject
    lateinit var mAdapter: MovieRecyclerViewAdapter
    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    var mActivityMainBinding: ActivityMainBinding? = null

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun getViewModel(): MainViewModel {
        mMainViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel::class.java)
        return mMainViewModel as MainViewModel
    }

    override fun handleError(throwable: Throwable) {
        // Handle errors here
    }

    @SuppressLint("RtlHardcoded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = viewDataBinding
        mMainViewModel?.setNavigator(this)

        window.exitTransition = Slide(Gravity.LEFT)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        longToast("App developed by Bapusaheb Patil")

        val columns = 2
        val layoutManager = GridLayoutManager(applicationContext, columns)
        mActivityMainBinding?.moviesRecyclerView?.layoutManager = layoutManager
        mActivityMainBinding?.moviesRecyclerView?.itemAnimator = LandingAnimator()
        mActivityMainBinding?.moviesRecyclerView?.adapter = mAdapter

        subscribeToLiveData()

        mActivityMainBinding?.bottomNavigation?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_popular -> {
                    mActivityMainBinding?.moviesRecyclerView?.smoothScrollToPosition(0)
                    mMainViewModel?.fetchMovies(POPULAR_TASK, null)
                }
                R.id.action_rated -> {
                    mActivityMainBinding?.moviesRecyclerView?.smoothScrollToPosition(0)
                    mMainViewModel?.fetchMovies(TOP_RATED_TASK, null)
                }
                R.id.action_upcoming -> {
                    mActivityMainBinding?.moviesRecyclerView?.smoothScrollToPosition(0)
                    mMainViewModel?.fetchMovies(UPCOMING_TASK, null)
                }
                R.id.action_now -> {
                    mActivityMainBinding?.moviesRecyclerView?.smoothScrollToPosition(0)
                    mMainViewModel?.fetchMovies(NOW_PLAYING_TASK, null)
                }
            }
            true
        }

        mActivityMainBinding?.searchView?.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String?) {
                mActivityMainBinding?.moviesRecyclerView?.smoothScrollToPosition(0)
                mMainViewModel?.fetchMovies(SEARCH_TASK, currentQuery)
                mActivityMainBinding?.searchView?.clearQuery()
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
            }
        })
        mActivityMainBinding?.searchView?.setOnMenuItemClickListener { item: MenuItem? ->
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

        mActivityMainBinding?.moviesRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) { // Scrolled up
                    mActivityMainBinding?.bottomNavigation?.visibility = View.GONE
                } else {
                    mActivityMainBinding?.bottomNavigation?.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...")
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val matches: ArrayList<String>? = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches != null) {
                if (matches.isNotEmpty()) {
                    val query = matches[0]
                    mMainViewModel?.fetchMovies(SEARCH_TASK, query)
                    longToast("Searching for $query...")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (mActivityMainBinding?.searchView?.isSearchBarFocused!!)
            mActivityMainBinding?.searchView?.clearQuery()
        else
            super.onBackPressed()
    }

    private fun subscribeToLiveData() {
        mMainViewModel?.movieListLiveData?.observe(this, Observer { movies ->
            mMainViewModel?.addMoviesToList(movies)
        })
    }

    companion object {
        const val SEARCH_TASK = 0
        const val POPULAR_TASK = 1
        const val TOP_RATED_TASK = 2
        const val UPCOMING_TASK = 3
        const val NOW_PLAYING_TASK = 4
        const val VOICE_RECOGNITION_REQUEST_CODE = 13
    }
}
