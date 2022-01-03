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
        Log.d("", " convert bitmap fit = " + fit + " rotation = " + rotation);
        Log.d("", " convert bitmap width= " + imageWidth + " height= " + imageHeight + " to view width= " + width + " height= " + height);
        int px = 0, py = 0;
        int cropWidth = imageWidth, cropHeight = imageHeight;
        float scaleWidth = scale, scaleHeight = scale;
        if (width > 0 && height > 0 && imageWidth > 0 && imageHeight > 0) {
            if (rotation % 2 != 0) {
                int value = width;
                width = height;
                height = value;
            }
            scaleWidth = scaleWidth * width / imageWidth;
            scaleHeight = scaleHeight * height / imageHeight;
            Log.d("", " convert scaleWidth= " + scaleWidth + " scaleHeight= " + scaleHeight);
            if (fit == 0) { // centerCrop
                scaleWidth = scaleHeight = Math.max(scaleWidth, scaleHeight);
                cropWidth = (int) (1.0f * width / scaleWidth + 0.5);
                cropHeight = (int) (1.0f * height / scaleHeight + 0.5);
            } else if (fit == 1) { // fitCenter
                scaleWidth = scaleHeight = Math.min(scaleWidth, scaleHeight);
                cropWidth = Math.min((int) (1.0f * width / scaleWidth + 0.5), imageWidth);
                cropHeight = Math.min((int) (1.0f * height / scaleHeight + 0.5), imageHeight);
            } else if (fit == 2) { // fitXY
                cropWidth = imageWidth;
                cropHeight = imageHeight;
            } else if (fit == 3) { // center
                scaleWidth = scaleHeight = scale;
                cropWidth = Math.min((int) (1.0f * width / scaleWidth + 0.5), imageWidth);
                cropHeight = Math.min((int) (1.0f * height / scaleHeight + 0.5), imageHeight);
            }
            px = (imageWidth - cropWidth) / 2;
            py = (imageHeight - cropHeight) / 2;
        }
        Log.d("", " convert by scaleWidth= " + scaleWidth + " scaleHeight= " + scaleHeight);
        Log.d("", " convert to cropWidth= " + cropWidth + " cropHeight= " + cropHeight);
        Log.d("", " convert from point px= " + px + " py= " + py);
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation * 90);
            matrix.postScale(flip == 0 ? scaleWidth : -scaleWidth, scaleHeight);
            bitmap = Bitmap.createBitmap(bitmap, px, py, cropWidth, cropHeight, matrix, true);
            Log.d("", " convert new bitmap with width= " + bitmap.getWidth() + " height= " + bitmap.getHeight());
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
            Log.d("", "decode bitmap from resource imageId= " + imageId);
            Log.d("", "decode bitmap width= " + imageWidth + " height= " + imageHeight + " to view width= " + width + " height= " + height);
            if (imageWidth <= 0 || imageHeight <= 0) {
                Log.d("", "decode bitmap from resource we do need InSampleSize from resource.");
                options.inJustDecodeBounds = true; // First decode with inJustDecodeBounds=true to check dimensions
                BitmapFactory.decodeResource(resources, imageId, options);
                imageWidth = options.outWidth;
                imageHeight = options.outHeight;
                imageRect.left = options.outWidth;
                imageRect.top = options.outHeight;
            } else {
                Log.d("", "decode bitmap as we known the bitmap info width= " + imageWidth + " height= " + imageHeight);
                options.outWidth = imageWidth;
                options.outHeight = imageHeight;
            }
            Log.d("", "decode bitmap from resource options.outWidth  ....." + options.outWidth + " options.outHeight  ....." + options.outHeight);

            options.inSampleSize = calculateInSampleSize(imageWidth, imageHeight, width, height);
            Log.d("", "decode bitmap from resource options.inSampleSize  ....." + options.inSampleSize);

            options.inScaled = false;
            options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
            //    options.inTargetDensity = resources.getDisplayMetrics().densityDpi;
            options.inDensity = resources.getDisplayMetrics().densityDpi;
            options.inJustDecodeBounds = false;  // Decode bitmap with inSampleSize set
            Bitmap bitmap = BitmapFactory.decodeResource(resources, imageId, options);
            if (bitmap != null) {
                Log.d("", "decode bitmap from resource rect end .....bitmap width= " + bitmap.getWidth() + " height= " + bitmap.getHeight());
                Log.d("", "decode bitmap from resource rect end .....bitmap density= " + bitmap.getDensity());
            }
            return bitmap;
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
            Log.d("", "decode from file url bitmap width= " + imageWidth + " height= " + imageHeight + " to view width= " + width + " height= " + height);
            if (imageWidth <= 0 || imageHeight <= 0) {
                Log.d("", "decode bitmap from file url we do need InSampleSize from resource.");
                options.inJustDecodeBounds = true; // First decode with inJustDecodeBounds=true to check dimensions
                BitmapFactory.decodeStream(inputStream, null, options);
                imageWidth = options.outWidth;
                imageHeight = options.outHeight;
                imageRect.left = options.outWidth;
                imageRect.top = options.outHeight;
                inputStream.getChannel().position(0);
            } else {
                Log.d("", "decode bitmap as we known the bitmap info width= " + imageWidth + " height= " + imageHeight);
                options.outWidth = imageWidth;
                options.outHeight = imageHeight;
            }
            Log.d("", "decode bitmap from file url options.outWidth  ....." + options.outWidth + " options.outHeight  ....." + options.outHeight);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(imageWidth, imageHeight, width, height);
            Log.d("", "decode bitmap from resource options.inSampleSize  ....." + options.inSampleSize);

            options.inScaled = false;
            options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
            //      options.inTargetDensity = resources.getDisplayMetrics().densityDpi;
            options.inDensity = resources.getDisplayMetrics().densityDpi;
            options.inJustDecodeBounds = false;  // Decode bitmap with inSampleSize set
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            if (bitmap != null) {
                Log.d("", "decode bitmap from file url end .....bitmap width= " + bitmap.getWidth() + " height= " + bitmap.getHeight());
                Log.d("", "decode bitmap from file url end .....bitmap density= " + bitmap.getDensity());
            }
            return bitmap;
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
            Log.d("", "before ratioWidth = " + ratioWidth);
            Log.d("", "before ratioHeight = " + ratioHeight);
            float ratio = Math.max(ratioWidth, ratioHeight);
            Log.d("", "before ratio = " + ratio);
            inSampleSize = ratio > 0f && ratio < 1.0 ? (int) (1.0f / ratio + 0.0) : 1;
            Log.d("", "before inSampleSize ++ ++ ++= " + inSampleSize);
        }
        Log.d("", "calculated inSampleSize= " + inSampleSize);
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
