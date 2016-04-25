package app.movie.com.movieapp.movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


class JSONParser {
    public static final String ID = "id";
    private static final String TOTAL_PAGES_KEY = "total_pages";    // Total Number Of Pages
    private static final String TOTAL_RESULTS_KEY = "total_results";// Total Number Of Results
    private static final String RESULTS_KEY = "results";    // Number Of Results In Current Page
    private static final String PAGE_NUMBER_KEY = "page";   // Current Page Number
    private static final String ORIGINAL_TITLE_KEY = "original_title";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String OVERVIEW_KEY = "overview";
    private static final String POSTER_PATH_KEY = "poster_path";
    private JSONObject jsonObject;

    public JSONParser(String data) throws JSONException {

        this.jsonObject = new JSONObject(data);
    }

    public int getCurrentPageNumber() throws JSONException {

        return jsonObject.getInt(PAGE_NUMBER_KEY);
    }


    public int getTotalNumberOfPages() throws JSONException {

        return jsonObject.getInt(TOTAL_PAGES_KEY);
    }

    public int getTotalNumberOfResults() throws JSONException {

        return jsonObject.getInt(TOTAL_RESULTS_KEY);
    }

    public int getNumberOfResultsInCurrentPage() throws JSONException {
        JSONArray results = jsonObject.getJSONArray(RESULTS_KEY);
        return results.length();
    }

    public MovieDetails getMovie(int id) throws JSONException {
        JSONArray results = jsonObject.getJSONArray(RESULTS_KEY);
        JSONObject movieJsonObject = results.getJSONObject(id);
        String title = movieJsonObject.getString(ORIGINAL_TITLE_KEY);
        double voteAverage = movieJsonObject.getDouble(VOTE_AVERAGE_KEY);
        String releaseDate = movieJsonObject.getString(RELEASE_DATE_KEY);
        String overview = movieJsonObject.getString(OVERVIEW_KEY);
        String posterPath = movieJsonObject.getString(POSTER_PATH_KEY);
        int idd = movieJsonObject.getInt(ID);
        return new MovieDetails(title, voteAverage, releaseDate, overview, posterPath, idd);
    }

    public List<MovieDetails> getMovies() throws JSONException {
        List<MovieDetails> movies = new ArrayList<>();
        JSONArray results = jsonObject.getJSONArray(RESULTS_KEY);
        for (int i = 0; i < results.length(); i++) {
            MovieDetails movie = getMovie(i);
            movies.add(movie);
        }
        return movies;
    }
}
