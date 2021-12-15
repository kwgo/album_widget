package com.jchip.album.activity;

import android.content.Intent;
import android.util.Log;

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
    }

    public void onFontSetting() {
        Log.d("", "call onFontSetting start this =========" + this);
        Log.d("", "call onFontSetting start =========" + this.getTextView(R.id.photo_label));
        this.startActivity(ActivityFont.class, (intent) -> onFontChange(intent));
    }

    public void onFrameSelect() {
        this.startActivity(ActivityFrame.class, (intent) -> onFrameChange(intent));
    }

    public void onFrameChange(Intent intent) {
        int frameSourceId = intent.getIntExtra(FRAME_RESOURCE, -1);
        if (frameSourceId >= 0) {
            this.photo.setFrameIndex(frameSourceId);
            this.setViewFrame(this.getView(R.id.photo_frame_container));
            this.setViewFrame(this.getView(R.id.photo_frame_cover));
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
            this.setViewFont(this.getTextView(R.id.photo_label));
            this.updatePhoto();
            Log.d("", "onFontChange this.photo===" + this.photo.getClass());
            Log.d("", "onFontChange this.photo.getFontText()===" + this.photo.getFontText());
        }
    }
}