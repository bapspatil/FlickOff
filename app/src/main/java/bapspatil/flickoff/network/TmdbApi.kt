package bapspatil.flickoff.network

import bapspatil.flickoff.model.TMDBCreditsResponse
import bapspatil.flickoff.model.TMDBResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by bapspatil
 */

interface TmdbApi {

    @GET("movie/{type}")
    fun getMovies(@Path("type") TYPE: String, @Query("api_key") API_KEY: String, @Query("language") LANGUAGE: String, @Query("page") PAGE: Int): Call<TMDBResponse>

    @GET("search/movie")
    fun searchMovies(@Query("api_key") API_KEY: String, @Query("language") LANGUAGE: String, @Query("page") PAGE: Int, @Query("query") QUERY: CharSequence): Call<TMDBResponse>

    @GET("movie/{movie_id}/credits")
    fun getCredits(@Path("movie_id") MOVIE_ID: Int, @Query("api_key") API_KEY: String): Call<TMDBCreditsResponse>

    companion object {

        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"
        const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val BASE_URL = "https://api.themoviedb.org/3/"

        fun create(): TmdbApi = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TmdbApi::class.java)
    }
}
