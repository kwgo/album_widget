package com.jchip.album.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jchip.album.ActivityPhotoSetting;
import com.jchip.album.R;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.WidgetData;
import com.jchip.album.view.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class WidgetPhotoSetting extends WidgetSetting {
    protected static final int PHOTO_DENSITY_FACTOR = 8;

    private static final int PHOTO_NUMBER = 2;

    private List<AlbumData> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(PhotoView.WIDGET_PHOTO_SETTING, R.layout.widget_photo_setting);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.albums = new ArrayList<>();
        for (AlbumData albumData : DataHelper.getInstance(this).queryAlbumPhotos()) {
            this.albums.add(albumData);

            AlbumData photoAlbum = new AlbumData();
            for (int index = 0; index < albumData.getPhotoSize(); index++) {
                if (index % PHOTO_NUMBER == 0) {
                    this.albums.add(photoAlbum = new AlbumData());
                }
                photoAlbum.addPhoto(albumData.getPhoto(index));
            }
        }
        this.initListView(R.id.photo_setting_view, R.layout.widget_photo_setting_item, this.albums.size());
    }

    @Override
    protected void bindItemView(View itemView, final int position) {
        AlbumData albumData = this.albums.get(position);
        if (albumData.isSaved()) {
            TextView albumName = itemView.findViewById(R.id.album_name);
            albumName.setText(albumData.getAlbumName());
            albumName.setVisibility(View.VISIBLE);
        } else {
            itemView.findViewById(R.id.photo_left_view).setVisibility(View.INVISIBLE);
            itemView.findViewById(R.id.photo_right_view).setVisibility(View.INVISIBLE);
            for (int index = 0; index < albumData.getPhotoSize(); index++) {
                this.photo = albumData.getPhoto(index);
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