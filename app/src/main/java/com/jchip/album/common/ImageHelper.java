package com.jchip.album.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.FileInputStream;

public class ImageHelper {
    public static Bitmap convertBitmap(Bitmap bitmap, float ratio, int rotation, int flip, int maxSize) {
        Log.d("", "start convertBitmap== " + maxSize);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (maxSize > 0 && maxSize < Math.max(width, height)) {
            ratio = ratio * maxSize / Math.max(width, height);
        }
        Matrix matrix = new Matrix();
        if (rotation > 0) {
            matrix.postRotate(rotation * 90);
        }
        matrix.postScale(flip == 0 ? ratio : -ratio, ratio);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        Log.d("", "end convertBitmap== " + maxSize);
        return bitmap;
    }

    public static Bitmap loadBitmap(String path, boolean inScaled) {
        Log.d("", "loadBitmap by path start .....");
        try (FileInputStream inputStream = new FileInputStream(path)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = inScaled;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("", "loadBitmap end .....");
            return bitmap;
        } catch (Exception ignored) {
        }
        return null;
    }

    public static Bitmap loadBitmap(Resources resources, int imageId, boolean inScaled) {
        try {
            Log.d("", "loadBitmap by id start .....");
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inScaled = inScaled;
//            return BitmapFactory.decodeResource(resources, imageId, options);
            return BitmapFactory.decodeResource(resources, imageId);
        } catch (Exception ignored) {
        }
        return null;
    }
}
