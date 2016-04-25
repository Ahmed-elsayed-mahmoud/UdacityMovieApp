package app.movie.com.movieapp.reviews;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FetchReviews extends AsyncTask<Integer, Void, Map<String, String>> {

    private static final String LOG_TAG = FetchReviews.class.getSimpleName();
    private Activity mActivity;
    private ReviewsUpdateListener listener;


    public FetchReviews(Activity activity, ReviewsUpdateListener listener) {
        mActivity = activity;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Map<String, String> doInBackground(Integer... params) {
        MovieReviewsServerConnector connector = new MovieReviewsServerConnector(mActivity, params[0]);
        Map<String, String> trailers;
        try {
            Log.d(LOG_TAG, "Before get Reviews");
            trailers = connector.getReviews();
            Log.d(LOG_TAG, "Before get Reviews");
        } catch (IOException | JSONException e) {
            Log.e("", "Error occurred while parsing Reviews data...: " + e.toString());
            return new HashMap<>();
        }
        return trailers;
    }

    @Override
    protected void onPostExecute(Map<String, String> trailers) {
        listener.reviewsSetUpdated(trailers, 4);

    }
}
