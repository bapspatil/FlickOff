package bapspatil.flickoff.network;

import org.json.JSONArray;

import java.util.ArrayList;

import bapspatil.flickoff.model.Movie;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by bapspatil
 */

public interface RetrofitAPI {

    @GET("movie/popular")
    Call<ArrayList<Movie>> fetchPopularMovies(@Path("results") JSONArray RESULTS_ARRAY, @Query("api_key") String API_KEY, @Query("language") String LANGUAGE, @Query("page") int PAGE);

    @GET("movie/top_rated")
    Call<ArrayList<Movie>> fetchTopRatedMovies(@Path("results") JSONArray RESULTS_ARRAY, @Query("api_key") String API_KEY, @Query("language") String LANGUAGE, @Query("page") int PAGE);

    @GET("movie/upcoming")
    Call<ArrayList<Movie>> fetchUpcomingMovies(@Path("results") JSONArray RESULTS_ARRAY, @Query("api_key") String API_KEY, @Query("language") String LANGUAGE, @Query("page") int PAGE);

    @GET("movie/now_playing")
    Call<ArrayList<Movie>> fetchNowPlayingMovies(@Path("results") JSONArray RESULTS_ARRAY, @Query("api_key") String API_KEY, @Query("language") String LANGUAGE, @Query("page") int PAGE);

    @GET("search/movie")
    Call<ArrayList<Movie>> searchMovies(@Path("results") JSONArray RESULTS_ARRAY, @Query("api_key") String API_KEY, @Query("language") String LANGUAGE, @Query("page") int PAGE);

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
