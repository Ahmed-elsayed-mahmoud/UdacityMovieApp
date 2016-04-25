package app.movie.com.movieapp.reviews;

import java.util.Map;

public interface ReviewsUpdateListener {
    void reviewsSetUpdated(Map<String, String> trailers, int h);
}
