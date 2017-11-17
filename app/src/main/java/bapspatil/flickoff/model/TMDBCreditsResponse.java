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
    @SerializedName("crew") private ArrayList<Crew> crew;

    public ArrayList<Cast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Cast> cast) {
        this.cast = cast;
    }

    public ArrayList<Crew> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<Crew> crew) {
        this.crew = crew;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.cast);
        dest.writeTypedList(this.crew);
    }

    public TMDBCreditsResponse() {
    }

    protected TMDBCreditsResponse(Parcel in) {
        this.cast = in.createTypedArrayList(Cast.CREATOR);
        this.crew = in.createTypedArrayList(Crew.CREATOR);
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
