package bapspatil.flickoff.network;

import bapspatil.flickoff.model.TMDBResponse;
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

    public static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    @GET("movie/{type}")
    Call<TMDBResponse> getMovies(@Path("type") String TYPE, @Query("api_key") String API_KEY, @Query("language") String LANGUAGE, @Query("page") int PAGE);

    @GET("search/movie")
    Call<TMDBResponse> searchMovies(@Query("api_key") String API_KEY, @Query("language") String LANGUAGE, @Query("page") int PAGE, @Query("query") String QUERY);

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
