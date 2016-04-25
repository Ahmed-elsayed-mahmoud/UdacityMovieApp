package app.movie.com.movieapp.movies;

import android.content.Context;
import android.net.Uri;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.movie.com.movieapp.R;

class MoviesServerConnection {

    private static final int DEFAULT_SERVER_PAGE_SIZE = 20;
    private final String LOG_TAG = MoviesServerConnection.class.getSimpleName();
    private final String apiKey;
    private final String serverUrl;


    public MoviesServerConnection(Context context) {
        this.apiKey = context.getString(R.string.server_api_key);
        this.serverUrl = context.getString(R.string.server_base_url);
    }

    public String getData() throws IOException {

        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter("sort_by", "popularity.desc")
                .appendQueryParameter("api_key", this.apiKey).build();
        return connectAndFetch(uri);

    }

    public String getPage(int page, int sortCriteria) throws IOException {

        Uri uri = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter("sort_by", sortCriteria == 0 ? "popularity.desc" : "vote_average.desc")
                .appendQueryParameter("api_key", this.apiKey)
                .appendQueryParameter("page", String.valueOf(page))
                .build();
        return connectAndFetch(uri);
    }

    private String connectAndFetch(Uri uri) throws IOException {
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

    public List<MovieDetails> getMovies(int page, int pagesSize, int sortCriteria) throws IOException, JSONException {

        int numberOfServerPagesPerResult = pagesSize / DEFAULT_SERVER_PAGE_SIZE;
        int firstRequiredPage = (page - 1) * numberOfServerPagesPerResult + 1;
        int lastRequiredPage = page * numberOfServerPagesPerResult;

        List<MovieDetails> movies = new ArrayList<>();
        for (int i = firstRequiredPage; i <= lastRequiredPage; i++) {
            String pageData = getPage(i, sortCriteria);
            movies.addAll(new JSONParser(pageData).getMovies());
        }
        return movies;
    }
}
