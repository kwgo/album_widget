package com.jchip.album.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jchip.album.ActivityPhotoSetting;
import com.jchip.album.R;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.WidgetData;

import java.util.ArrayList;
import java.util.List;

public class WidgetPhotoSetting extends WidgetSetting {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.widget_photo_setting);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        ListView settingView = (ListView) findViewById(R.id.photo_setting_view);
        ListViewAdapter listViewAdapter = new ListViewAdapter(this);
        settingView.setAdapter(listViewAdapter);
    }

    public class ListViewAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        private List<AlbumData> albums;
        private static final int PHOTO_NUMBER = 2;

        public ListViewAdapter(Context context) {
            this.context = context;
            this.inflater = (LayoutInflater.from(context));

            this.albums = new ArrayList<>();
            for (AlbumData albumData : DataHelper.getInstance(context).queryAlbumPhotos()) {
                AlbumData album = new AlbumData(albumData.getAlbumName());
                album.setAlbumId(albumData.getAlbumId());
                this.albums.add(album);

                AlbumData photoAlbum = new AlbumData();
                for (int index = 0; index < albumData.getPhotoSize(); index++) {
                    if (index % PHOTO_NUMBER == 0) {
                        photoAlbum = new AlbumData();
                        this.albums.add(photoAlbum);
                    }
                    photoAlbum.addPhoto(albumData.getPhoto(index));
                }
            }
        }

        @Override
        public int getCount() {
            return this.albums.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(R.layout.widget_photo_setting_item, null);
                AlbumData albumData = this.albums.get(position);
                if (albumData.isSaved()) {
                    TextView albumName = view.findViewById(R.id.album_name);
                    albumName.setText(albumData.getAlbumName());
                } else {
                    for (int index = 0; index < albumData.getPhotoSize(); index++) {
                        PhotoData photoData = albumData.getPhoto(index);
                        View photoView = view.findViewById(index % PHOTO_NUMBER == 0 ? R.id.photo_left_view : R.id.photo_right_view);
                        PhotoHelper.setPhotoView(context, photoView, photoData, true, false);

                        photoView.setOnClickListener((v) -> {
                            WidgetData widgetData = new WidgetData();
                            widgetData.setWidgetId(appWidgetId);
                            widgetData.setAlbumId(photoData.getAlbumId());
                            widgetData.setPhotoId(photoData.getPhotoId());
                            saveWidget(widgetData);
                            updateWidget(ActivityPhotoSetting.PhotoProvider.class);
                            finish();
                        });
                    }
                }
                view.findViewById(R.id.album_name).setVisibility(albumData.isSaved() ? View.VISIBLE : View.GONE);
                view.findViewById(R.id.photos_view).setVisibility(albumData.isSaved() ? View.GONE : View.VISIBLE);
            }
            return view;
        }
    }
}