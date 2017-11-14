package bapspatil.flickoff.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    @SerializedName("poster_path") private String posterPath;
    @SerializedName("title") private String title;
    @SerializedName("overview") private String plot;
    @SerializedName("release_date") String date;
    @SerializedName("vote_average") String rating;
    @SerializedName("id") private int id;
    @SerializedName("backdrop_path") private String backdropPath;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel in) {
        this.posterPath = in.readString();
        this.title = in.readString();
        this.plot = in.readString();
        this.date = in.readString();
        this.rating = in.readString();
        this.id = in.readInt();
        this.backdropPath = in.readString();

    }

    public Movie() {

    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterPath);
        dest.writeString(this.title);
        dest.writeString(this.plot);
        dest.writeString(this.date);
        dest.writeString(this.rating);
        dest.writeInt(this.id);
        dest.writeString(this.backdropPath);
    }
}
