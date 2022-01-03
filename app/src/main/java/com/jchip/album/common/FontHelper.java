package com.jchip.album.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

public class FontHelper {
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public static Bitmap getTextBitmap(String text, Typeface font, int textSize, int color, Layout.Alignment alignment) {
        // new anti aliased Paint
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setTypeface(font);
        // paint.setShadowLayer(1f, 0f, 1f,  color & 0xFAFFFFFF);

        int textWidth = getTextWidth(paint, text);
        // init StaticLayout for text
        StaticLayout textLayout = new StaticLayout(text, paint, textWidth, alignment, 0.8f, 0.0f, false);
        // get height of multiline text
        int textHeight = textLayout.getHeight();
        // init a bitmap
        Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);
        // get position of text's top left corner
        Canvas canvas = new Canvas(bitmap);
        canvas.save();
        canvas.translate(0, 0);
        textLayout.draw(canvas);
        canvas.restore();
        return bitmap;
    }

    public static Bitmap drawTextOnBitmap(Bitmap bitmap, float scale, String text, Layout.Alignment alignment) {
        //float scale = resources.getDisplayMetrics().density;

        Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) { // set default bitmap config if none
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        // prepare canvas
        Canvas canvas = new Canvas(bitmap);
        // new anti aliased Paint
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (14 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        // set text width to canvas width minus 16dp padding
        int textWidth = canvas.getWidth() - (int) (16 * scale);
        // init StaticLayout for text
        StaticLayout textLayout = new StaticLayout(text, paint, textWidth, alignment, 1.0f, 0.0f, false);
        // get height of multiline text
        int textHeight = textLayout.getHeight();
        // get position of text's top left corner
        float x = (bitmap.getWidth() - textWidth) / 2;
        float y = (bitmap.getHeight() - textHeight) / 2;
        // draw text to the Canvas center
        canvas.save();
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.restore();
        return bitmap;
    }

}
