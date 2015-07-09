package me.andrewwelton.iwannaseethat;

/**
 * Created by Andrew on 7/7/2015.
 */
public class Movie {
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

    public Movie(String overview, String title, String releaseDate, String posterImage) {
        this.overview = overview;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterImage = posterImage;
    }

    public String toString() {
        return "TITLE: " + this.title + "OVERVIEW: " + this.overview + "RELEASE: " + this.releaseDate + "POSTER: " + this.posterImage;
    }

}
