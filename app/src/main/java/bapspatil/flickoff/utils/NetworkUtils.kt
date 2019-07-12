package bapspatil.flickoff.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import bapspatil.flickoff.network.TmdbApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtils {

    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting)
            isConnected = true
        return isConnected
    }

    fun getCacheEnabledRetrofit(context: Context): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
                .cache(Cache(context.cacheDir, (5 * 1024 * 1024).toLong()))
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (hasNetwork(context)!!)
                        request.newBuilder().header("Cache-Control", "public, max-age=" + 1).build()
                    else
                        request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                    chain.proceed(request)
                }
                .build()

        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(TmdbApi.BASE_URL)
                .build()
    }
}
