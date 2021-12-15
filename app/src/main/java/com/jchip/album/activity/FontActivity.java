package com.jchip.album.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.data.PhotoData;

public class FontActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.font_layer);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        Intent intent = this.getIntent();
        this.setResult(RESULT_OK, intent);

        this.photo = (PhotoData) intent.getSerializableExtra(PhotoData.tableName);
        //this.photo = this.photo == null ? new PhotoData() : this.photo;

        this.setImagePhoto(this.getImageView(R.id.font_image));
        this.setPhotoFont(this.getTextView(R.id.font_label));
        this.setPhotoText(this.getEditView(R.id.font_text));

        this.getSeekView(R.id.font_color_a).setProgress((this.photo.getFontColor() >> 32) & 0xFF);
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
                onTextChange(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        this.getEditView(R.id.font_text).addTextChangedListener(textWatcher);

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
    }

    private void onLocationChange() {
        this.photo.setFontLocation((this.photo.getFontLocation() + 1) % 4);
        this.setFontLocation(this.getTextView(R.id.font_label));
    }

    private void onSizeChange(int change) {
        TextView textView = this.getTextView(R.id.font_label);
        float fontSize = textView.getTextSize() + change * dp2px(2);
        if (fontSize > dp2px(10) && fontSize < dp2px(100)) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
            this.photo.setFontSize((int) fontSize);
        }
    }

    private void onColorChange() {
        int a = this.getSeekView(R.id.font_color_a).getProgress();
        int r = this.getSeekView(R.id.font_color_r).getProgress();
        int g = this.getSeekView(R.id.font_color_g).getProgress();
        int b = this.getSeekView(R.id.font_color_b).getProgress();
        this.getTextView(R.id.font_label).setTextColor(Color.argb(a, r, g, b));
        this.photo.setFontColor(Color.argb(a, r, g, b));
    }

    private void onTextChange(String text) {
        this.getTextView(R.id.font_label).setText(text);
        this.photo.setFontText(text);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}