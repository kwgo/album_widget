package com.jchip.album.widget;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jchip.album.ActivityAlbumSetting;
import com.jchip.album.R;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.WidgetData;
import com.jchip.album.view.PhotoView;

import java.util.List;

public class WidgetAlbumSetting extends WidgetSetting {
    protected static final int ALBUM_DENSITY_FACTOR = 16;

    private List<AlbumData> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(PhotoView.WIDGET_ALBUM_SETTING, R.layout.widget_album_setting);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.albums = DataHelper.getInstance(this).queryPhotoAlbum();
        if (this.albums == null || this.albums.isEmpty()) {
            this.startApp();
            this.finish();
        }
        Log.d("", "*** start init album list size = " + this.albums.size());

        this.initListView(R.id.album_setting_view, R.layout.widget_album_setting_item, this.albums.size());
    }

    @Override
    protected void bindItemView(View itemView, final int position) {
        AlbumData albumData = this.albums.get(position);
        TextView albumName = itemView.findViewById(R.id.album_name);
        albumName.setText(albumData.getAlbumName());
        this.photo = albumData.getPhoto(0);
        View photoView = itemView.findViewById(R.id.photo_view);
        this.setPhotoView(photoView);

        itemView.setOnClickListener((view) -> {
            WidgetData widgetData = new WidgetData();
            widgetData.setWidgetId(this.appWidgetId);
            widgetData.setAlbumId(albumData.getAlbumId());
            this.saveWidget(widgetData);
            this.updateWidget(ActivityAlbumSetting.AlbumProvider.class);
            this.finish();
        });
    }
}