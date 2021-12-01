package com.jchip.album.activity;

import android.view.View;

import com.jchip.album.R;

public class LayerActivity extends AbstractActivity {

    protected static final String LAYER_ALBUM = "layerAlbum";
    protected static final String LAYER_PHOTO = "layerPhoto";
    protected static final String LAYER_FRAME = "layerFrame";
    protected static final String LAYER_FONT = "layerFont";

    private View albumNameView;
    private View albumPhotoView;
    private View albumFontView;
    private View albumFrameView;


    private View settingView;
    private View fontEditView;
    private View photoAddView;

    @Override
    public void initContentView() {
        albumNameView = this.findViewById(R.id.album_name);
        albumPhotoView = this.findViewById(R.id.album_photo);
        albumFontView = this.findViewById(R.id.album_font);
        albumFrameView = this.findViewById(R.id.album_frame);

        // button views
        settingView = this.findViewById(R.id.album_setting);
        fontEditView = this.findViewById(R.id.album_font_edit);
        photoAddView = this.findViewById(R.id.album_photo_add);
    }

    public void setLayer(String layer) {
        switch (layer) {
            case LAYER_ALBUM:
                this.setVisibility(albumNameView, true, false);
                this.setVisibility(albumPhotoView, true, false);
                this.setVisibility(albumFontView, false, false);
                this.setVisibility(albumFrameView, false, false);
                this.setVisibility(settingView, false, false);
                this.setVisibility(fontEditView, false, false);
                this.setVisibility(photoAddView, true, false);
                break;
            case LAYER_PHOTO:
                this.setVisibility(albumNameView, true, false);
                this.setVisibility(albumPhotoView, true, false);
                this.setVisibility(albumFontView, true, false);
                this.setVisibility(albumFrameView, false, false);
                this.setVisibility(settingView, false, false);
                this.setVisibility(fontEditView, true, false);
                this.setVisibility(photoAddView, true, false);
                break;
            case LAYER_FRAME:
                this.setVisibility(albumNameView, true, false);
                this.setVisibility(albumPhotoView, true, false);
                this.setVisibility(albumFontView, false, false);
                this.setVisibility(albumFrameView, true, false);
                this.setVisibility(settingView, true, false);
                this.setVisibility(fontEditView, false, false);
                this.setVisibility(photoAddView, true, false);
                break;
            case LAYER_FONT:
                this.setVisibility(albumNameView, true, false);
                this.setVisibility(albumPhotoView, true, false);
                this.setVisibility(albumFontView, false, false);
                this.setVisibility(albumFrameView, true, false);
                this.setVisibility(settingView, false, false);
                this.setVisibility(fontEditView, true, false);
                this.setVisibility(photoAddView, false, false);
                break;
        }
    }


}