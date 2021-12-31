package com.jchip.album.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.FileInputStream;

public class ImageHelper {
    public static Bitmap convertBitmap(Bitmap bitmap, float scale, int fit, int rotation, int flip, int width, int height) {
        int imageWidth = bitmap.getWidth(), imageHeight = bitmap.getHeight();

        int px = 0, py = 0;
        int newWidth = imageWidth, newHeight = imageHeight;

        float scaleWidth = scale, scaleHeight = scale;

        Log.d("", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ fit = " + fit);
        Log.d("", "convert bitmap width= " + imageWidth + " height= " + imageHeight + " to view width= " + width + " height= " + height);
        if (width > 0 && height > 0) {
            scaleWidth = scaleWidth * width / imageWidth;
            scaleHeight = scaleHeight * height / imageHeight;
            Log.d("", " convert scaleWidth= " + scaleWidth + " scaleHeight= " + scaleHeight);
            if (fit == 0) { // centerCrop
                scaleWidth = scaleHeight = Math.max(scaleWidth, scaleHeight);
            } else if (fit == 1) { // fitCenter
                scaleWidth = scaleHeight = Math.min(scaleWidth, scaleHeight);
//          } else if (fit == 2) { // fitXY
            } else if (fit == 3) { // center
                scaleWidth = scaleHeight = scale;
            }
            newWidth = Math.min((int) (1.0f * width / scaleWidth), imageWidth);
            newHeight = Math.min((int) (1.0f * height / scaleHeight), imageHeight);
            px = (imageWidth - newWidth) / 2;
            py = (imageHeight - newHeight) / 2;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation * 90);
        matrix.postScale(flip == 0 ? scaleWidth : -scaleWidth, scaleHeight);

        Log.d("", " convert by scaleWidth= " + scaleWidth + " scaleHeight= " + scaleHeight);
        Log.d("", " convert to width= " + width + " height= " + height);

        Log.d("", " convert from point px= " + px + " py= " + py);
        Log.d("", " convert to size newWidth= " + newWidth + " newHeight= " + newHeight);

        try {
            bitmap = Bitmap.createBitmap(bitmap, px, py, newWidth, newHeight, matrix, true);
            Log.d("", "new converted bitmap with width= " + bitmap.getWidth() + " height= " + bitmap.getHeight());
        } catch (Exception ignore) {
        }
        Log.d("", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ END " + fit);
        return bitmap;
    }

    public static Bitmap decodeBitmap(Resources resources, int imageId, int imageWidth, int imageHeight, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            Log.d("", "decode bitmap from resource imageId= " + imageId);
            Log.d("", "decode bitmap width= " + imageWidth + " height= " + imageHeight + " to view width= " + width + " height= " + height);
            if (!isNeedInSampleSize(imageWidth, imageHeight, width, height)) {
                Log.d("", "decode bitmap from resource do NOT need InSampleSize.");
                return BitmapFactory.decodeResource(resources, imageId);
            }
            if (imageWidth <= 0 || imageHeight <= 0) {
                Log.d("", "decode bitmap from resource we do need InSampleSize from resource.");
                options.inJustDecodeBounds = true; // First decode with inJustDecodeBounds=true to check dimensions
                BitmapFactory.decodeResource(resources, imageId, options);
                imageWidth = options.outWidth;
                imageHeight = options.outHeight;
            } else {
                Log.d("", "decode bitmap as we known the bitmap info width= " + imageWidth + " height= " + imageHeight);
                options.outWidth = imageWidth;
                options.outHeight = imageHeight;
            }
            Log.d("", "decode bitmap from resource options.outWidth  ....." + options.outWidth + " options.outHeight  ....." + options.outHeight);

            options.inSampleSize = calculateInSampleSize(imageWidth, imageHeight, width, height);
            Log.d("", "decode bitmap from resource options.inSampleSize  ....." + options.inSampleSize);

            options.inScaled = false;
            options.inJustDecodeBounds = false;  // Decode bitmap with inSampleSize set
            Bitmap bitmap = BitmapFactory.decodeResource(resources, imageId, options);
            if (bitmap != null) {
                Log.d("", "decode bitmap from resource rect end .....bitmap width= " + bitmap.getWidth() + " height= " + bitmap.getHeight());
            }
            return bitmap;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeBitmap(String imageUrl, int imageWidth, int imageHeight, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        try (FileInputStream inputStream = new FileInputStream(imageUrl)) {
            Log.d("", "decode bitmap from file url= " + imageUrl);
            Log.d("", "decode bitmap width= " + imageWidth + " height= " + imageHeight + " to view width= " + width + " height= " + height);
            if (!isNeedInSampleSize(imageWidth, imageHeight, width, height)) {
                Log.d("", "decode bitmap from file url do NOT need InSampleSize.");
                return BitmapFactory.decodeStream(inputStream);
            }
            if (imageWidth <= 0 || imageHeight <= 0) {
                Log.d("", "decode bitmap from resource we do need InSampleSize from resource.");
                options.inJustDecodeBounds = true; // First decode with inJustDecodeBounds=true to check dimensions
                BitmapFactory.decodeStream(inputStream, null, options);
                imageWidth = options.outWidth;
                imageHeight = options.outHeight;
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
            options.inJustDecodeBounds = false;  // Decode bitmap with inSampleSize set
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            if (bitmap != null) {
                Log.d("", "decode bitmap from resource rect end .....bitmap width= " + bitmap.getWidth() + " height= " + bitmap.getHeight());
            }
            return bitmap;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    public static boolean isNeedInSampleSize(int imageWidth, int imageHeight, int width, int height) {
        return (imageWidth <= 0 && imageHeight <= 0 && width > 0 && height > 0)
                || ((imageWidth > 0 && imageHeight > 0 && width > 0 && height > 0)
                && (imageWidth > width && imageHeight > height));
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
            inSampleSize = ratio > 0f && ratio < 1.0 ? (int) (1.0f / ratio + 0.5) : 1;
            Log.d("", "before inSampleSize ++ ++ ++= " + inSampleSize);
        }
        Log.d("", "calculated inSampleSize= " + inSampleSize);
        return inSampleSize;
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
/*


    public static Bitmap decodeBitmap(String url, int bitmapWidth, int bitmapHeight, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        try (FileInputStream inputStream = new FileInputStream(url)) {
            Log.d("", "decode from url start .... url= " + url);
            Log.d("", "decode from url start .... require width= " + width + " height= " + height);
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
            ignored.printStackTrace();
            return null;
        }
    }

    //Given the bitmap size and View size calculate a subsampling size (powers of 2)
    public static int calculateInSampleSize(BitmapFactory.Options options, int width, int height) {
        Log.d("", "calculateInSampleSize options.outWidth = " + options.outWidth);
        Log.d("", "calculateInSampleSize options.outHeight = " + options.outHeight);
        float ratioWidth = width > 0 ? 1.0f * width / options.outWidth : 1.0f;
        float ratioHeight = height > 0 ? 1.0f * height / options.outHeight : 1.0f;
        Log.d("", "before ratioWidth  ++= " + ratioWidth);
        Log.d("", "before ratioHeight  ++= " + ratioHeight);
        float ratio = Math.max(ratioWidth, ratioHeight);
        Log.d("", "before ratio  ++= " + ratio);
        int inSampleSize = ratio > 0f && ratio < 1.0 ? (int) (1.0f / ratio + 0.5) : 1;
        Log.d("", "before inSampleSize ++ ++ ++= " + inSampleSize);
//        if (options.outWidth * options.outHeight / inSampleSize / inSampleSize > width * height) {
//            Log.d("", "BcalculateInSampleSize inSampleSize ++ ++ ++= " + inSampleSize);
//            inSampleSize++;
//        }
        Log.d("", "BcalculateInSampleSize inSampleSize= " + inSampleSize);
        return inSampleSize;
    }

    //Given the bitmap size and View size calculate a subsampling size (powers of 2)
    public static int calculateInSampleSize1(BitmapFactory.Options options, int width,
                                             int height) {
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

 */
}
