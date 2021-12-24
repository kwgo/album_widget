package com.jchip.album.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

public class ImageHelper {
    public static Bitmap convertBitmap(Bitmap bitmap, float ratio, int rotation, int flip, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (maxSize > 0 && maxSize < Math.max(width, height)) {
            ratio = ratio * maxSize / Math.max(width, height);
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation * 90);
        matrix.postScale(flip == 0 ? ratio : -ratio, ratio);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
