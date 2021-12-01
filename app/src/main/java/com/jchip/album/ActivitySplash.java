package com.jchip.album;

import android.os.Bundle;

import com.jchip.album.activity.AbstractActivity;

public class ActivitySplash extends AbstractActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.openLayer(PHOTO_LAYER);
       // this.openLayer(ALBUM_LAYER);
        this.startActivity(ActivityAlbum.class);
        this.finish();
    }

    @Override
    public void initContentView() {
    }
}