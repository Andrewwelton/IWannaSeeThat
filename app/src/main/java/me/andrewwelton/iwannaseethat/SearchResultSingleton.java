package me.andrewwelton.iwannaseethat;

import java.util.ArrayList;

/**
 * Created by Andrew on 8/18/2015.
 */
public class SearchResultSingleton {
    private static SearchResultSingleton sInstance = null;
    private ArrayList<Movie> searchResults;

    private SearchResultSingleton() {
        searchResults = new ArrayList<>();
    }

    public static SearchResultSingleton getInstance() {
        if(sInstance == null) {
            sInstance = new SearchResultSingleton();
        }
        return sInstance;
    }

    public ArrayList<Movie> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(ArrayList<Movie> newResults) {
        searchResults = newResults;
    }
}
