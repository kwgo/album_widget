package com.jchip.album.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.common.ImageHelper;
import com.jchip.album.data.PhotoData;

public class FontActivity extends AbstractActivity {

    private int gravity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.font_layer);
    }

    @Override
    public void initContentView() {
        Intent intent = this.getIntent();
        this.photo = (PhotoData) intent.getSerializableExtra(PhotoData.tableName);
        if (isEmpty(this.photo)) {
            this.photo = new PhotoData();
        }
        Log.d("", " this.photo====" + this.photo);

        if (!isEmpty(this.photo)) {
            if (!isEmpty(this.photo.getPhotoPath())) {
                Bitmap bitmap = AlbumHelper.loadBitmap(this.photo.getPhotoPath());
                bitmap = ImageHelper.convertBitmap(bitmap, this.photo.getRotationIndex(), this.photo.getFlipIndex());
                this.getImageView(R.id.font_image).setImageBitmap(bitmap);
            }
            if (!isEmpty(this.photo.getFontText())) {
                this.getEditView(R.id.font_text).setText(this.photo.getFontText());
                this.getTextView(R.id.font_label).setText(this.photo.getFontText());
            }
            this.setFontLocation(this.photo.getFontLocation());
            this.getTextView(R.id.font_label).setTextColor(this.photo.getFontColor());
            this.getSeekView(R.id.font_color_a).setProgress((this.photo.getFontColor() >> 32) & 0xFF);
            this.getSeekView(R.id.font_color_r).setProgress((this.photo.getFontColor() >> 16) & 0xFF);
            this.getSeekView(R.id.font_color_g).setProgress((this.photo.getFontColor() >> 8) & 0xFF);
            this.getSeekView(R.id.font_color_b).setProgress((this.photo.getFontColor()) & 0xFF);
        }

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

        findViewById(R.id.font_setting_view).setOnClickListener((v) -> this.finish());
    }

    private void onFontTypeChange() {

    }


    private void onLocationChange() {
        this.photo.setFontLocation((this.photo.getFontLocation() + 1) % 4);
        this.setFontLocation(this.photo.getFontLocation());
    }

    private void setFontLocation(int fontLocation) {
        TextView textView = this.getTextView(R.id.font_label);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(fontLocation % 2 == 0 ? RelativeLayout.ALIGN_PARENT_START : RelativeLayout.ALIGN_PARENT_END);
        layoutParams.addRule(fontLocation / 2 == 0 ? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.ALIGN_PARENT_BOTTOM);
        textView.setLayoutParams(layoutParams);
    }

    private void onSizeChange(int change) {
        TextView textView = this.getTextView(R.id.font_label);
        float fontSize = textView.getTextSize() + change * 2;
        if (fontSize > 16 && fontSize < 120) {
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

}