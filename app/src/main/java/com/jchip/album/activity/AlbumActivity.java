package com.jchip.album.activity;

import android.os.Bundle;

import com.jchip.album.R;

public class AlbumActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_layer);
    }
}