package bapspatil.flickoff.ui.main

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import bapspatil.flickoff.BuildConfig
import bapspatil.flickoff.model.Movie
import bapspatil.flickoff.model.TMDBResponse
import bapspatil.flickoff.network.RetrofitAPI
import bapspatil.flickoff.ui.base.BaseViewModel
import bapspatil.flickoff.utils.rx.SchedulerProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

class MainViewModel(schedulerProvider: SchedulerProvider) : BaseViewModel<MainNavigator>(schedulerProvider) {

    val moviesObservableArrayList: ObservableArrayList<Movie> = ObservableArrayList()
    var movieListLiveData: MutableLiveData<List<Movie>> = MutableLiveData()

    init {
        fetchMovies(MainActivity.POPULAR_TASK, null)
    }

    fun addMoviesToList(movies: List<Movie>?) {
        moviesObservableArrayList.clear()
        if (movies != null)
            moviesObservableArrayList.addAll(movies)
    }

    fun fetchMovies(taskId: Int?, taskQuery: String?) {
        setIsLoading(true)
        val retrofitAPI = RetrofitAPI.retrofit.create(RetrofitAPI::class.java)
        val call: Call<TMDBResponse>
        call = when (taskId) {
            MainActivity.SEARCH_TASK -> retrofitAPI.searchMovies(BuildConfig.TMDB_API_TOKEN, "en-US", 1, taskQuery!!)
            MainActivity.POPULAR_TASK -> retrofitAPI.getMovies("popular", BuildConfig.TMDB_API_TOKEN, "en-US", 1)
            MainActivity.TOP_RATED_TASK -> retrofitAPI.getMovies("top_rated", BuildConfig.TMDB_API_TOKEN, "en-US", 1)
            MainActivity.UPCOMING_TASK -> retrofitAPI.getMovies("upcoming", BuildConfig.TMDB_API_TOKEN, "en-US", 1)
            MainActivity.NOW_PLAYING_TASK -> retrofitAPI.getMovies("now_playing", BuildConfig.TMDB_API_TOKEN, "en-US", 1)
            else -> retrofitAPI.getMovies("popular", BuildConfig.TMDB_API_TOKEN, "en-US", 1)
        }
        call.enqueue(object : Callback<TMDBResponse> {
            override fun onResponse(call: Call<TMDBResponse>, response: Response<TMDBResponse>) {
                val tmdbResponse: TMDBResponse? = response.body()
                if (tmdbResponse != null) {
                    movieListLiveData.value = tmdbResponse.results
                    setIsLoading(false)
                }
            }

            override fun onFailure(call: Call<TMDBResponse>, t: Throwable) {
                // Handle onFailure here
                setIsLoading(true)
            }
        })
    }
}
