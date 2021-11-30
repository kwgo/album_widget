package com.jchip.album.view;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.data.AlbumData;

import java.util.List;

public class AlbumViewAdapter extends ArrayAdapter<AlbumData> {

    private Context context;
    private int layoutId;
    private List<AlbumData> albums;

    public AlbumViewAdapter(Context context, int layoutId, List<AlbumData> albums) {
        super(context, layoutId, albums);
        this.context = context;
        this.layoutId = layoutId;
        this.albums = albums;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            /*
             * The convertView argument is essentially a "ScrapView" as described is Lucas post
             * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
             * It will have a non-null value when ListView is asking you recycle the row layout.
             * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
             */
            if (convertView == null) {
                // inflate the layout
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(layoutId, parent, false);
            }
            // object item based on the position
            AlbumData album = albums.get(position);
            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
            textViewItem.setText(album.getAlbumName());
            // in case you want to add some style, you can do something like:
            // textViewItem.setBackgroundColor(Color.CYAN);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}