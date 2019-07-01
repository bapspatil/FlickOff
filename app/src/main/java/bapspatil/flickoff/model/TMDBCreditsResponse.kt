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
data class TMDBCreditsResponse(
    @SerializedName("cast") var cast: ArrayList<Cast>,
    @SerializedName("crew") var crew: ArrayList<Crew>
) :
    Parcelable
