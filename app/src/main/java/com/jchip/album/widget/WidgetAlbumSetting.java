package com.jchip.album.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jchip.album.ActivityAlbumSetting;
import com.jchip.album.R;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.WidgetData;

import java.util.List;

public class WidgetAlbumSetting extends WidgetSetting {
    private static final int DENSITY_FACTOR = 8;
    private List<AlbumData> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.widget_album_setting);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.albums = DataHelper.getInstance(this).queryPhotoAlbum();

        this.initRecyclerView(R.id.album_setting_view, R.layout.widget_album_setting_item, this.albums.size());
    }

    @Override
    protected void bindItemView(View itemView, int position) {
        AlbumData albumData = this.albums.get(position);
        TextView albumName = itemView.findViewById(R.id.album_name);
        albumName.setText(albumData.getAlbumName());
        PhotoData photoData = albumData.getPhoto(0);
        View photoView = itemView.findViewById(R.id.photo_view);
        PhotoHelper.setPhotoView(this, photoView, photoData, DENSITY_FACTOR, true, false, false);

        itemView.setOnClickListener((view) -> {
            WidgetData widgetData = new WidgetData();
            widgetData.setWidgetId(appWidgetId);
            widgetData.setAlbumId(albumData.getAlbumId());
            this.saveWidget(widgetData);
            this.updateWidget(ActivityAlbumSetting.AlbumProvider.class);
            finish();
        });
    }
}