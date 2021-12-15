package com.jchip.album;

import android.os.Bundle;
import android.view.WindowManager;

import com.jchip.album.activity.AlbumActivity;

public class ActivityAlbum extends AlbumActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }
}