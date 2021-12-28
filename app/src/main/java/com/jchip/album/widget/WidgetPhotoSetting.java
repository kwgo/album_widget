package com.jchip.album.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jchip.album.ActivityPhotoSetting;
import com.jchip.album.R;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.WidgetData;

import java.util.ArrayList;
import java.util.List;

public class WidgetPhotoSetting extends WidgetSetting {
    protected static final int PHOTO_DENSITY_FACTOR = 8;

    private static final int PHOTO_NUMBER = 2;

    private List<AlbumData> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.widget_photo_setting);
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

        this.initRecyclerView(R.id.photo_setting_view, R.layout.widget_photo_setting_item, this.albums.size());

    }

    @Override
    protected void bindItemView(View itemView, int position) {
        AlbumData albumData = this.albums.get(position);
        if (albumData.isSaved()) {
            TextView albumName = itemView.findViewById(R.id.album_name);
            albumName.setText(albumData.getAlbumName());
        } else {
            for (int index = 0; index < albumData.getPhotoSize(); index++) {
                PhotoData photoData = albumData.getPhoto(index);
                View photoView = itemView.findViewById(index % PHOTO_NUMBER == 0 ? R.id.photo_left_view : R.id.photo_right_view);
                this.setPhotoView(photoView, photoData, PHOTO_DENSITY_FACTOR);
                photoView.setVisibility(View.VISIBLE);

                photoView.setOnClickListener((view) -> {
                    WidgetData widgetData = new WidgetData();
                    widgetData.setAlbumId(photoData.getAlbumId());
                    widgetData.setPhotoId(photoData.getPhotoId());
                    this.saveWidget(widgetData);
                    this.updateWidget(ActivityPhotoSetting.PhotoProvider.class);
                    this.finish();
                });
            }
        }
        itemView.findViewById(R.id.album_name).setVisibility(albumData.isSaved() ? View.VISIBLE : View.GONE);
        itemView.findViewById(R.id.photos_view).setVisibility(albumData.isSaved() ? View.GONE : View.VISIBLE);
    }
}