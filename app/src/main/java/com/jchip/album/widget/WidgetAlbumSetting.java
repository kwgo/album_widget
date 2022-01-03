package com.jchip.album.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jchip.album.ActivityAlbumSetting;
import com.jchip.album.R;
import com.jchip.album.data.WidgetData;
import com.jchip.album.view.AlbumView;
import com.jchip.album.view.PhotoViewConfig;

import java.util.List;

public class WidgetAlbumSetting extends WidgetSetting {

    private List<AlbumView> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(PhotoViewConfig.WIDGET_ALBUM_SETTING, R.layout.widget_album_setting);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.albums = this.queryPhotoAlbum();
        if (this.albums == null || this.albums.isEmpty()) {
            this.startApp();
            this.finish();
        }

        this.initRecyclerView(R.id.album_setting_view, R.layout.widget_album_setting_item, this.albums.size());
    }

    @Override
    protected void bindItemView(View itemView, final int position) {
        AlbumView albumView = this.albums.get(position);
        TextView albumName = itemView.findViewById(R.id.album_name);
        albumName.setText(albumView.getAlbumName());
        this.photo = albumView.getPhotoView(0);
        this.photo.setFrameRect(PhotoViewConfig.getImageRect(this.layer));
        View photoView = itemView.findViewById(R.id.photo_view);
        this.setPhotoView(photoView);

        itemView.setOnClickListener((view) -> {
            WidgetData widgetData = new WidgetData();
            widgetData.setWidgetId(this.appWidgetId);
            widgetData.setAlbumId(albumView.getAlbumId());
            this.saveWidget(widgetData);
            this.updateWidget(ActivityAlbumSetting.AlbumProvider.class);
            this.finish();
        });
    }
}