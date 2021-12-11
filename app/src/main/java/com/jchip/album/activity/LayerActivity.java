package com.jchip.album.activity;

import android.util.Log;
import android.view.View;

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
    protected View fontEditButton;
    protected View frameAddButton;
    protected View photoAddButton;

    private String currentLayer;

    @Override
    public void initContentView() {
        this.albumNameView = this.findViewById(R.id.album_name_text);
        this.albumPhotoView = this.findViewById(R.id.photo_image);
        this.albumFontView = this.findViewById(R.id.photo_font);
        this.albumFrameView = this.findViewById(R.id.photo_frame);

        // button views
        this.photoAddButton = this.findViewById(R.id.album_photo_add);
        this.frameAddButton = this.findViewById(R.id.album_frame_add);
        this.fontEditButton = this.findViewById(R.id.album_font_edit);
        this.settingButton = this.findViewById(R.id.album_setting);

        this.fontEditButton.setOnClickListener((v) -> this.toggleLayer(LAYER_FONT, LAYER_PHOTO));
        this.frameAddButton.setOnClickListener((v) -> this.toggleLayer(LAYER_FRAME, LAYER_PHOTO));
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

        this.setVisibility(albumNameView, true, false);
        this.setVisibility(albumPhotoView, true, false);
        this.setVisibility(albumFontView, false, false);
        this.setVisibility(albumFrameView, false, false);


        this.setVisibility(settingButton, true, false);
        this.setVisibility(fontEditButton, true, false);
        this.setVisibility(frameAddButton, true, false);
        this.setVisibility(photoAddButton, true, false);

        switch (layer) {
            case LAYER_ALBUM:
                this.setVisibility(fontEditButton, false, false);
                this.setVisibility(frameAddButton, false, false);
                break;
            case LAYER_PHOTO:
                break;
            case LAYER_FONT:
                this.setVisibility(albumFontView, true, false);
                break;
            case LAYER_FRAME:
                this.setVisibility(albumFrameView, true, false);
                break;
        }

    }


}