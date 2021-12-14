package com.jchip.album.activity;

import android.content.Intent;
import android.util.Log;

import com.jchip.album.ActivityFont;
import com.jchip.album.ActivityFrame;
import com.jchip.album.R;

public class LayerActivity extends AbstractActivity {

    @Override
    public void initContentView() {
        this.findViewById(R.id.font_setting).setOnClickListener((v) -> this.onFontSetting());
        this.findViewById(R.id.frame_select).setOnClickListener((v) -> this.onFrameSelect());
    }

    public void onFontSetting() {
        this.startActivity(ActivityFont.class, (intent) -> onFontChange(intent));
    }

    public void onFrameSelect() {
        this.startActivity(ActivityFrame.class, (intent) -> onFrameChange(intent));
    }

    public void onFrameChange(Intent intent) {
        Log.d("", "call onFrameChange back =========" + "onFrameChange");
        if (intent != null) {
            int frameResourceId = intent.getIntExtra(FRAME_RESOURCE, -1);
            Log.d("", "frameResourceId=========" + frameResourceId);

            this.findViewById(R.id.photo_frame_container).setBackgroundResource(frameResourceId);
            this.findViewById(R.id.photo_frame_cover).setBackgroundResource(frameResourceId);
        }
    }

    public void onFontChange(Intent intent) {
        Log.d("", "call onFontChange back =========" + "onFontChange");
        if (intent != null) {

        }
    }
}