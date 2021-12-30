package com.jchip.album.widget;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jchip.album.ActivityPhotoSetting;
import com.jchip.album.R;
import com.jchip.album.data.WidgetData;
import com.jchip.album.view.AlbumView;
import com.jchip.album.view.PhotoViewConfig;

import java.util.ArrayList;
import java.util.List;

public class WidgetPhotoSetting extends WidgetSetting {
    private static final int PHOTO_NUMBER = 2;

    private List<AlbumView> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(PhotoViewConfig.WIDGET_PHOTO_SETTING, R.layout.widget_photo_setting);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.albums = new ArrayList<>();
        for (AlbumView albumView : this.queryAlbumPhotos()) {
            this.albums.add(albumView);
            AlbumView photoAlbumView = new AlbumView(this, this.layer);
            for (int index = 0; index < albumView.getPhotoSize(); index++) {
                if (index % PHOTO_NUMBER == 0) {
                    this.albums.add(photoAlbumView = new AlbumView(this, this.layer));
                }
                photoAlbumView.addPhotoView(albumView.getPhotoView(index));
            }
        }
        if (this.albums == null || this.albums.isEmpty()) {
            this.startApp();
            this.finish();
        }
        this.initListView(R.id.photo_setting_view, R.layout.widget_photo_setting_item, this.albums.size());
    }

    @Override
    protected void bindItemView(View itemView, final int position) {
        Log.d("", "-------------------showing item view position = " + position);
        AlbumView albumView = this.albums.get(position);
        if (albumView.isSaved()) {
            TextView albumName = itemView.findViewById(R.id.album_name);
            albumName.setText(albumView.getAlbumName());
            albumName.setVisibility(View.VISIBLE);
        } else {
            itemView.findViewById(R.id.photo_left_view).setVisibility(View.INVISIBLE);
            itemView.findViewById(R.id.photo_right_view).setVisibility(View.INVISIBLE);
            for (int index = 0; index < albumView.getPhotoSize(); index++) {
                this.photo = albumView.getPhotoView(index);
                View photoView = itemView.findViewById(index % PHOTO_NUMBER == 0 ? R.id.photo_left_view : R.id.photo_right_view);
                this.setPhotoView(photoView);
                photoView.setVisibility(View.VISIBLE);

                final int albumId = this.photo.getAlbumId();
                final int photoId = this.photo.getPhotoId();
                photoView.setOnClickListener((view) -> {
                    WidgetData widgetData = new WidgetData();
                    widgetData.setAlbumId(albumId);
                    widgetData.setPhotoId(photoId);
                    this.saveWidget(widgetData);
                    this.updateWidget(ActivityPhotoSetting.PhotoProvider.class);
                    this.finish();
                });
            }
        }
    }
}