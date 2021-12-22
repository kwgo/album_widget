package com.jchip.album.activity;

import android.content.Intent;

import com.jchip.album.ActivityAbout;
import com.jchip.album.ActivityFont;
import com.jchip.album.ActivityFrame;
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
        this.startActivity(ActivityAbout.class, null);
    }

    public void onFontSetting() {
        this.startActivity(ActivityFont.class, (intent) -> onFontChange(intent));
    }

    public void onFrameSelect() {
        this.startActivity(ActivityFrame.class, (intent) -> onFrameChange(intent));
    }

    public void onFrameChange(Intent intent) {
        int frameId = intent.getIntExtra(FRAME_RESOURCE, -1);
        int frameLookId = intent.getIntExtra(FRAME_LOOK, -1);
        if (frameId >= 0) {
            this.photo.setFrameIndex(frameId);
            this.photo.setFrameLook(frameLookId);
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
            this.updatePhoto();
            this.setPhotoLabel(this.getTextView(R.id.photo_label));
        }
    }
}