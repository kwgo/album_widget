package com.jchip.album.layer;

import android.content.Intent;

import com.jchip.album.ActivityFont;
import com.jchip.album.ActivityFrame;
import com.jchip.album.ActivitySetting;
import com.jchip.album.R;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.SettingData;
import com.jchip.album.view.PhotoView;
import com.jchip.album.view.PhotoViewHelper;

public abstract class FlowLayer extends DataLayer {

    @Override
    public void initContentView() {
        super.initContentView();

        this.enableOrientation();

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

            this.getView(R.id.album_photo_view).setBackgroundColor(settingData.getBackgroundColor());
            if (this.photo != null && !this.photo.isSaved()) {
                this.photo.setPhotoFrame(settingData.getFrameIndex());
                this.setPhotoView(this.getView(R.id.photo_view));
            }
        }
    }

    @Override
    protected void onOrientationChanged(int orientation) {
        long angle = orientation * -90L;

        this.getView(R.id.photo_left).setRotation(angle);
        this.getView(R.id.photo_right).setRotation(angle);
        this.getView(R.id.photo_delete).setRotation(angle);
        this.getView(R.id.photo_flip).setRotation(angle);
        this.getView(R.id.photo_rotation).setRotation(angle);
        this.getView(R.id.photo_scale).setRotation(angle);

        this.getView(R.id.font_setting).setRotation(angle);
        this.getView(R.id.frame_select).setRotation(angle);
        this.getView(R.id.photo_add).setRotation(angle);
        this.getView(R.id.album_setting).setRotation(angle);

        PhotoViewHelper.rotatePhotoView(this.photo, this.getView(R.id.photo_view), orientation);
    }
}