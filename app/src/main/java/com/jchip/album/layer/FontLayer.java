package com.jchip.album.layer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.SeekBar;

import com.jchip.album.R;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.data.PhotoData;
import com.jchip.album.view.PhotoView;
import com.jchip.album.view.PhotoViewConfig;

import java.util.List;

public class FontLayer extends AbstractLayer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(PhotoViewConfig.LAYER_FONT_SETTING, R.layout.album_font_layer);
        super.setStatusBarColor(android.R.color.transparent);
    }

    @Override
    public void initContentView() {
        Intent intent = this.getIntent();
        this.setResult(RESULT_OK, intent);

        PhotoData photoData = (PhotoData) intent.getSerializableExtra(PhotoData.tableName);
        this.photo = new PhotoView(this, photoData, this.layer);

        this.setPhotoView(this.getView(R.id.photo_view));

        this.getTextView(R.id.photo_text).setText(this.photo.getFontText());
        this.getSeekView(R.id.font_color_a).setProgress((this.photo.getFontColor() >> 24) & 0xFF);
        this.getSeekView(R.id.font_color_r).setProgress((this.photo.getFontColor() >> 16) & 0xFF);
        this.getSeekView(R.id.font_color_g).setProgress((this.photo.getFontColor() >> 8) & 0xFF);
        this.getSeekView(R.id.font_color_b).setProgress((this.photo.getFontColor()) & 0xFF);

        // events
        this.getButtonView(R.id.font_type).setOnClickListener((v) -> this.onFontTypeChange());
        this.getButtonView(R.id.font_location).setOnClickListener((v) -> this.onLocationChange());
        this.getButtonView(R.id.font_size_plus).setOnClickListener((v) -> this.onSizeChange(+1));
        this.getButtonView(R.id.font_size_minus).setOnClickListener((v) -> this.onSizeChange(-1));

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextChange(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        this.getEditView(R.id.photo_text).addTextChangedListener(textWatcher);

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
                    onColorChange();
                }
            }
        };
        this.getSeekView(R.id.font_color_r).setOnSeekBarChangeListener(seekListener);
        this.getSeekView(R.id.font_color_g).setOnSeekBarChangeListener(seekListener);
        this.getSeekView(R.id.font_color_b).setOnSeekBarChangeListener(seekListener);
        this.getSeekView(R.id.font_color_a).setOnSeekBarChangeListener(seekListener);

        this.getView(R.id.font_setting_view).setOnClickListener((v) -> this.finish());
    }

    private void onFontTypeChange() {
        List<Integer> fonts = PhotoViewConfig.fonts;
        int fontIndex = (this.photo.getFontIndex() + 1) % fonts.size();
        this.photo.setPhotoFont(fonts.get(fontIndex), -1, -1, -1, null);
        this.setPhotoFont(this.getTextView(R.id.photo_label));
    }

    private void onLocationChange() {
        List<Integer> locations = PhotoViewConfig.locations;
        int locationIndex = (this.photo.getLocationIndex() + 1) % locations.size();
        this.photo.setPhotoFont(-1, -1, -1, locations.get(locationIndex), null);
        this.setPhotoFont(this.getTextView(R.id.photo_label));
    }

    private void onSizeChange(int change) {
        float fontSize = this.photo.getFontSize() + change * PhotoHelper.dpToPx(2);
        if (fontSize > PhotoHelper.dpToPx(10) && fontSize < PhotoHelper.dpToPx(100)) {
            this.photo.setPhotoFont(-1, (int) fontSize, -1, -1, null);
            this.setPhotoFont(this.getTextView(R.id.photo_label));
        }
    }

    private void onColorChange() {
        int a = this.getSeekView(R.id.font_color_a).getProgress();
        int r = this.getSeekView(R.id.font_color_r).getProgress();
        int g = this.getSeekView(R.id.font_color_g).getProgress();
        int b = this.getSeekView(R.id.font_color_b).getProgress();
        this.photo.setPhotoFont(-1, -1, Color.argb(a, r, g, b), -1, null);
        this.setPhotoFont(this.getTextView(R.id.photo_label));
    }

    private void onTextChange(String text) {
        this.photo.setPhotoFont(-1, -1, -1, -1, text);
        this.setPhotoFont(this.getTextView(R.id.photo_label));
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}