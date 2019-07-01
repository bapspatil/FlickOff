package bapspatil.flickoff.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Movie(
    @SerializedName("poster_path") var posterPath: String,
    @SerializedName("title") var title: String,
    @SerializedName("overview") var plot: String,
    @SerializedName("release_date") var date: String,
    @SerializedName("vote_average") var rating: String,
    @SerializedName("id") var id: Int,
    @SerializedName("backdrop_path") var backdropPath: String
) :
    Parcelable
