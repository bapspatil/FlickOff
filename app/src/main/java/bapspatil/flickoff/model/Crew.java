package bapspatil.flickoff.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bapspatil on 11/17/17.
 */

public class Crew implements Parcelable {
    @SerializedName("job") private String job;
    @SerializedName("name") private String name;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.job);
        dest.writeString(this.name);
    }

    public Crew() {
    }

    protected Crew(Parcel in) {
        this.job = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Crew> CREATOR = new Creator<Crew>() {
        @Override
        public Crew createFromParcel(Parcel source) {
            return new Crew(source);
        }

        @Override
        public Crew[] newArray(int size) {
            return new Crew[size];
        }
    };
}
