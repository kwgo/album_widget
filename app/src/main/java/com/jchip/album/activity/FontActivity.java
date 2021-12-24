package com.jchip.album.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.data.PhotoData;

import java.util.Arrays;
import java.util.List;

public class FontActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.album_font_layer);
        super.setStatusBarColor(android.R.color.transparent);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        Intent intent = this.getIntent();
        this.setResult(RESULT_OK, intent);

        this.photo = (PhotoData) intent.getSerializableExtra(PhotoData.tableName);

        this.setPhotoLook(this.getView(R.id.photo_view));

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
        List<Integer> fonts = PhotoHelper.getFonts();
        int fontIndex = PhotoHelper.getFontIndex(this.photo.getFontType());
        fontIndex = fontIndex < 0 ? 0 : fontIndex;
        this.photo.setFontType(fonts.get((fontIndex + 1) % fonts.size()));
        this.setPhotoFont(this.getTextView(R.id.photo_label));
    }

    private void onLocationChange() {
        List<Integer> locations = Arrays.asList(
                Gravity.START | Gravity.TOP, Gravity.CENTER_HORIZONTAL | Gravity.TOP, Gravity.END | Gravity.TOP,
                Gravity.START | Gravity.CENTER_VERTICAL, Gravity.CENTER, Gravity.END | Gravity.CENTER_VERTICAL,
                Gravity.START | Gravity.BOTTOM, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, Gravity.END | Gravity.BOTTOM
        );
        int locationIndex = locations.indexOf(this.photo.getFontLocation());
        locationIndex = locationIndex < 0 ? 0 : locationIndex;
        this.photo.setFontLocation(locations.get((locationIndex + 1) % locations.size()));
        this.setFontLocation(this.getTextView(R.id.photo_label));
    }

    private void onSizeChange(int change) {
        TextView textView = this.getTextView(R.id.photo_label);
        float fontSize = textView.getTextSize() + change * dpToPx(2);
        if (fontSize > dpToPx(10) && fontSize < dpToPx(100)) {
            this.photo.setFontSize((int) fontSize);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        }
    }

    private void onColorChange() {
        int a = this.getSeekView(R.id.font_color_a).getProgress();
        int r = this.getSeekView(R.id.font_color_r).getProgress();
        int g = this.getSeekView(R.id.font_color_g).getProgress();
        int b = this.getSeekView(R.id.font_color_b).getProgress();
        this.photo.setFontColor(Color.argb(a, r, g, b));
        this.getTextView(R.id.photo_label).setTextColor(Color.argb(a, r, g, b));
    }

    private void onTextChange(String text) {
        this.photo.setFontText(text);
        this.getTextView(R.id.photo_label).setText(text);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}