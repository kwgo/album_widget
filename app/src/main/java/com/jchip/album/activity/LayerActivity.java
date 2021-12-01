package com.jchip.album.activity;

public class LayerActivity extends AbstractActivity {

    protected static final String LAYER_ALBUM = "layerAlbum";
    protected static final String LAYER_PHOTO = "layerPhoto";
    protected static final String LAYER_FRAME = "layerFrame";
    protected static final String LAYER_FONT = "layerFont";

    @Override
    public void initContentView() {
    }

    public void setLayer(String layer) {
        switch (layer) {
            case LAYER_ALBUM:
                //this.startActivity(ActivityAlbum.class);
                break;
            case LAYER_PHOTO:
                // this.startActivity(ActivityPhoto.class);
                break;
            case LAYER_FRAME:
                //this.startActivity(ActivityFrame.class);
                break;
            case LAYER_FONT:
                //this.startActivity(ActivityFont.class);
                break;

        }
    }

}