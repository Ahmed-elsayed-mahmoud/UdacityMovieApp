package app.movie.com.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import app.movie.com.movieapp.favourites.MovieContract;
import app.movie.com.movieapp.movies.MovieDetails;
import app.movie.com.movieapp.reviews.FetchReviews;
import app.movie.com.movieapp.reviews.ReviewsArrayAdapter;
import app.movie.com.movieapp.reviews.ReviewsUpdateListener;
import app.movie.com.movieapp.trailers.FetchTrailers;
import app.movie.com.movieapp.trailers.TrailersArrayAdapter;
import app.movie.com.movieapp.trailers.TrailersUpdateListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment implements TrailersUpdateListener, ReviewsUpdateListener {

    public static final String ARG_MOVIE = "arg_movie";
    private static final String LOG_TAG = DetailsActivityFragment.class.getSimpleName();
    private static final String MOVIE_SHARE_HASHTAG = " #MovieApp";
    private TrailersArrayAdapter trailersAdapter;
    private ReviewsArrayAdapter reviewsAdapter;
    private ListView trailersGridView, reviewsGridView;
    private MovieDetails movie;
    private ShareActionProvider mShareActionProvider;


    public DetailsActivityFragment() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (movie != null) {
            inflater.inflate(R.menu.menu_details, menu);
            Log.d(LOG_TAG, "menu action share");
            MenuItem menuItem = menu.findItem(R.id.action_share);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareIntent());
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }
        }
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String movieData = getMovieData();
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                movieData + MOVIE_SHARE_HASHTAG);
        return shareIntent;
    }

    private String getMovieData() {
        String data = "";
        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable(ARG_MOVIE);
        }
        if (movie != null) {
            data += movie.getOriginalTitle() + "\n\n";
            data += "User Rating:  " + String.valueOf(movie.getUserRating()) + "\n";
            data += "Release Date:  " + movie.getReleaseDate() + "\n";
            data += movie.getPlotSynopsis() + "\n\n";
            if (trailersAdapter != null && trailersAdapter.getCount() != 0)
                data += getFirstTrailer() + "\n\n";

        }
        return data;
    }

    private String getFirstTrailer() {
        Map<String, String> map = trailersAdapter.getElements();
        Map.Entry<String, String> entry = map.entrySet().iterator().next();
        String key = entry.getKey();
        String value = entry.getValue();
        String trailer = key + "\n" + "http://www.youtube.com/watch?v=" + value;
        return trailer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            Log.d(LOG_TAG, "arg paracelable");
            movie = arguments.getParcelable(ARG_MOVIE);
        }
        Log.d(LOG_TAG, "create adapter ");
        trailersAdapter = new TrailersArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, new HashMap<String, String>());
        reviewsAdapter = new ReviewsArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, new HashMap<String, String>());


        if (movie != null) {
            Log.d(LOG_TAG, "Fetch data");
            FetchTrailers fetchTrailers = new FetchTrailers(getActivity(), this);
            fetchTrailers.execute(movie.getId());
            FetchReviews fetchReviews = new FetchReviews(getActivity(), this);
            fetchReviews.execute(movie.getId());
            Log.d(LOG_TAG, "Fetch succ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable(ARG_MOVIE);
        }
        View rootView = null;
        if (movie != null) {
            rootView = inflater.inflate(R.layout.details_fragment, container, false);

            TextView moviePlot = (TextView) rootView.findViewById(R.id.movie_plot);
            moviePlot.setText("null".equals(movie.getPlotSynopsis()) ? "" : movie.getPlotSynopsis());

            TextView movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
            movieTitle.setText(movie.getOriginalTitle());

            TextView rating = (TextView) rootView.findViewById(R.id.movie_rating);
            rating.setText(String.valueOf("Average Rating \n    [ " + movie.getUserRating() + "/10 ]"));

            final RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
            ratingBar.setRating(new Float(movie.getUserRating() / 2));

            TextView releaseDate = (TextView) rootView.findViewById(R.id.movie_release_date);
            releaseDate.setText("Release Date \n [ " + ("null".equals(movie.getReleaseDate()) ? "N/A" : movie.getReleaseDate()) + " ]");

            ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_poster);
            Picasso.with(getActivity()).load(movie.getPosterUrl()).error(R.drawable.no_poseter_found).into(imageView);

            final ImageButton favouriteBtn = (ImageButton) rootView.findViewById(R.id.favorite_button);
            if (isFavorite(getActivity(), movie.getId()) == 1) {
                favouriteBtn.setImageResource(R.drawable.startyellow);
            } else {
                favouriteBtn.setImageResource(R.drawable.startgrey);
            }
            favouriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(LOG_TAG, getActivity().getContentResolver().toString());
                    if (isFavorite(getActivity(), movie.getId()) == 1) {
                        getActivity().getContentResolver().delete(
                                MovieContract.MovieEntry.CONTENT_URI,
                                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                                new String[]{Integer.toString(movie.getId())}
                        );
                        Toast.makeText(getActivity(), "The Movie is deleted from your Favorites",
                                Toast.LENGTH_LONG).show();
                        favouriteBtn.setImageResource(R.drawable.startgrey);

                    } else {
                        ContentValues values = new ContentValues();
                        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
                        values.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPosterUrl());
                        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getPlotSynopsis());
                        values.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getUserRating());
                        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getUserRating());

                        getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                                values);
                        Toast.makeText(getActivity(), "The Movie is added to your Favorites",
                                Toast.LENGTH_LONG).show();
                        favouriteBtn.setImageResource(R.drawable.startyellow);
                    }

                }
            });
            trailersGridView = (ListView) rootView.findViewById(R.id.trailer_list_view);
            trailersGridView.setAdapter(trailersAdapter);
            trailersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailersAdapter.getItem(position))));
                }
            });
            reviewsGridView = (ListView) rootView.findViewById(R.id.review_list_view);
            reviewsGridView.setAdapter(reviewsAdapter);
        }
        return rootView;
    }

    @Override
    public void trailersSetUpdated(Map<String, String> trailers) {
        if (trailers.isEmpty()) {
            trailers.put("", "N/A");
        }
        trailersAdapter.updateValues(trailers);
        setListViewHeightBasedOnChildren(trailersGridView);
        Log.d(LOG_TAG, ">>>>>>>>>>" + trailersAdapter.getCount() + "");
        updateIntentShare();
    }

    @Override
    public void reviewsSetUpdated(Map<String, String> reviews, int h) {
        if (reviews.isEmpty()) {
            reviews.put("", "N/A");
        }
        reviewsAdapter.updateValues(reviews);
        setListViewHeightBasedOnChildren(reviewsGridView);
    }

    public void updateIntentShare() {
        try {
            mShareActionProvider.setShareIntent(createShareIntent());
        } catch (Exception e) {
        }
    }

    public int isFavorite(Context context, int id) {

        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,   // projection
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", // selection
                new String[]{Integer.toString(id)},   // selectionArgs
                null    // sort order
        );
        int numRows = cursor.getCount();
        cursor.close();
        return numRows;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
