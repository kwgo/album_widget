package com.jchip.album.activity;

import android.os.Bundle;
import android.view.View;

import com.jchip.album.R;

public class PhotoActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_photo);
//        if (this.isLandscape()) {
//            View view = this.findViewById(R.id.album_photo_view);
//            view.setRotation(90);
//        }
    }
}