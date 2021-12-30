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

public class AlbumNameAdapter extends ArrayAdapter<AlbumView> {

    private Context context;
    private List<AlbumView> albums;

    public AlbumNameAdapter(Context context, List<AlbumView> albums) {
        super(context, R.layout.album_name_item, albums);
        this.context = context;
        this.albums = albums;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            // So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(R.layout.album_name_item, parent, false);
            }
            // object item based on the position
            AlbumView album = albums.get(position);
            TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
            textViewItem.setText(album.getAlbumName());
            // in case you want to add some style, you can do something like:
            // textViewItem.setBackgroundColor(Color.CYAN);
        } catch (NullPointerException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return convertView;
    }
}