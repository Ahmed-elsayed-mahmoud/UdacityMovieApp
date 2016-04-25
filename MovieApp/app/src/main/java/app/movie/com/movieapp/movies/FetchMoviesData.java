package app.movie.com.movieapp.movies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.movie.com.movieapp.R;

public class FetchMoviesData extends AsyncTask<Void, Void, List<MovieDetails>> {

    private static final int PAGE_NUMBER_1 = 1;
    private static final int NUMBER_OF_MOVIES_TO_LOAD = 200;
    private Activity mActivity;
    private ProgressDialog pd;
    private MoviesUpdateListener listener;
    private int mSortCriteria = 0;

    public FetchMoviesData(Activity activity, MoviesUpdateListener listener, int sortCriteria) {
        mActivity = activity;
        this.mSortCriteria = sortCriteria;
        this.listener = listener;
    }

    public FetchMoviesData(Activity activity, MoviesUpdateListener listener) {
        mActivity = activity;
        this.mSortCriteria = 0;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(mActivity);
        pd.setTitle(mActivity.getString(R.string.dialog_progress_title));
        pd.setMessage(mActivity.getString(R.string.dialog_progress_message));
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    @Override
    protected List<MovieDetails> doInBackground(Void... params) {
        MoviesServerConnection connector = new MoviesServerConnection(mActivity.getApplicationContext());
        List<MovieDetails> movies;
        try {
            movies = connector.getMovies(PAGE_NUMBER_1, NUMBER_OF_MOVIES_TO_LOAD, mSortCriteria);
        } catch (IOException | JSONException e) {
            Log.e("", "Error occurred while parsing movies data...: " + e.toString());
            return new ArrayList<>();
        }
        return movies;
    }

    @Override
    protected void onPostExecute(List<MovieDetails> movies) {
        listener.moviesSetUpdated(movies);
        if (pd != null) {
            pd.dismiss();
        }
    }
}
