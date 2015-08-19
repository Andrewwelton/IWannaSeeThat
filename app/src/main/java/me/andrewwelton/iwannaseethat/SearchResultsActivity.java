package me.andrewwelton.iwannaseethat;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.otto.Bus;

import org.droidparts.util.Strings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.andrewwelton.iwannaseethat.network.VolleySingleton;


public class SearchResultsActivity extends AppCompatActivity {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private SearchResultSingleton searchResultSingleton;
    private ArrayList<Movie> movieResultList = new ArrayList<>();

    private RecyclerView movieSearchResults;
    private SearchResultsAdapter searchResultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchResultSingleton = SearchResultSingleton.getInstance();
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null) {
           searchResultsAdapter.setMovieList(searchResultSingleton.getSearchResults());
        }

        movieSearchResults = (RecyclerView) findViewById(R.id.movie_results);
        searchResultsAdapter = new SearchResultsAdapter(this);
        movieSearchResults.setAdapter(searchResultsAdapter);
        movieSearchResults.setLayoutManager(new LinearLayoutManager(this));


        movieSearchResults.addOnItemTouchListener(new RecyclerTouchListener(this, movieSearchResults, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie selectedMovie = searchResultsAdapter.getMovieAtIndex(position);
                Intent intent = new Intent(SearchResultsActivity.this, MovieSelected.class);
                intent.putExtra("selected", selectedMovie);
                startActivity(intent);
            }

            @Override
            public void OnLongClick(View view, int position) {

            }
        }));

        handleIntent(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        searchResultsAdapter.setMovieList(searchResultSingleton.getSearchResults());
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(getIntent());
    }

    public static String getRequestURL(String query) {
        String encoded = Strings.urlEncode(query);
        return ApplicationClass.API_MOVIE_SEARCH + "?api_key=" + ApplicationClass.API_KEY_TMDB + "&query=" + encoded;
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            volleySingleton = VolleySingleton.getInstance();
            requestQueue = volleySingleton.getRequestQueue();
            String query = intent.getStringExtra(SearchManager.QUERY);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestURL(query), (String)null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    parseJSONResponse(response);
                    searchResultsAdapter.setMovieList(movieResultList);
                    searchResultSingleton.setSearchResults(movieResultList);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(request);
        }
    }

    private void parseJSONResponse(JSONObject response) {
        if(response == null || response.length() == 0) { return; }
        try {
            if (response.has("results")) {
                JSONArray resultsArray = response.getJSONArray("results");
                for(int i = 0; i < resultsArray.length(); i++) {
                    JSONObject currentResult = resultsArray.getJSONObject(i);
                    String overview = currentResult.getString("overview");
                    String title = currentResult.getString("title");
                    String releaseDate = currentResult.getString("release_date");
                    String imagePath = currentResult.getString("poster_path");
                    String backdropImage = currentResult.getString("backdrop_path");
                    Movie newMovie = new Movie(overview, title, releaseDate, imagePath, backdropImage);
                    movieResultList.add(newMovie);
                }
            }
        } catch (JSONException e) {
            // results no good
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    // Do I care?
                    super.onLongPress(e);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if(child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            gestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);
        public void OnLongClick(View view, int position);
    }

    class MovieSelectedEvent {
        public Movie selectedMovie;
        public MovieSelectedEvent(Movie movie) {
            this.selectedMovie = movie;
        }
    }
}
