package app.movie.com.movieapp.trailers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import app.movie.com.movieapp.R;

public class MovieTrailersServerConnector {

    public static final String RESULTS_kEY = "results";
    private static final String LOG_TAG = MovieTrailersServerConnector.class.getSimpleName();
    private final String apikey;
    private Context context;
    private int mId;


    public MovieTrailersServerConnector(Context context, int id) {
        this.context = context;
        mId = id;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.apikey = sharedPreferences.getString("api-key", context.getString(R.string.server_api_key));
    }

    public String getData() throws IOException {
        String baseUrl = this.context.getString(R.string.trailer_url);
        // TODO: store string constants in resource file(s)
        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendPath(String.valueOf(mId))
                .appendPath("videos")
                .appendQueryParameter("api_key", this.apikey).build();

        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(uri.toString()).openConnection();
        httpURLConnection.connect();

        int responseCode;
        try {
            responseCode = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            responseCode = httpURLConnection.getResponseCode();
        }

        switch (responseCode) {
            case HttpURLConnection.HTTP_OK:
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuilder stringBuilder = new StringBuilder();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }
                return stringBuilder.toString();

            default:
                throw new IllegalStateException("Connection method is not equipped to handle this case");
        }
    }

    public Map<String, String> getTrailers() throws JSONException, IOException {
        Map<String, String> trailers = new HashMap<>();
        final JSONObject jsonObject = new JSONObject(getData());
        JSONArray results = jsonObject.getJSONArray(RESULTS_kEY);
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJsonObject = results.getJSONObject(i);
            if (movieJsonObject.getString("site").equals("YouTube")) {
                trailers.put(movieJsonObject.getString("name"), movieJsonObject.getString("key"));
            } else {
                Log.w(LOG_TAG, "Ignored trailer for unknown website: " + movieJsonObject.getString("site"));
            }
        }
        return trailers;
    }


}
