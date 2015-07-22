package me.andrewwelton.iwannaseethat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Array;
import java.util.ArrayList;

import me.andrewwelton.iwannaseethat.network.VolleySingleton;

/**
 * Created by Andrew on 7/8/2015.
 */
public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolderSearchResults> {

    private LayoutInflater layoutInflater;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    public SearchResultsAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();

    }

    public void setMovieList(ArrayList<Movie> listOfMovies) {
        this.movieList = listOfMovies;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderSearchResults onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.movie_search_result, parent, false);
        ViewHolderSearchResults viewHolder = new ViewHolderSearchResults(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderSearchResults holder, int position) {
        Movie currentMovie = movieList.get(position);
        holder.movieTitle.setText(currentMovie.getTitle());
        holder.releaseDate.setText(currentMovie.getReleaseDate());
        String url = "";
        if(currentMovie.getPosterImage() != "null") {
            url = ApplicationClass.API_BASE_URL + ApplicationClass.API_POSTER_SIZES[0] + currentMovie.getPosterImage();
        }
        if(url.length() > 0) {
            imageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.moviePoster.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public Movie getMovieAtIndex(int i) { return movieList.get(i); }

    static class ViewHolderSearchResults extends RecyclerView.ViewHolder {
        private ImageView moviePoster;
        private TextView movieTitle;
        private TextView releaseDate;
        public ViewHolderSearchResults(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_thumbnail);
            movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            releaseDate = (TextView) itemView.findViewById(R.id.movie_release_date);
        }
    }
}
