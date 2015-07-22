package me.andrewwelton.iwannaseethat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 7/7/2015.
 */
public class Movie implements Parcelable {
    private String overview;

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String releaseDate;

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    private String posterImage;

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    private String backdropImage;

    public String getBackdropImage() {
        return backdropImage;
    }

    public void setBackdropImage(String backdropImage) {
        this.backdropImage = backdropImage;
    }


    public Movie(String overview, String title, String releaseDate, String posterImage, String backdropImage) {
        this.overview = overview;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterImage = posterImage;
        this.backdropImage = backdropImage;
    }

    public String toString() {
        return "TITLE: " + this.title + "OVERVIEW: " + this.overview + "RELEASE: " + this.releaseDate + "POSTER: " + this.posterImage;
    }

    public Movie(Parcel in) {
        String[] data = new String[5];

        in.readStringArray(data);
        this.overview = data[0];
        this.title = data[1];
        this.releaseDate = data[2];
        this.posterImage = data[3];
        this.backdropImage = data[4];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.overview,
                this.title,
                this.releaseDate,
                this.posterImage,
                this.backdropImage
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
