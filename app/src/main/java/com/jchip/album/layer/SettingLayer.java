package com.jchip.album.layer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;

import com.jchip.album.ActivityFrame;
import com.jchip.album.R;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.SettingData;
import com.jchip.album.view.PhotoView;
import com.jchip.album.view.PhotoViewConfig;

public class SettingLayer extends DataLayer {
    private String[] periodNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(PhotoViewConfig.LAYER_ALBUM_SETTING, R.layout.album_setting_layer);
        super.setStatusBarColor(android.R.color.transparent);

        Intent intent = this.getIntent();
        this.setResult(RESULT_OK, intent);
    }

    @Override
    public void initContentView() {
        this.photo = new PhotoView(this, new PhotoData(), this.layer);
        this.periodNames = getResources().getStringArray(R.array.setting_slide_period);

        this.settingData = this.querySetting();
        if (!this.settingData.isSaved()) {
            int backgroundColor = this.getResources().getColor(R.color.background);
            this.settingData.setBackgroundColor(backgroundColor);
            this.settingData.setSlideSpeed(PhotoViewConfig.DEFAULT_SLIDESHOW_SPEED);
            this.settingData.setFrameIndex(PhotoViewConfig.DEFAULT_FRAME_ID);
        }
        this.getIntent().putExtra(SettingData.tableName, this.settingData);

        this.resetSetting();

        SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    onSeekBarChange();
                }
            }
        };

        this.getSeekView(R.id.slideshow_speed).setOnSeekBarChangeListener(seekListener);

        this.getSeekView(R.id.background_color_r).setOnSeekBarChangeListener(seekListener);
        this.getSeekView(R.id.background_color_g).setOnSeekBarChangeListener(seekListener);
        this.getSeekView(R.id.background_color_b).setOnSeekBarChangeListener(seekListener);

        this.getView(R.id.setting_frame_view).setOnClickListener((v) -> this.onFrameSelect());
        this.getView(R.id.setting_reset).setOnClickListener((v) -> this.onResetSetting());
        this.getView(R.id.album_setting_view).setOnClickListener((v) -> this.finish());
    }

    private void onSeekBarChange() {
        int slideSpeed = this.getSeekView(R.id.slideshow_speed).getProgress();
        this.settingData.setSlideSpeed(slideSpeed);
        this.getTextView(R.id.slideshow_speed_text).setText(this.periodNames[this.settingData.getSlideSpeed()]);

        int a = 255;
        int r = this.getSeekView(R.id.background_color_r).getProgress();
        int g = this.getSeekView(R.id.background_color_g).getProgress();
        int b = this.getSeekView(R.id.background_color_b).getProgress();
        this.settingData.setBackgroundColor(Color.argb(a, r, g, b));
        this.getView(R.id.background_color_text).setBackgroundColor(this.settingData.getBackgroundColor());

        this.saveSetting();
    }

    private void onFrameSelect() {
        this.startActivity(ActivityFrame.class, this::onFrameChange);
    }

    private void onFrameChange(Intent intent) {
        PhotoData photoData = (PhotoData) intent.getSerializableExtra(PhotoData.tableName);
        if (photoData != null) {
            this.settingData.setFrameIndex(photoData.getFrameIndex());
            this.photo.setPhotoFrame(photoData.getFrameIndex());
            this.setPhotoView(this.getView(R.id.photo_view));

            this.saveSetting();
        }
    }

    private void onResetSetting() {
        int backgroundColor = this.getResources().getColor(R.color.background);
        this.settingData.setBackgroundColor(backgroundColor);
        this.settingData.setSlideSpeed(PhotoViewConfig.DEFAULT_SLIDESHOW_SPEED);
        this.settingData.setFrameIndex(PhotoViewConfig.DEFAULT_FRAME_ID);

        this.resetSetting();

        this.saveSetting();
    }

    private void resetSetting() {
        this.photo.setPhotoFrame(this.settingData.getFrameIndex());
        this.setPhotoView(this.getView(R.id.photo_view));

        this.getTextView(R.id.slideshow_speed_text).setText(this.periodNames[this.settingData.getSlideSpeed()]);
        this.getView(R.id.background_color_text).setBackgroundColor(this.settingData.getBackgroundColor());

        this.getSeekView(R.id.slideshow_speed).setMax(this.periodNames.length - 1);
        this.getSeekView(R.id.slideshow_speed).setProgress(this.settingData.getSlideSpeed());

        this.getSeekView(R.id.background_color_r).setProgress((this.settingData.getBackgroundColor() >> 16) & 0xFF);
        this.getSeekView(R.id.background_color_g).setProgress((this.settingData.getBackgroundColor() >> 8) & 0xFF);
        this.getSeekView(R.id.background_color_b).setProgress((this.settingData.getBackgroundColor()) & 0xFF);
    }
}