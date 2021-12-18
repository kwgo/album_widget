package com.jchip.album.activity;

import android.content.Intent;

import com.jchip.album.ActivityAlbumSetting;
import com.jchip.album.ActivityFont;
import com.jchip.album.ActivityFrame;
import com.jchip.album.ActivityPhotoSetting;
import com.jchip.album.R;
import com.jchip.album.data.PhotoData;

public class LayerActivity extends DataActivity {

    @Override
    public void initContentView() {
        super.initContentView();

        this.getButtonView(R.id.font_setting).setOnClickListener((v) -> this.onFontSetting());
        this.getButtonView(R.id.frame_select).setOnClickListener((v) -> this.onFrameSelect());

        this.getButtonView(R.id.album_help).setOnClickListener((v) -> this.onAlbumHelp());
    }

    public void onAlbumHelp() {
        this.startActivity(ActivityPhotoSetting.class, null);
    }

    public void onFontSetting() {
        this.startActivity(ActivityFont.class, (intent) -> onFontChange(intent));
    }

    public void onFrameSelect() {
        this.startActivity(ActivityFrame.class, (intent) -> onFrameChange(intent));
    }

    public void onFrameChange(Intent intent) {
        int frameSourceId = intent.getIntExtra(FRAME_RESOURCE, -1);
        if (frameSourceId >= 0) {
            this.photo.setFrameIndex(frameSourceId);
            this.setPhotoFrame(this.getView(R.id.photo_container));
            this.setPhotoFrame(this.getView(R.id.photo_frame));
            this.updatePhoto();
        }
    }

    public void onFontChange(Intent intent) {
        PhotoData photo = (PhotoData) intent.getSerializableExtra(PhotoData.tableName);
        if (photo != null) {
            this.photo.setFontType(photo.getFontType());
            this.photo.setFontSize(photo.getFontSize());
            this.photo.setFontColor(photo.getFontColor());
            this.photo.setFontLocation(photo.getFontLocation());
            this.photo.setFontText(photo.getFontText());
            this.setPhotoFont(this.getTextView(R.id.photo_label));
            this.updatePhoto();
        }
    }
}