package com.jchip.album.layer;

import android.graphics.Rect;
import android.view.OrientationEventListener;
import android.view.Surface;

public abstract class OrientationLayer extends DataLayer {
    protected int orientation = 0;
    private OrientationEventListener orientationEventListener;

    @Override
    public void initContentView() {
        super.initContentView();

        this.orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                int orientation = -1;
                if (rotation >= 330 || rotation < 30) {
                    orientation = Surface.ROTATION_0;
                } else if (rotation >= 60 && rotation < 120) {
                    orientation = Surface.ROTATION_90;
                } else if (rotation >= 150 && rotation < 210) {
                    orientation = Surface.ROTATION_180;
                } else if (rotation >= 240 && rotation < 300) {
                    orientation = Surface.ROTATION_270;
                }
                if (orientation != OrientationLayer.this.orientation) {
                    OrientationLayer.this.orientation = orientation;
                    OrientationLayer.this.onOrientationChanged(orientation);
                }
            }
        };
    }

    @Override
    public Rect getViewRect(int sourceId) {
        Rect rect = super.getViewRect(sourceId);
        if (this.orientation == 1 || this.orientation == 3) {
            return new Rect(0, 0, rect.bottom, rect.right);
        }
        return rect;
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.orientationEventListener.enable();
    }

    @Override
    protected void onStop() {
        this.orientationEventListener.disable();
        super.onStop();
    }
}