package me.andrewwelton.iwannaseethat;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import me.andrewwelton.iwannaseethat.network.VolleySingleton;


public class MovieSelected extends ActionBarActivity {

    private Toolbar toolbar;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    private Movie selectedMovie;

    // TODO:: Make a snackbar
    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_selected);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setTabsFromPagerAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        selectedMovie = data.getParcelable("selected");

        collapsingToolbarLayout.setTitle(selectedMovie.getTitle());

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
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }


    }

    public Movie getSelectedMovie() {
        return selectedMovie;
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
        } else if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MovieFragment extends Fragment {
        public static final String ARG_PAGE = "arg_page";

        public MovieFragment() {

        }

        public static MovieFragment newInstance(int pageNumber) {
            MovieFragment fragment = new MovieFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber + 1);
            fragment.setArguments(arguments);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            MovieRecyclerAdapter adapter =  new MovieRecyclerAdapter(getActivity());
            //TODO:: Fragment needs selectedMovie, use this object to populate whatever info I want to show to the user.
            // adapter.setSelectedMovie();
            int pageNumber = arguments.getInt(ARG_PAGE);
            RecyclerView recyclerView = new RecyclerView(getActivity());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return recyclerView;
        }
    }
}

class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        MovieSelected.MovieFragment fragment = MovieSelected.MovieFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab " + (position + 1);
    }
}

class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieRecyclerViewHolder> {
    private LayoutInflater inflater;
    private Movie selectedMovie;

    public void setSelectedMovie(Movie movie) {
        selectedMovie = movie;
    }

    public MovieRecyclerAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MovieRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.custom_row, parent, false);
        MovieRecyclerViewHolder holder = new MovieRecyclerViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieRecyclerViewHolder holder, int position) {
        holder.textView.setText("I Will be an overview!");
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class MovieRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MovieRecyclerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_superhero);
        }
    }
}
