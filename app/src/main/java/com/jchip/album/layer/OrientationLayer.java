package com.jchip.album.layer;

import android.graphics.Rect;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.OrientationEventListener;
import android.view.Surface;

public abstract class OrientationLayer extends AbstractLayer {
    private static final int ROTATION_DELAY = 500;

    protected int orientation = 0;
    private CountDownTimer rotationTimer;
    private OrientationEventListener orientationEventListener;

    public void enableOrientation() {
        this.rotationTimer = new CountDownTimer(ROTATION_DELAY, ROTATION_DELAY) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                onOrientationChanged(orientation);
            }
        };

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
                    // if (!isRotationLocked()) {
                    OrientationLayer.this.orientation = orientation;
                    if (rotationTimer != null) {
                        rotationTimer.cancel();
                        rotationTimer.start();
                    }
                    //OrientationLayer.this.onOrientationChanged(orientation);
                    // }
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

    protected boolean isRotationLocked() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) != 1;
        } catch (Exception ignore) {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.orientationEventListener != null) {
            this.orientationEventListener.enable();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.orientationEventListener != null) {
            this.orientationEventListener.disable();
        }
    }

    @Override
    protected void onDestroy() {
        if (this.rotationTimer != null) {
            this.rotationTimer.cancel();
        }
        super.onDestroy();
    }
}