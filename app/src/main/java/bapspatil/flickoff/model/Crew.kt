package bapspatil.flickoff.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by bapspatil on 11/17/17.
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class Crew(
    @SerializedName("job") var job: String,
    @SerializedName("name") var name: String
) :
    Parcelable
