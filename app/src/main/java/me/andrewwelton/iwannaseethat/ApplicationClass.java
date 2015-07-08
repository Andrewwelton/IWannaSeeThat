package me.andrewwelton.iwannaseethat;

import android.app.Application;
import android.content.Context;

/**
 * Created by Andrew on 7/7/2015.
 */
public class ApplicationClass extends Application {

    public static final String API_KEY_TMDB = "ad5580970c5231210a244a38369f5a46";
    public static final String API_MOVIE_SEARCH = "http://api.themoviedb.org/3/search/movie";
    private static ApplicationClass sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static ApplicationClass getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
