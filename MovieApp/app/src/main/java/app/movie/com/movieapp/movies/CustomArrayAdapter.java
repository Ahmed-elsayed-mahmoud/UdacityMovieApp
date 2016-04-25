package app.movie.com.movieapp.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.movie.com.movieapp.R;

public class CustomArrayAdapter extends BaseAdapter {

    private static final String LOG_TAG = CustomArrayAdapter.class.getSimpleName();
    private final Context context;
    private final int resource;
    private final Object mLock = new Object();
    private List<MovieDetails> elements;

    public CustomArrayAdapter(Context context, int resource, List<MovieDetails> elements) {
        this.context = context;
        this.resource = resource;
        this.elements = elements;
    }

    public List<MovieDetails> getElements() {
        return elements;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public MovieDetails getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = (ImageView) LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        }

        String url = getItem(position).getPosterUrl();
        Picasso.with(context).load(url).error(R.drawable.no_poseter_found).into(view);
        return view;
    }


    public void updateValues(List<MovieDetails> elements) {
        synchronized (mLock) {
            this.elements = elements;
        }
        notifyDataSetChanged();
    }
}
