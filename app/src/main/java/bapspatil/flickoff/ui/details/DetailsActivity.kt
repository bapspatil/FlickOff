package bapspatil.flickoff.ui.details

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import bapspatil.flickoff.BuildConfig
import bapspatil.flickoff.R
import bapspatil.flickoff.adapters.CastRecyclerViewAdapter
import bapspatil.flickoff.adapters.CastRecyclerViewAdapter.OnActorClickHandler
import bapspatil.flickoff.model.Cast
import bapspatil.flickoff.model.Movie
import bapspatil.flickoff.model.TMDBCreditsResponse
import bapspatil.flickoff.network.TmdbApi
import bapspatil.flickoff.utils.GlideApp
import bapspatil.flickoff.utils.NetworkUtils
import com.google.android.material.appbar.AppBarLayout
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.activity_details.*
import org.jetbrains.anko.browse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

// TODO: Extend this to BaseActivity, implement DetailsNavigator
class DetailsActivity : AppCompatActivity() {

    private var mMovie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        window.enterTransition = Slide(Gravity.BOTTOM)
        postponeEnterTransition()

        setSupportActionBar(detailsToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        directorTextView.text = "N/A" // What if the director isn't available? Busy guy...

        val receivedIntent = intent
        if (receivedIntent.hasExtra("movie")) {
            mMovie = receivedIntent.getParcelableExtra("movie")
            ratingTextView!!.text = mMovie?.rating
            if (mMovie?.date != null && mMovie?.date != "")
                dateTextView!!.text = prettifyDate(mMovie?.date.toString())
            titleTextView!!.text = mMovie?.title
            plotTextView!!.text = mMovie?.plot
            GlideApp.with(this)
                    .load(TmdbApi.POSTER_BASE_URL + mMovie?.posterPath)
                    .centerCrop()
                    .into(posterImageView!!)
            GlideApp.with(this)
                    .load(TmdbApi.BACKDROP_BASE_URL + mMovie?.backdropPath)
                    .placeholder(R.drawable.tmdb_placeholder_land)
                    .error(R.drawable.tmdb_placeholder_land)
                    .fallback(R.drawable.tmdb_placeholder_land)
                    .centerCrop()
                    .into(backdropImageView!!)
        }

        collapsingToolbar.title = mMovie?.title
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent))
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.black))
        appBar.addOnOffsetChangedListener(AppBarLayout.BaseOnOffsetChangedListener<AppBarLayout> { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout!!.totalScrollRange == 0)
                posterImageView!!.visibility = View.GONE
            else
                posterImageView!!.visibility = View.VISIBLE
        })

        fetchCredits()

        startPostponedEnterTransition()
    }

    private fun fetchCredits() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        castRecyclerView!!.layoutManager = layoutManager

        val castList = ArrayList<Cast>()
        val mCastAdapter = CastRecyclerViewAdapter(this, castList, object : OnActorClickHandler {
            override fun onActorClicked(actorName: String) {
                try {
                    browse("https://www.google.com/search?q=$actorName movies")
                } catch (e: Exception) {
                    // Who doesn't have Google? Or a browser?
                    e.printStackTrace()
                }
            }
        })
        castRecyclerView!!.adapter = ScaleInAnimationAdapter(mCastAdapter)

        val retrofitAPI = NetworkUtils.getCacheEnabledRetrofit(applicationContext).create(TmdbApi::class.java)
        val creditsCall = retrofitAPI.getCredits(mMovie!!.id, BuildConfig.TMDB_API_TOKEN)
        creditsCall.enqueue(object : Callback<TMDBCreditsResponse> {
            override fun onResponse(call: Call<TMDBCreditsResponse>, response: Response<TMDBCreditsResponse>) {
                val creditsResponse = response.body()

                // Get cast info
                castList.clear()
                if (creditsResponse != null && creditsResponse.cast.size != 0) {
                    castList.addAll(creditsResponse.cast)
                    mCastAdapter.notifyDataSetChanged()
                } else {
                    castLabelTextView.visibility = View.GONE
                    castRecyclerView!!.visibility = View.GONE
                }

                // Get director info
                if (creditsResponse != null) {
                    for (crew in creditsResponse.crew) {
                        if (crew.job == "Director") {
                            directorTextView!!.text = crew.name
                            break
                        }
                    }
                }
            }

            override fun onFailure(call: Call<TMDBCreditsResponse>, t: Throwable) {
                // Why bother doing anything here?
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SimpleDateFormat")
    private fun prettifyDate(jsonDate: String): String {
        val sourceDateFormat = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = null
        try {
            date = sourceDateFormat.parse(jsonDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val destDateFormat = SimpleDateFormat("MMM dd\nyyyy")
        return destDateFormat.format(date)
    }
}
