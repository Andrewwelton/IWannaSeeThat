package me.andrewwelton.iwannaseethat;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import me.andrewwelton.iwannaseethat.network.VolleySingleton;


public class MovieSelected extends ActionBarActivity {

    private Toolbar toolbar;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    private ValueAnimator colorAnimation;

    private Movie selectedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_selected);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        selectedMovie = data.getParcelable("selected");

        setTitle(selectedMovie.getTitle());

        int colorFrom = getResources().getColor(R.color.primaryColor);
        int colorTo = getResources().getColor(R.color.transparent);
        colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(100);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                toolbar.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });

        String url = "";
        if(selectedMovie.getBackdropImage() != "null") {
            url = ApplicationClass.API_BASE_URL + "w780" + selectedMovie.getBackdropImage();
        }
        Log.e("URL: ", url);
        if(url.length() > 0) {
            imageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Log.e("Bitmap", "Received Successfully");
                    Bitmap temp = response.getBitmap();
                    ImageView backdrop = (ImageView) findViewById(R.id.movie_backdrop);
                    backdrop.setImageBitmap(response.getBitmap());
                    colorAnimation.start();

                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_selected, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
