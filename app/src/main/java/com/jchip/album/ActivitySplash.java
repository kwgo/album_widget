package com.jchip.album;

import android.os.Bundle;

import com.jchip.album.layer.AbstractLayer;

public class ActivitySplash extends AbstractLayer {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        this.getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        );

        this.startActivity(ActivityAlbum.class);
        this.finish();
    }
}