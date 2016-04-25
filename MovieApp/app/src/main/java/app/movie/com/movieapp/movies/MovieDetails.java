package app.movie.com.movieapp.movies;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import app.movie.com.movieapp.MainActivityFragment;

public class MovieDetails implements Parcelable {
    public static final Parcelable.Creator<MovieDetails> CREATOR = new Parcelable.Creator<MovieDetails>() {

        @Override
        public MovieDetails createFromParcel(Parcel parcel) {
            return new MovieDetails(parcel);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }

    };
    private String originalTitle;
    private double userRating;
    private String releaseDate;
    private String plotSynopsis;
    private String posterUrl;
    private int id;

    public MovieDetails(String originalTitle, double userRating, String releaseDate, String plotSynopsis, String posterPath, int id) {
        this.originalTitle = originalTitle;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.plotSynopsis = plotSynopsis;
        this.id = id;
        this.posterUrl = "http://image.tmdb.org/t/p/w185" + posterPath;
    }

    public MovieDetails(Cursor cursor) {
        this.id = cursor.getInt(MainActivityFragment.COL_MOVIE_ID);
        this.originalTitle = cursor.getString(MainActivityFragment.COL_TITLE);
        this.posterUrl = cursor.getString(MainActivityFragment.COL_POSTER);
        this.plotSynopsis = cursor.getString(MainActivityFragment.COL_OVERVIEW);
        this.userRating = cursor.getInt(MainActivityFragment.COL_RATING);
        this.releaseDate = cursor.getString(MainActivityFragment.COL_RELEASE_DATE);
    }

    public MovieDetails(Parcel parcel) {
        this.originalTitle = parcel.readString();
        this.userRating = parcel.readDouble();
        this.releaseDate = parcel.readString();
        this.plotSynopsis = parcel.readString();
        this.posterUrl = parcel.readString();
        this.id = parcel.readInt();
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "originalTitle='" + originalTitle + '\'' +
                ", userRating=" + userRating +
                ", releaseDate='" + releaseDate + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeDouble(userRating);
        parcel.writeString(releaseDate);
        parcel.writeString(plotSynopsis);
        parcel.writeString(posterUrl);
        parcel.writeInt(id);
    }
}
