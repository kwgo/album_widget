package com.jchip.album.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
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

    /**
     * Creates and returns a new bitmap containing the given text.
     */
    public static Bitmap convertTextBitmapa(String text, Typeface typeface, float size, int colour, Paint.Align align) {
        final TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(typeface);
        textPaint.setTextSize(size);
        //paint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setSubpixelText(true);
        textPaint.setColor(colour);
        textPaint.setTextAlign(Paint.Align.LEFT);
        Bitmap bitmap = Bitmap.createBitmap((int) textPaint.measureText(text), (int) size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, 0, bitmap.getHeight(), textPaint);
        return bitmap;
    }

    public static Bitmap convertTextBitmap(String text, Typeface typeface, float size, int colour, Paint.Align align) {
        Bitmap myBitmap = Bitmap.createBitmap(160, 84, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(typeface);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(65);
        paint.setTextAlign(Paint.Align.CENTER);
        myCanvas.drawText(text, 80, 60, paint);
        return myBitmap;
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        Bitmap roundBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        int color = 0xff424242;
        Paint paint = new Paint();
        int radius;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            radius = bitmap.getHeight() / 2;
        } else {
            radius = bitmap.getWidth() / 2;
        }
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, paint);
        //canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return roundBitmap;
    }

    public static Bitmap getTransparentImage(Bitmap bitmap) {
        bitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);
        return bitmap;
    }
}
