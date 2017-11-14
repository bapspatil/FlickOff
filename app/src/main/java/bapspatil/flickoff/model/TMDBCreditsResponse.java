package bapspatil.flickoff.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bapspatil
 */

public class TMDBCreditsResponse implements Parcelable {
    @SerializedName("cast") private ArrayList<Cast> cast;

    public ArrayList<Cast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Cast> cast) {
        this.cast = cast;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.cast);
    }

    public TMDBCreditsResponse() {
    }

    protected TMDBCreditsResponse(Parcel in) {
        this.cast = in.createTypedArrayList(Cast.CREATOR);
    }

    public static final Creator<TMDBCreditsResponse> CREATOR = new Creator<TMDBCreditsResponse>() {
        @Override
        public TMDBCreditsResponse createFromParcel(Parcel source) {
            return new TMDBCreditsResponse(source);
        }

        @Override
        public TMDBCreditsResponse[] newArray(int size) {
            return new TMDBCreditsResponse[size];
        }
    };
}
