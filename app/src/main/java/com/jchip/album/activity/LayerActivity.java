package com.jchip.album.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.jchip.album.ActivityFont;
import com.jchip.album.ActivityFrame;
import com.jchip.album.R;

public class LayerActivity extends AbstractActivity {

    protected static final String LAYER_ALBUM = "layerAlbum";
    protected static final String LAYER_PHOTO = "layerPhoto";
    protected static final String LAYER_FRAME = "layerFrame";
    protected static final String LAYER_FONT = "layerFont";

    protected View albumNameView;
    protected View albumPhotoView;
    protected View albumFontView;
    protected View albumFrameView;

    protected View settingButton;
    protected View fontSettingButton;
    protected View frameSelectButton;
    protected View photoAddButton;

    private String currentLayer;

    @Override
    public void initContentView() {
        this.albumNameView = this.findViewById(R.id.album_name_text);
        this.albumPhotoView = this.findViewById(R.id.photo_image);
        this.albumFontView = this.findViewById(R.id.photo_font);
        this.albumFrameView = this.findViewById(R.id.photo_frame_container);

        // button views
        this.photoAddButton = this.findViewById(R.id.photo_add);
        this.frameSelectButton = this.findViewById(R.id.frame_select);
        this.fontSettingButton = this.findViewById(R.id.font_setting);
        this.settingButton = this.findViewById(R.id.album_setting);

        this.fontSettingButton.setOnClickListener((v) -> this.onFontSetting());
        this.frameSelectButton.setOnClickListener((v) -> this.onFrameSelect());
    }

    public void onFontSetting() {
        this.startActivity(ActivityFont.class, (intent) -> onFrameChange(intent));
    }

    public void onFrameSelect() {
        this.startActivity(ActivityFrame.class, (intent) -> onFontChange(intent));
    }

    public void onFrameChange(Intent intent) {
        Log.d("", "call back =========" + "onFrameChange");
        if (intent != null) {
            int frameResourceId = intent.getIntExtra(FRAME_RESOURCE, -1);
            Log.d("", "frameResourceId=========" + frameResourceId);

            this.findViewById(R.id.photo_frame_container).setBackgroundResource(frameResourceId);
            this.findViewById(R.id.photo_frame_corver).setBackgroundResource(frameResourceId);
        }
    }

    public void onFontChange(Intent intent) {
        Log.d("", "call back =========" + "onFontChange");
        if (intent != null) {

        }
    }

    public void toggleLayer(String layer, String preLayer) {
        this.setLayer(layer, preLayer, !layer.equals(this.currentLayer));
    }

    public void setLayer(String layer, String preLayer, boolean apply) {
        this.setLayer(apply ? layer : preLayer);
    }

    public void setLayer(String layer) {
        //this.previousLayer = this.currentLayer;
        this.currentLayer = layer;
        Log.d("", "current layer:" + currentLayer);
//
//        this.setVisibility(albumNameView, true, false);
//        this.setVisibility(albumPhotoView, true, false);
//        this.setVisibility(albumFontView, false, false);
//        this.setVisibility(albumFrameView, false, false);
//
//
//        this.setVisibility(settingButton, true, false);
//        this.setVisibility(fontSettingButton, true, false);
//        this.setVisibility(frameSelectButton, true, false);
//        this.setVisibility(photoAddButton, true, false);

        switch (layer) {
            case LAYER_ALBUM:
                //this.setVisibility(fontSettingButton, false, false);
                //this.setVisibility(frameSelectButton, false, false);
                break;
            case LAYER_PHOTO:
                break;
            case LAYER_FONT:
                //this.setVisibility(albumFontView, true, false);
                break;
            case LAYER_FRAME:
                //this.setVisibility(albumFrameView, true, false);
                break;
        }

    }


}