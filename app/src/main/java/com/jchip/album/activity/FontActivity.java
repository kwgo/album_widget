package com.jchip.album.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.widget.SeekBar;

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
        if (!isEmpty(this.photo)) {
            Bitmap bitmap = AlbumHelper.loadBitmap(this.photo.getPhotoPath());
            bitmap = ImageHelper.convertBitmap(bitmap, this.photo.getRotationIndex(), this.photo.getFlipIndex());
            this.getImageView(R.id.font_image).setImageBitmap(bitmap);

            if (!isEmpty(this.photo.getComment())) {
                this.getEditView(R.id.font_text).setText(this.photo.getComment());
                this.getTextView(R.id.font_label).setText(this.photo.getComment());
            }

        }

        this.getButtonView(R.id.font_type).setOnClickListener((v) -> this.onFontTypeChange());
        this.getButtonView(R.id.font_location).setOnClickListener((v) -> this.onLocationChange());

        this.getButtonView(R.id.font_size_plus).setOnClickListener((v) -> this.onSizeChange(true, false));
        this.getButtonView(R.id.font_size_minus).setOnClickListener((v) -> this.onSizeChange(false, true));

        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("", "onTextChanged=====" + s);
                onTextChange(s.toString().trim());
            }

            public void afterTextChanged(Editable s) {
                Log.d("", "afterTextChanged=====" + s.toString());
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
                Log.d("", "++++++++++++++++++++++progress++" + progress);
                Log.d("", "++++++++++++++++++++++fromUser++" + fromUser);
                if (seekBar.getId() == R.id.font_color_a) {
                    onColorChange(progress, -1, -1, -1);
                } else if (seekBar.getId() == R.id.font_color_r) {
                    onColorChange(-1, progress, -1, -1);
                } else if (seekBar.getId() == R.id.font_color_g) {
                    onColorChange(-1, -1, progress, -1);
                } else if (seekBar.getId() == R.id.font_color_b) {
                    onColorChange(-1, -1, -1, progress);
                }
            }
        };

        this.getSeekView(R.id.font_color_r).setOnSeekBarChangeListener(seekListener);
        this.getSeekView(R.id.font_color_g).setOnSeekBarChangeListener(seekListener);
        this.getSeekView(R.id.font_color_b).setOnSeekBarChangeListener(seekListener);
        this.getSeekView(R.id.font_color_a).setOnSeekBarChangeListener(seekListener);
    }

    private void onFontTypeChange() {

    }


    private void onLocationChange() {
        int[] gravities = new int[]{Gravity.TOP | Gravity.START, Gravity.TOP | Gravity.END, Gravity.BOTTOM | Gravity.END, Gravity.BOTTOM | Gravity.START};
        gravity = (gravity + 1) % gravities.length;
        this.getTextView(R.id.font_label).setGravity(gravity);
    }

    private void onSizeChange(boolean sizePlus, boolean sizeMinus) {
        Log.d("", "++++++onButtonClick++++++++++++++++onButtonClick++");
        float fontSize = this.getTextView(R.id.font_label).getTextSize();
        fontSize += sizePlus ? 1 : sizeMinus ? -1 : 0;
        if (fontSize > 8 && fontSize < 50) {
            this.getTextView(R.id.font_label).setTextSize(fontSize);
        }
    }

    private void onColorChange(int a, int r, int g, int b) {
        Log.d("", "---++onColorChange++++---------------+++++onColorChange++" + Color.argb(a, r, g, b));
        this.getTextView(R.id.font_label).setTextColor(Color.argb(a, r, g, b));
    }

    private void onTextChange(String text) {
        this.getTextView(R.id.font_label).setText(text);
    }

}