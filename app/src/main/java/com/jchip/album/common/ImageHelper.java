package com.jchip.album.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.FileInputStream;

public class ImageHelper {
    public static Bitmap convertBitmap(Bitmap bitmap, float scale, int fit, int rotation, int flip, int width, int height) {
        int imageWidth = bitmap.getWidth(), imageHeight = bitmap.getHeight();
        int px = 0, py = 0;
        float scaleWidth = scale, scaleHeight = scale;
        int cropWidth = imageWidth, cropHeight = imageHeight;
        int viewWidth = rotation % 2 == 0 ? width : height;
        int viewHeight = rotation % 2 == 0 ? height : width;
        if (viewWidth > 0 && viewHeight > 0 && imageWidth > 0 && imageHeight > 0) {
            scaleWidth = scale * viewWidth / imageWidth;
            scaleHeight = scale * viewHeight / imageHeight;
            if (fit == 0) { // centerCrop
                scaleWidth = scaleHeight = Math.max(scaleWidth, scaleHeight);
                cropWidth = (int) (1.0f * viewWidth / scaleWidth + 0.5);
                cropHeight = (int) (1.0f * viewHeight / scaleHeight + 0.5);
            } else if (fit == 1) { // fitCenter);
                scaleWidth = scaleHeight = Math.min(scaleWidth, scaleHeight);
                cropWidth = Math.min((int) (1.0f * viewWidth / scaleWidth + 0.5), imageWidth);
                cropHeight = Math.min((int) (1.0f * viewHeight / scaleHeight + 0.5), imageHeight);
            } else if (fit == 2) { // fitXY
                scaleWidth = rotation % 2 == 0 ? scale * viewWidth / imageWidth : scale * viewHeight / imageHeight;
                scaleHeight = rotation % 2 == 0 ? scale * viewHeight / imageHeight : scale * viewWidth / imageWidth;
                cropWidth = imageWidth;
                cropHeight = imageHeight;
            } else if (fit == 3) { // center
                scaleWidth = scaleHeight = scale;
                cropWidth = Math.min((int) (1.0f * viewWidth / scaleWidth + 0.5), imageWidth);
                cropHeight = Math.min((int) (1.0f * viewHeight / scaleHeight + 0.5), imageHeight);
            }
            px = (imageWidth - cropWidth) / 2;
            py = (imageHeight - cropHeight) / 2;
        }
        try {
            Log.d("", "convert bitmap imageWidth = " + imageWidth + " imageHeight = " + imageHeight);
            Log.d("", "convert bitmap viewWidth = " + viewWidth + " viewHeight = " + viewHeight);
            Log.d("", "convert bitmap scaleWidth = " + scaleWidth + " scaleHeight = " + scaleHeight);
            Log.d("", "convert bitmap cropWidth = " + cropWidth + " cropHeight = " + cropHeight);

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation * 90);
            matrix.postScale(flip == 0 ? scaleWidth : -scaleWidth, scaleHeight);
            bitmap = Bitmap.createBitmap(bitmap, px, py, cropWidth, cropHeight, matrix, true);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeBitmap(Resources resources, int imageId, Rect imageRect) {
        int imageWidth = imageRect.left, imageHeight = imageRect.top;
        int width = imageRect.right, height = imageRect.bottom;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            if (imageWidth <= 0 || imageHeight <= 0) {
                options.inJustDecodeBounds = true; // First decode with inJustDecodeBounds=true to check dimensions
                BitmapFactory.decodeResource(resources, imageId, options);
                imageWidth = options.outWidth;
                imageHeight = options.outHeight;
                imageRect.left = options.outWidth;
                imageRect.top = options.outHeight;
            } else {
                options.outWidth = imageWidth;
                options.outHeight = imageHeight;
            }
            options.inSampleSize = calculateInSampleSize(imageWidth, imageHeight, width, height);
            options.inScaled = false;
            options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
            //    options.inTargetDensity = resources.getDisplayMetrics().densityDpi;
            options.inDensity = resources.getDisplayMetrics().densityDpi;
            options.inJustDecodeBounds = false;  // Decode bitmap with inSampleSize set
            return BitmapFactory.decodeResource(resources, imageId, options);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeBitmap(Resources resources, String imageUrl, Rect imageRect) {
        int imageWidth = imageRect.left, imageHeight = imageRect.top;
        int width = imageRect.right, height = imageRect.bottom;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try (FileInputStream inputStream = new FileInputStream(imageUrl)) {
            if (imageWidth <= 0 || imageHeight <= 0) {
                options.inJustDecodeBounds = true; // First decode with inJustDecodeBounds=true to check dimensions
                BitmapFactory.decodeStream(inputStream, null, options);
                imageWidth = options.outWidth;
                imageHeight = options.outHeight;
                imageRect.left = options.outWidth;
                imageRect.top = options.outHeight;
                inputStream.getChannel().position(0);
            } else {
                options.outWidth = imageWidth;
                options.outHeight = imageHeight;
            }
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(imageWidth, imageHeight, width, height);
            options.inScaled = false;
            options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
            // options.inTargetDensity = resources.getDisplayMetrics().densityDpi;
            options.inDensity = resources.getDisplayMetrics().densityDpi;
            options.inJustDecodeBounds = false;  // Decode bitmap with inSampleSize set
            return BitmapFactory.decodeStream(inputStream, null, options);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    public static boolean isNeedCalculateInSampleSize(int imageWidth, int imageHeight, int width, int height) {
        return (imageWidth > 0 && imageHeight > 0 && width > 0 && height > 0)
                && (imageWidth > width && imageHeight > height);
    }

    public static int calculateInSampleSize(int imageWidth, int imageHeight, int width, int height) {
        int inSampleSize = 1;
        if (isNeedCalculateInSampleSize(imageWidth, imageHeight, width, height)) {
            float ratioWidth = width > 0 ? 1.0f * width / imageWidth : 1.0f;
            float ratioHeight = height > 0 ? 1.0f * height / imageHeight : 1.0f;
            float ratio = Math.max(ratioWidth, ratioHeight);
            inSampleSize = ratio > 0f && ratio < 1.0 ? (int) (1.0f / ratio + 0.0) : 1;
        }
        return inSampleSize;
    }

    public static Bitmap getImageBitmap(Resources resources, int imageId, float densityFactor) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;
            options.inTargetDensity = resources.getDisplayMetrics().densityDpi;
            options.inDensity = (int) (1f * options.inTargetDensity / densityFactor);
            return BitmapFactory.decodeResource(resources, imageId, options);
        } catch (Exception ignore) {
            return null;
        }
    }
}
