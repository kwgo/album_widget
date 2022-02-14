package com.jchip.album.layer;

import android.content.Intent;

import com.jchip.album.ActivityAbout;
import com.jchip.album.ActivityFont;
import com.jchip.album.ActivityFrame;
import com.jchip.album.ActivitySetting;
import com.jchip.album.R;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.SettingData;
import com.jchip.album.view.PhotoView;

public abstract class FlowLayer extends DataLayer {

    @Override
    public void initContentView() {
        super.initContentView();

        this.getButtonView(R.id.font_setting).setOnClickListener((v) -> this.onFontSetting());
        this.getButtonView(R.id.frame_select).setOnClickListener((v) -> this.onFrameSelect());

        this.getButtonView(R.id.album_setting).setOnClickListener((v) -> this.onAlbumSetting());
    }

    public void onAlbumSetting() {
        this.startActivity(ActivitySetting.class, this::onSettingChange);
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
            this.setPhotoView(this.getView(R.id.photo_view));
        }
    }

    public void onFontChange(Intent intent) {
        PhotoData photoData = (PhotoData) intent.getSerializableExtra(PhotoData.tableName);
        if (photoData != null) {
            this.photo.setPhotoView(new PhotoView(this, photoData, this.layer));
            this.updatePhoto();
            this.setPhotoView(this.getView(R.id.photo_view));
        }
    }

    public void onSettingChange(Intent intent) {
        SettingData settingData = (SettingData) intent.getSerializableExtra(SettingData.tableName);
        if (settingData != null) {
            this.settingData.setSlideSpeed(settingData.getSlideSpeed());
            this.settingData.setBackgroundColor(settingData.getBackgroundColor());
            this.settingData.setFrameIndex(settingData.getFrameIndex());

            this.getView(R.id.album_setting_view).setBackgroundColor(settingData.getBackgroundColor());
            if (this.photo != null && !this.photo.isSaved()) {
                this.photo.setPhotoFrame(settingData.getFrameIndex());
                this.setPhotoView(this.getView(R.id.photo_view));
            }
        }
    }
}