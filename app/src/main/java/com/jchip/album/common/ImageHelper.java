package com.jchip.album.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.jchip.album.R;

import java.io.IOException;

public class ImageHelper {
    private Bitmap bmp;
    private Canvas canvas;
    private Paint paint;
    private ImageView imageShow;

    //图片合成1
    private void addFrameToImage(Bitmap bm) //bmp原图(前景) bm资源图片(背景)
    {
        Bitmap drawBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        canvas = new Canvas(drawBitmap);
        paint = new Paint();
        canvas.drawBitmap(bmp, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.LIGHTEN));
        //对边框进行缩放
        int w = bm.getWidth();
        int h = bm.getHeight();
        //缩放比 如果图片尺寸超过边框尺寸 会自动匹配
        float scaleX = bmp.getWidth() * 1F / w;
        float scaleY = bmp.getHeight() * 1F / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        Bitmap copyBitmap = Bitmap.createBitmap(bm, 0, 0, w, h, matrix, true);
        canvas.drawBitmap(copyBitmap, 0, 0, paint);
        imageShow.setImageBitmap(drawBitmap);
    }

    public static Bitmap resize(Context context, int faceWidth, Bitmap srcImg) throws IOException {

        //Decode *.png file to Bitmap
        Bitmap Bitmap_temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.album_button);
        Bitmap Bitmap_final = Bitmap_temp.copy(android.graphics.Bitmap.Config.ARGB_8888, true);

        //Get Pixel and change color if pixel color match
        int[] allpixels = new int[Bitmap_final.getHeight() * Bitmap_final.getWidth()];
        Bitmap_final.getPixels(allpixels, 0, Bitmap_final.getWidth(), 0, 0, Bitmap_final.getWidth(), Bitmap_final.getHeight());
        for (int i = 0; i < allpixels.length; i++) {
            if (allpixels[i] == Color.parseColor("#fff000")) {
                allpixels[i] = Color.parseColor("#0D0D0D");
            }
        }
        Bitmap_final.setPixels(allpixels, 0, Bitmap_final.getWidth(), 0, 0, Bitmap_final.getWidth(), Bitmap_final.getHeight());

        //Set Bitmap to ImageView
        // imageShow.setImageBitmap(Bitmap_final);

        return null;
    }


    //图片合成
    private Bitmap addFrameToImageTwo(Bitmap frameBitmap) //bmp原图 frameBitmap资源图片(边框)
    {
        //bmp原图 创建新位图
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap drawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //对边框进行缩放
        int w = frameBitmap.getWidth();
        int h = frameBitmap.getHeight();
        float scaleX = width * 1F / w;
        float scaleY = height * 1F / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        Bitmap copyBitmap = Bitmap.createBitmap(frameBitmap, 0, 0, w, h, matrix, true);

        int pixColor = 0;
        int layColor = 0;
        int newColor = 0;

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixA = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int newA = 0;

        int layR = 0;
        int layG = 0;
        int layB = 0;
        int layA = 0;

        float alpha = 0.8F;
        float alphaR = 0F;
        float alphaG = 0F;
        float alphaB = 0F;

        for (int i = 0; i < width; i++) {
            for (int k = 0; k < height; k++) {
                pixColor = bmp.getPixel(i, k);
                layColor = copyBitmap.getPixel(i, k);
                // 获取原图片的RGBA值
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                pixA = Color.alpha(pixColor);
                // 获取边框图片的RGBA值
                layR = Color.red(layColor);
                layG = Color.green(layColor);
                layB = Color.blue(layColor);
                layA = Color.alpha(layColor);
                // 颜色与纯黑色相近的点
                if (layR < 20 && layG < 20 && layB < 20) {
                    alpha = 1F;
                } else {
                    alpha = 0.3F;
                }
                alphaR = alpha;
                alphaG = alpha;
                alphaB = alpha;
                // 两种颜色叠加
                newR = (int) (pixR * alphaR + layR * (1 - alphaR));
                newG = (int) (pixG * alphaG + layG * (1 - alphaG));
                newB = (int) (pixB * alphaB + layB * (1 - alphaB));
                layA = (int) (pixA * alpha + layA * (1 - alpha));
                // 值在0~255之间
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                newA = Math.min(255, Math.max(0, layA));
                //绘制
                newColor = Color.argb(newA, newR, newG, newB);
                drawBitmap.setPixel(i, k, newColor);
            }
        }
        return drawBitmap;
    }

    //生成圆角图片
    private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap roundBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        int color = 0xff424242;
        Paint paint = new Paint();
        //设置圆形半径
        int radius;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            radius = bitmap.getHeight() / 2;
        } else {
            radius = bitmap.getWidth() / 2;
        }
        //绘制圆形
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return roundBitmap;
    }

    private Bitmap getRoundedCornerBitmap2(Bitmap bitmap) {
        //绘制圆角矩形
        Bitmap roundBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = 80;     //转角设置80
        //绘制
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return roundBitmap;
    }


    public static Bitmap getBitmapWithTransparentBG(Bitmap srcBitmap, int bgColor) {
        Bitmap result = srcBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int nWidth = result.getWidth();
        int nHeight = result.getHeight();
        for (int y = 0; y < nHeight; ++y)
            for (int x = 0; x < nWidth; ++x) {
                int nPixelColor = result.getPixel(x, y);
                if (nPixelColor == bgColor)
                    result.setPixel(x, y, Color.TRANSPARENT);
            }
        return result;
    }

    public static Bitmap getBitmapWithTransparentBG2(Context context, Bitmap srcBitmap, int bgColor) {

        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(
                context.getResources().getColor(R.color.black),
                PorterDuff.Mode.MULTIPLY
        );
        //imgView.getDrawable().setColorFilter(porterDuffColorFilter);
        // imgView.setBackgroundColor(Color.TRANSPARENT);
        return null;
    }

    //使图片透明的方法
    public Bitmap makeTheImageTransparent(Bitmap bitmap) {
        bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_4444);
        bitmap.eraseColor(Color.TRANSPARENT);
        return bitmap;
    }

    public Bitmap transparentImage(Bitmap bmp) {
        //int m_ImageWidth, m_ImageHeigth;
        int m_ImageWidth = bmp.getWidth();
        int m_ImageHeigth = bmp.getHeight();
        int[] m_BmpPixel = new int[m_ImageWidth * m_ImageHeigth];
        bmp.getPixels(m_BmpPixel, 0, m_ImageWidth, 0, 0, m_ImageWidth,
                m_ImageHeigth);
        for (int i = 0; i < m_ImageWidth * m_ImageHeigth; i++) {
            if ((m_BmpPixel[i] & 0x00ffffff) == 0x00ff0000) {
                m_BmpPixel[i] = 0x00000000;
            }
        }
        bmp.setPixels(m_BmpPixel, 0, m_ImageWidth, 0, 0, m_ImageWidth,
                m_ImageHeigth);
        return bmp;
    }

    public void doMatrix() {
        //imageView.setScaleType(ImageView.ScaleType.MATRIX);  //设置为矩阵模式

        Matrix matrix = new Matrix();           //创建一个单位矩阵
        matrix.setTranslate(100, 100);          //平移x和y各100单位
        matrix.preRotate(30);                   //顺时针旋转30度
        //imageView.setImageMatrix(matrix);

        //Draw9Patch.jar

    }
}
