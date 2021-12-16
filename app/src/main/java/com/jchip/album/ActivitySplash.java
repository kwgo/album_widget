package com.jchip.album;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import com.jchip.album.activity.AbstractActivity;
import com.jchip.album.activity.AlbumActivity;
import com.jchip.album.activity.FontActivity;
import com.jchip.album.activity.FrameActivity;
import com.jchip.album.view.AlbumView;

public class ActivitySplash extends AbstractActivity {
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