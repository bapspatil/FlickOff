package bapspatil.flickoff.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by bapspatil
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class TMDBResponse(
        @SerializedName("page") var page: Int,
        @SerializedName("results") var results: ArrayList<Movie>,
        @SerializedName("total_results") var totalResults: Int,
        @SerializedName("total_pages") var totalPages: Int)
    : Parcelable