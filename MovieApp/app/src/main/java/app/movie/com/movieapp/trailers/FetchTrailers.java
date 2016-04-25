package app.movie.com.movieapp.trailers;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FetchTrailers extends AsyncTask<Integer, Void, Map<String, String>> {

    private Activity mActivity;
    private TrailersUpdateListener listener;


    public FetchTrailers(Activity activity, TrailersUpdateListener listener) {
        mActivity = activity;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Map<String, String> doInBackground(Integer... params) {
        MovieTrailersServerConnector connector = new MovieTrailersServerConnector(mActivity, params[0]);
        Map<String, String> trailers;
        try {
            trailers = connector.getTrailers();
        } catch (IOException | JSONException e) {
            Log.e("", "Error occurred while parsing trailers data...: " + e.toString());
            return new HashMap<>();
        }
        return trailers;
    }

    @Override
    protected void onPostExecute(Map<String, String> trailers) {
        listener.trailersSetUpdated(trailers);

    }
}
