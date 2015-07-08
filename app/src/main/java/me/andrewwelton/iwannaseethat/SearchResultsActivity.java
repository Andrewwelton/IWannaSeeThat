package me.andrewwelton.iwannaseethat;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.droidparts.util.Strings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.andrewwelton.iwannaseethat.network.VolleySingleton;


public class SearchResultsActivity extends ActionBarActivity {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleIntent(getIntent());
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
            volleySingleton = VolleySingleton.getsInstance();
            requestQueue = volleySingleton.getRequestQueue();
            String query = intent.getStringExtra(SearchManager.QUERY);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestURL(query), (String)null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    parseJSONResponse(response);
                    // Toast.makeText(ApplicationClass.getAppContext(), response.toString(), Toast.LENGTH_SHORT).show();
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
}
