package app.movie.com.movieapp.reviews;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Map;

public class ReviewsArrayAdapter extends BaseAdapter {

    private final Context context;
    private final int resource;
    private final Object mLock = new Object();
    private Map<String, String> elements;

    public ReviewsArrayAdapter(Context context, int resource, Map<String, String> elements) {
        this.context = context;
        this.resource = resource;
        this.elements = elements;
    }

    public Map<String, String> getElements() {
        return elements;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public String getItem(int position) {
        return (String) elements.values().toArray()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        }

        ((TextView) view.findViewById(android.R.id.text1)).setTextColor(Color.RED);
        ((TextView) view.findViewById(android.R.id.text1)).setText((String) elements.keySet().toArray()[position]);
        ((TextView) view.findViewById(android.R.id.text2)).setTextColor(Color.BLACK);
        ((TextView) view.findViewById(android.R.id.text2)).setText((String) elements.values().toArray()[position]);

        return view;
    }


    public void updateValues(Map<String, String> elements) {
        synchronized (mLock) {
            this.elements = elements;
        }
        notifyDataSetChanged();
    }

}
