package com.jchip.album.layer;

import android.content.Intent;
import android.util.Log;

import com.jchip.album.ActivityAbout;
import com.jchip.album.ActivityFont;
import com.jchip.album.ActivityFrame;
import com.jchip.album.R;
import com.jchip.album.data.PhotoData;
import com.jchip.album.view.PhotoView;

public abstract class FlowLayer extends DataLayer {

    @Override
    public void initContentView() {
        super.initContentView();

        this.getButtonView(R.id.font_setting).setOnClickListener((v) -> this.onFontSetting());
        this.getButtonView(R.id.frame_select).setOnClickListener((v) -> this.onFrameSelect());

        this.getButtonView(R.id.album_help).setOnClickListener((v) -> this.onAlbumHelp());
    }

    public void onAlbumHelp() {
        this.startActivity(ActivityAbout.class, null);
    }

    public void onFontSetting() {
        this.startActivity(ActivityFont.class, this::onFontChange);
    }

    public void onFrameSelect() {
        this.startActivity(ActivityFrame.class, this::onFrameChange);
    }

    public void onFrameChange(Intent intent) {
         PhotoData photoData = (PhotoData) intent.getSerializableExtra(PhotoData.tableName);
        if (photoData != null) {
            this.photo.setPhotoView(new PhotoView(this, photoData, this.layer));
            this.updatePhoto();
            this.setPhotoFrame(this.getView(R.id.photo_container), this.getView(R.id.photo_board), this.getView(R.id.photo_frame));
        }
    }

    public void onFontChange(Intent intent) {
        PhotoData photoData = (PhotoData) intent.getSerializableExtra(PhotoData.tableName);
        if (photoData != null) {
            this.photo.setPhotoView(new PhotoView(this, photoData, this.layer));
            this.updatePhoto();
            this.setPhotoFont(this.getTextView(R.id.photo_label));
        }
    }
}