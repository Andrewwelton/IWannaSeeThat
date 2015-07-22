package me.andrewwelton.iwannaseethat;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;

/**
 * Created by Andrew on 7/7/2015.
 */
public class ApplicationClass extends Application {

    public static final String API_KEY_TMDB = "ad5580970c5231210a244a38369f5a46";
    public static final String API_MOVIE_SEARCH = "http://api.themoviedb.org/3/search/movie";
    public static final String API_BASE_URL = "http://image.tmdb.org/t/p/";

    public static final String[] API_POSTER_SIZES = {"w92", "w154", "w185", "w342", "w500", "w780", "original"};
    private static ApplicationClass sInstance;

    private static Bus appBus;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        appBus = new Bus();
    }

    public static ApplicationClass getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static Bus getAppBus() {
        return appBus;
    }
}
