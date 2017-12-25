package bapspatil.flickoff.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by bapspatil
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class Cast(
        @SerializedName("name") var name: String,
        @SerializedName("profile_path") var profilePath: String)
    : Parcelable
