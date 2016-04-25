package app.movie.com.movieapp.trailers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Map;

public class TrailersArrayAdapter extends BaseAdapter {

    private final Context context;
    private final int resource;
    private final Object mLock = new Object();
    private Map<String, String> elements;

    public TrailersArrayAdapter(Context context, int resource, Map<String, String> elements) {
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
        TextView view = (TextView) convertView;
        if (view == null) {
            view = (TextView) LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        }
        view.setTextColor(Color.RED);
        if (!elements.keySet().toArray()[position].equals("")) {
            view.setText("►" + (String) elements.keySet().toArray()[position]);
        } else {
            elements.remove(elements.keySet().toArray()[position]);
        }

        return view;
    }


    public void updateValues(Map<String, String> elements) {
        synchronized (mLock) {
            this.elements = elements;
        }
        notifyDataSetChanged();
    }

}
