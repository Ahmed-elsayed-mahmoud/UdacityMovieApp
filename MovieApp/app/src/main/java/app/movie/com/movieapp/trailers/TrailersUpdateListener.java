package app.movie.com.movieapp.trailers;

import java.util.Map;


public interface TrailersUpdateListener {
    void trailersSetUpdated(Map<String, String> trailers);
}
