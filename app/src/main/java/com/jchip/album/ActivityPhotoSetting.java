package com.jchip.album;

import android.os.Bundle;
import android.view.WindowManager;

import com.jchip.album.widget.WidgetPhotoProvider;
import com.jchip.album.widget.WidgetPhotoSetting;

public class ActivityPhotoSetting extends WidgetPhotoSetting {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }

    public static class PhotoProvider extends WidgetPhotoProvider {
    }
}