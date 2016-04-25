package app.movie.com.movieapp;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.movie.com.movieapp.favourites.MovieContract;
import app.movie.com.movieapp.movies.CustomArrayAdapter;
import app.movie.com.movieapp.movies.MoviesUpdateListener;
import app.movie.com.movieapp.movies.FetchMoviesData;
import app.movie.com.movieapp.movies.MovieDetails;


public class MainActivityFragment extends Fragment implements MoviesUpdateListener {

    public static final String KEY_MOVIES = "key_movies";
    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_POSTER = 3;
    public static final int COL_OVERVIEW = 4;
    public static final int COL_RATING = 5;
    public static final int COL_RELEASE_DATE = 6;
    private static final int SORT_POPULAR = 0;
    private static final int SORT_USER = 1;
    private static final int SORT_FAVOURITE = 2;
    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE
    };
    private int sort = 0;
    private CustomArrayAdapter arrayAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.grid_view_cell, new ArrayList<MovieDetails>());

        List<MovieDetails> movies = null;
        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
        }
        if (movies == null) {
            if (isNetworkAvailable()) {
                getActivity().setTitle("Popular Movies");
                FetchMoviesData fetchMoviesData = new FetchMoviesData(getActivity(), this);
                fetchMoviesData.execute();
            } else {
                getActivity().setTitle("My Favourites");
                Toast.makeText(getActivity(), "You are offline, cannot fetch new movies",
                        Toast.LENGTH_LONG).show();
                new FetchFavoriteMoviesTask(getActivity()).execute();
            }
        } else {
            arrayAdapter.updateValues(movies);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridViewLayout = (GridView) view.findViewById(R.id.grid_view_layout);
        gridViewLayout.setAdapter(arrayAdapter);
        gridViewLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDetails movie = arrayAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(movie);
            }
        });
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_MOVIES)) {
                ArrayList<MovieDetails> mMovies = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
                arrayAdapter.updateValues(mMovies);
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_MOVIES, (ArrayList<? extends Parcelable>) arrayAdapter.getElements());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void moviesSetUpdated(List<MovieDetails> movies) {
        arrayAdapter.updateValues(movies);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(!isNetworkAvailable()){
            Toast.makeText(getActivity(), "You are offline, cannot fetch new movies",
                    Toast.LENGTH_LONG).show();
        }
        if (id == R.id.action_sorting_popularity) {
            sort = SORT_POPULAR;
            getActivity().setTitle("Popular Movies");
            FetchMoviesData fetchMoviesData = new FetchMoviesData(getActivity(), this, SORT_POPULAR);
            fetchMoviesData.execute();
        }
        if (id == R.id.action_sorting_userrating) {
            sort = SORT_USER;
            getActivity().setTitle("High rated Movies");
            FetchMoviesData fetchMoviesData = new FetchMoviesData(getActivity(), this, SORT_USER);
            fetchMoviesData.execute();
        } else if (id == R.id.action_refresh) {
            if (sort == SORT_FAVOURITE) {
                getActivity().setTitle("My Favourites");
                new FetchFavoriteMoviesTask(getActivity()).execute();
            } else {
                if (sort == SORT_USER) getActivity().setTitle("High rated Movies");
                else getActivity().setTitle("Popular Movies");
                FetchMoviesData fetchMoviesData = new FetchMoviesData(getActivity(), this, sort);
                fetchMoviesData.execute();
            }
        } else if (id == R.id.action_favorites) {
            sort = SORT_FAVOURITE;
            getActivity().setTitle("My Favourites");
            new FetchFavoriteMoviesTask(getActivity()).execute();
        }
        return true;
    }


    public interface Callback {
        void onItemSelected(MovieDetails movie);
    }

    public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, List<MovieDetails>> {

        private Context mContext;

        public FetchFavoriteMoviesTask(Context context) {
            mContext = context;
        }

        private List<MovieDetails> getFavoriteMoviesDataFromCursor(Cursor cursor) {
            List<MovieDetails> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MovieDetails movie = new MovieDetails(cursor);
                    results.add(movie);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return results;
        }

        @Override
        protected List<MovieDetails> doInBackground(Void... params) {
            Cursor cursor = mContext.getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
            return getFavoriteMoviesDataFromCursor(cursor);
        }

        @Override
        protected void onPostExecute(List<MovieDetails> movies) {
            if (movies != null) {
                if (arrayAdapter != null) {
                    arrayAdapter.updateValues(movies);
                }
            }
        }
    }
}
