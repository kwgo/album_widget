package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.activity.DataActivity;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.WidgetData;

import java.util.List;

public class WidgetAlbumSetting extends WidgetSetting {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.widget_album_setting);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        ListView settingView = (ListView) findViewById(R.id.album_setting_view);
        ListViewAdapter listViewAdapter = new ListViewAdapter(this);
        settingView.setAdapter(listViewAdapter);

        settingView.setOnItemClickListener((adapterView, view, position, id) -> {
            AlbumData albumData = (AlbumData) listViewAdapter.getItem(position);
            WidgetData widgetData = new WidgetData();
            widgetData.setWidgetId(appWidgetId);
            widgetData.setAlbumId(albumData.getAlbumId());
            this.saveWidget(widgetData);
            this.updateWidget(WidgetAlbumProvider.class);
            finish();
        });
    }

    public class ListViewAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        private List<AlbumData> albums;

        public ListViewAdapter(Context context) {
            this.context = context;
            this.inflater = (LayoutInflater.from(context));

            this.albums = ((DataActivity) context).queryPhotoAlbum();
        }

        @Override
        public int getCount() {
            return this.albums.size();
        }

        @Override
        public Object getItem(int position) {
            return this.albums.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(R.layout.widget_album_setting_item, null);
                AlbumData albumData = this.albums.get(position);
                TextView albumName = view.findViewById(R.id.album_name);
                albumName.setText(albumData.getAlbumName());
                PhotoData photoData = albumData.getPhoto(0);
                View photoView = view.findViewById(R.id.photo_view);
                PhotoHelper.setPhotoView(context, photoView, photoData, true, false);
            }
            return view;
        }
    }
}