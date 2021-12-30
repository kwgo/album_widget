package com.jchip.album.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.FileInputStream;

public class ImageHelper {
    public static Bitmap convertBitmap(Bitmap bitmap, float ratio, int rotation, int flip, int maxWidth, int maxHeight) {
        Log.d("", "start convertBitmap ratio= " + ratio);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.d("", "start convert bitmap - width= " + width + " height= " + height + " maxWidth== " + maxWidth + " maxHeight== " + maxHeight);
        if (maxWidth > 0 || maxHeight > 0) {
            float ratioWidth = maxWidth > 0 ? 1f * maxWidth / width : 1.0f;
            float ratioHeight = maxHeight > 0 ? 1f * maxHeight / height : 1.0f;
            float maxRatio = Math.max(ratioWidth, ratioHeight);
            ratio = ratio * (maxRatio > 0 && maxRatio < 1 ? maxRatio : 1.0f);
        }
        if (rotation > 0 || flip != 0 || Math.abs(ratio - 1.0f) > 0.01f) {
            Matrix matrix = new Matrix();
            if (rotation > 0) {
                matrix.postRotate(rotation * 90);
            }
            if (flip != 0 || Math.abs(ratio - 1.0f) > 0.01f) {
                matrix.postScale(flip == 0 ? ratio : -ratio, ratio);
            }
            Log.d("", "create new bitmap with = " + rotation + " and flip= " + flip + " and ratio= " + ratio);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        Log.d("", "end convert bitmap - width= " + width + " height= " + height + " maxWidth== " + maxWidth + " maxHeight== " + maxHeight);
        return bitmap;
    }

    public static Bitmap loadBitmap(Resources resources, int imageId, boolean inScaled) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = inScaled;
            return BitmapFactory.decodeResource(resources, imageId, options);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Bitmap decodeBitmap(String url, int bitmapWidth, int bitmapHeight, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        try (FileInputStream inputStream = new FileInputStream(url)) {
            if (bitmapWidth <= 0 || bitmapHeight <= 0) {
                Log.d("", "decode from url start .... url= " + url);
                Log.d("", "decode from url start .... require width= " + width + " height= " + height);
                // First decode with inJustDecodeBounds=true to check dimensions
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, options);
                Log.d("", "decode from url options.outWidth  ....." + options.outWidth);
                Log.d("", "decode from url options.outHeight  ....." + options.outHeight);

                inputStream.getChannel().position(0);
            } else {
                Log.d("", "as we know the bitmap info.... bitmap width= " + bitmapWidth + " height= " + bitmapHeight);
                options.outWidth = bitmapWidth;
                options.outHeight = bitmapHeight;
            }

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, width, height);
            //  options.inSampleSize = 3;
            Log.d("", "decode from url   options.inSampleSize  ....." + options.inSampleSize);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inScaled = false;

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            if (bitmap != null) {
                Log.d("", "decodeBitmap end .....bitmap width= " + bitmap.getWidth() + " height= " + bitmap.getHeight());
            }
            return bitmap;
        } catch (Exception ignored) {
            return null;
        }
    }

    //Given the bitmap size and View size calculate a subsampling size (powers of 2)
    public static int calculateInSampleSize(BitmapFactory.Options options, int width, int height) {
        float ratioWidth = width > 0 ? 1.0f * width / options.outWidth : 1.0f;
        float ratioHeight = height > 0 ? 1.0f * height / options.outHeight : 1.0f;
        float ratio = Math.max(ratioWidth, ratioHeight);
        int inSampleSize = ratio < 1.0f && ratio != 0 ? (int) (1.0f / ratio - 0.5f) : 1;
        Log.d("", "BcalculateInSampleSize inSampleSize= " + inSampleSize);
        return inSampleSize;
    }

    //Given the bitmap size and View size calculate a subsampling size (powers of 2)
    public static int calculateInSampleSize1(BitmapFactory.Options options, int width, int height) {
        int inSampleSize = 1;   //Default subsampling size
        // See if image raw height and width is bigger than that of required view
        if (options.outHeight > height || options.outWidth > width) {
            //bigger
            final int halfHeight = options.outHeight / 2;
            final int halfWidth = options.outWidth / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > height && (halfWidth / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
