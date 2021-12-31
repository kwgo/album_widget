package com.jchip.album.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import java.io.FileInputStream;

public class ImageHelper {
    public static Bitmap convertBitmap(Bitmap bitmap, float scale, int fit, int rotation, int flip, float width, float height) {
        Log.d("", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ " + scale);
        Log.d("", "new convertBitmap fit= " + fit);
        float pointX = 0;
        float pointY = 0;

        float imageWidth = bitmap.getWidth();
        float imageHeight = bitmap.getHeight();
        float scaleWidth = scale;
        float scaleHeight = scale;

        Log.d("", "new convert bitmap - width= " + imageWidth + " height= " + imageHeight + " View Width== " + width + " Height== " + height);
        if (width > 0 && height > 0) {
            scaleWidth = scale * width / imageWidth;
            scaleHeight = scale * height / imageHeight;
            float minScale = Math.min(scaleWidth, scaleHeight);
            float maxScale = Math.max(scaleWidth, scaleHeight);
            Log.d("", " in middle fit=" + fit + " 0 minScale= " + minScale + " maxScale= " + maxScale);
            if (fit == 0) {
                Log.d("", "start fit==0");
                scaleWidth = scaleHeight = maxScale;
                imageWidth = imageWidth * scaleWidth;
                imageHeight = imageHeight * scaleHeight;
                Log.d("", " in middle fit=0 1 imageWidth= " + imageWidth + " imageHeight= " + imageHeight);
                pointX = imageWidth > width ? Math.abs(imageWidth - width) / 2 : 0;
                pointY = imageHeight > height ? Math.abs(imageHeight - height) / 2 : 0;
                Log.d("", " in middle fit=0 2 pointX= " + pointX + " pointY= " + pointY);
                imageWidth = (imageWidth > width ? width : imageWidth) / scaleWidth;
                imageHeight = (imageHeight > height ? height : imageHeight) / scaleHeight;
                Log.d("", " in middle fit=0 3 imageWidth= " + imageWidth + " imageHeight= " + imageHeight);
            } else if (fit == 1) {
                Log.d("", "start fit==1");
                scaleWidth = scaleHeight = minScale;
                imageWidth = imageWidth * scaleWidth;
                imageHeight = imageHeight * scaleHeight;
                Log.d("", " in middle fit=1 1 imageWidth= " + imageWidth + " imageHeight= " + imageHeight);
                pointX = imageWidth > width ? Math.abs(imageWidth - width) / 2 : 0;
                pointY = imageHeight > height ? Math.abs(imageHeight - height) / 2 : 0;
                Log.d("", " in middle fit=1 2 pointX= " + pointX + " pointY= " + pointY);
                imageWidth = (imageWidth > width ? width : imageWidth) / scaleWidth;
                imageHeight = (imageHeight > height ? height : imageHeight) / scaleHeight;
                Log.d("", " in middle fit=1 3 pointX= " + pointX + " pointY= " + pointY);
            } else if (fit == 2) {
                Log.d("", "start fit==2");
            } else if (fit == 3) {
                Log.d("", "start fit==3");
                scaleWidth = scaleHeight = scale;

                imageWidth = imageWidth * scaleWidth;
                imageHeight = imageHeight * scaleHeight;
                Log.d("", " in middle fit=3 1 imageWidth= " + imageWidth + " imageHeight= " + imageHeight);
                float newWidth = Math.min(imageWidth, width);
                float newHeight = Math.min(imageHeight, height);
                Log.d("", " in middle fit=3 2 newWidth= " + newWidth + " newHeight= " + newHeight);

                pointX = newWidth == width ? Math.abs(imageWidth - width) / 2 : 0;
                pointY = newHeight == height ? Math.abs(imageHeight - height) / 2 : 0;

                imageWidth = newWidth / scaleWidth;
                imageHeight = newHeight / scaleHeight;
                Log.d("", " in middle fit=3 3 imageWidth= " + imageWidth + " imageHeight= " + imageHeight);

            }
        }
        scaleWidth = flip == 0 ? scaleWidth : -scaleWidth;
        Log.d("", " new bitmap  scaleWidth= " + scaleWidth + " and scaleHeight= " + scaleHeight);

        Matrix matrix = new Matrix();
        matrix.postRotate(rotation * 90);
        matrix.postScale(scaleWidth, scaleHeight);

        width = width > 0 ? width : imageWidth;
        height = height > 0 ? height : imageHeight;

        Log.d("", " new bitmap with x= " + pointX + " y= " + pointY);
        Log.d("", " new bitmap with width= " + width + " height= " + height);
        Log.d("", " new bitmap with imageWidth= " + imageWidth + " imageHeight= " + imageHeight);
        Log.d("", " new bitmap width = " + width + " and height= " + height + " and scaleWidth= " + scaleWidth + " and scaleHeight= " + scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, (int) pointX, (int) pointY, (int) imageWidth, (int) imageHeight, matrix, true);

        Log.d("", "end new converted bitmap - width= " + bitmap.getWidth() + " height= " + bitmap.getHeight());
        Log.d("", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ END ");
        return bitmap;
    }

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

    public static Bitmap decodeBitmap(Resources resources, int imageId, Rect imageRect) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            Log.d("", "decode bitmap from resource start .... imageId= " + imageId);
            Log.d("", "decode bitmap from resource start .... view rect = " + imageRect);
            if (!isNeedInSampleSize(imageRect)) {
                Log.d("", "decode bitmap from resource do not need InSampleSize .... imageId= " + imageId);
                return BitmapFactory.decodeResource(resources, imageId);
            }
            Log.d("", "decode bitmap from resource we do need InSampleSize .... imageId= " + imageId);
            if (imageRect.left <= 0 || imageRect.top <= 0) {
                // First decode with inJustDecodeBounds=true to check dimensions
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(resources, imageId, options);
                imageRect.left = options.outWidth;
                imageRect.top = options.outHeight;
            } else {
                Log.d("", "decode bitmap from resource as we know the bitmap info.... bitmap width= " + imageRect.right + " height= " + imageRect.bottom);
                options.outWidth = imageRect.left;
                options.outHeight = imageRect.top;
            }
            Log.d("", "decode bitmap from resource options.outWidth  ....." + options.outWidth);
            Log.d("", "decode bitmap from resource options.outHeight  ....." + options.outHeight);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(imageRect);
            options.inSampleSize = 1;
            //  options.inSampleSize = 3;
            Log.d("", "decode bitmap from resource options.inSampleSize  ....." + options.inSampleSize);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inScaled = false;

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

    public static boolean isNeedInSampleSize(Rect rect) {
        return (rect.left <= 0 && rect.top <= 0 && rect.right > 0 && rect.bottom > 0)
                || ((rect.left > 0 && rect.top > 0 && rect.right > 0 && rect.bottom > 0)
                && (rect.left > rect.right && rect.top > rect.bottom));
    }

    public static boolean isNeedCalculateInSampleSize(Rect rect) {
        return (rect.left > 0 && rect.top > 0 && rect.right > 0 && rect.bottom > 0)
                && (rect.left > rect.right && rect.top > rect.bottom);
    }

    public static int calculateInSampleSize(Rect rect) {
        int inSampleSize = 1;
        if (isNeedCalculateInSampleSize(rect)) {

            Log.d("", "calculateInSampleSize image width = " + rect.left);
            Log.d("", "calculateInSampleSize image height = " + rect.top);
            Log.d("", "calculateInSampleSize view width = " + rect.right);
            Log.d("", "calculateInSampleSize view height = " + rect.bottom);

            float ratioWidth = rect.right > 0 ? 1.0f * rect.right / rect.left : 1.0f;
            float ratioHeight = rect.bottom > 0 ? 1.0f * rect.bottom / rect.top : 1.0f;
            Log.d("", "before ratioWidth = " + ratioWidth);
            Log.d("", "before ratioHeight = " + ratioHeight);
            float ratio = Math.max(ratioWidth, ratioHeight);
            Log.d("", "before ratio = " + ratio);
            inSampleSize = ratio > 0f && ratio < 1.0 ? (int) (1.0f / ratio + 0.5) : 1;
            Log.d("", "before inSampleSize ++ ++ ++= " + inSampleSize);
//        if (options.outWidth * options.outHeight / inSampleSize / inSampleSize > width * height) {
//            Log.d("", "BcalculateInSampleSize inSampleSize ++ ++ ++= " + inSampleSize);
//            inSampleSize++;
//        }
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

    public static Bitmap decodeBitmap(String url, int bitmapWidth, int bitmapHeight, int width,
                                      int height) {
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
}
