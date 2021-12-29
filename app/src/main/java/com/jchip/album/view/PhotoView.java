package com.jchip.album.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import androidx.core.content.res.ResourcesCompat;

import com.jchip.album.R;
import com.jchip.album.common.ImageHelper;
import com.jchip.album.common.NinePatchHelper;
import com.jchip.album.data.PhotoData;

import java.util.Arrays;
import java.util.List;

public class PhotoView {
    public static final int DEFAULT_FRAME_ID = R.drawable.frame_item_0;
    public static final int DEFAULT_PHOTO_ID = R.drawable.photo_default;

    public static final int DEFAULT_FONT_INDEX = 1;
    public static final int DEFAULT_FONT_LOCATION = Gravity.CENTER;
    public static final int DEFAULT_FONT_SIZE = 120;
    public static final int DEFAULT_FONT_COLOR = 0xFFFFFFFF;

    public static final int LAYER_ALBUM_PHOTO = 0;
    public static final int LAYER_FRAME_SETTING = 1;
    public static final int LAYER_FONT_SETTING = 2;

    public static final int WIDGET_ALBUM_PHOTO = 3;
    public static final int WIDGET_ALBUM_SETTING = 4;
    public static final int WIDGET_PHOTO_SETTING = 5;

    public static final ScaleType[] scaleTypes = {
            ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_XY, ImageView.ScaleType.CENTER
    };

    public static final List<Integer> fonts = Arrays.asList(
            R.font.niconne_regular, R.font.anton_regular, R.font.macondo_egular,
            R.font.abril_fatface_regular, R.font.ole_regular, R.font.wind_song_medium);

    private final Context context;
    private final PhotoData photo;
    private final int layer;

    public PhotoView(Context context, PhotoData photo, int layer) {
        this.context = context;
        this.photo = photo;
        this.layer = layer;
    }

    public Drawable getFrameDrawable() {
        Log.d("", " this.getLayer()===" + this.layer);
        Log.d("", " this.getImageGap()===" + this.getImageGap());
        Log.d("", " this.getImageDensitySize()===" + this.getFrameDensitySize());
        Log.d("", " this.getImageMaxWidth()===" + this.getImageMaxWidth());
        Log.d("", " this.getImageMaxHeight()===" + this.getImageMaxHeight());
        //Log.d("", " this.getImageMaxSize()===" + this.getImageMaxSize());
        Log.d("", " this.getScreenWidth()===" + PhotoViewConfig.getScreenWidth());
        Log.d("", " this.getScreenHeight()===" + PhotoViewConfig.getScreenHeight());
        Log.d("", " ---------");

        // x 40 to change density
        int densitySize = this.getFrameDensitySize();
        return NinePatchHelper.getImageDrawable(context, this.getFrameIndex(), densitySize);
    }

    public ScaleType getPhotoScale() {
        return scaleTypes[photo.getScaleIndex()];
    }

    public Bitmap getPhotoImage() {
        return this.loadPhotoImage();
    }

    private Bitmap loadPhotoImage() {
        Bitmap bitmap = null;
        if (photo.getPhotoPath() != null && !photo.getPhotoPath().trim().isEmpty()) {
            //bitmap = ImageHelper.loadBitmap(photo.getPhotoPath(), false);
            bitmap = ImageHelper.decodeBitmap(photo.getPhotoPath(), this.getImageMaxWidth(), this.getImageMaxHeight());
        }
        if (bitmap != null) {
            bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), 0, 0);
            //bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), this.getImageMaxWidth(), this.getImageMaxHeight());
            //bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), this.getImageMaxSize(), this.getImageMaxSize());
        }
        if (bitmap == null) {
            return ImageHelper.loadBitmap(context.getResources(), DEFAULT_PHOTO_ID, false);
            // return ImageHelper.decodeResource(context.getResources(), DEFAULT_PHOTO_ID, this.getImageMaxSize(), this.getImageMaxSize());
        }
        return bitmap;
    }


    public boolean isFontEmpty() {
        return this.getFontText() == null || this.getFontText().trim().isEmpty();
    }

    public boolean isImageOn() {
        return layer != LAYER_FRAME_SETTING;
    }

    public boolean isFrameOn() {
        return true;
    }

    public boolean isFontOn() {
        return true;
    }


    public List<Integer> getFonts() {
        return this.fonts;
    }

    public int getFontIndex() {
        int fontIndex = this.fonts.indexOf(photo.getFontType());
        return fontIndex < 0 ? DEFAULT_FONT_INDEX : fontIndex;
    }

    public Typeface getFontFaceType() {
        try {
            int fontIndex = this.getFontIndex();
            return ResourcesCompat.getFont(context, this.fonts.get(fontIndex));
        } catch (Exception ignored) {
        }
        return null;
    }

    public void setScaleIndex(int scaleIndex) {
        photo.setScaleIndex(scaleIndex);
    }

    public int getScaleIndex() {
        return photo.getScaleIndex();
    }

    public int getFrameIndex() {
        return photo.getFrameIndex() > 0 ? photo.getFrameIndex() : DEFAULT_FRAME_ID;
    }

    public int getFontLocation() {
        return photo.getFontLocation() >= 0 ? photo.getFontLocation() : DEFAULT_FONT_LOCATION;
    }

    public String getFontText() {
        return photo.getFontText();
    }

    public int getFontColor() {
        return photo.getFontColor();
    }

    public int getFontSize() {
        return photo.getFontSize() > 0 ? photo.getFontSize() : DEFAULT_FONT_SIZE;
    }

    public int getFrameDensitySize() {
        return PhotoViewConfig.getDensitySizeFactor(layer);
    }

    public int getFontTextSize() {
        return (int) (PhotoViewConfig.getFontSizeFactor(layer) * this.getFontSize());
    }

    public int getImageGap() {
        return ScaleType.FIT_CENTER != this.getPhotoScale() ? 0 : PhotoViewConfig.getImageGap(layer);
    }

    public int getImageMaxWidth() {
        return PhotoViewConfig.getImageMaxWidth(layer);
    }

    public int getImageMaxHeight() {
        return PhotoViewConfig.getImageMaxHeight(layer);
    }

//    public int getImageMaxSize() {
//        return PhotoViewConfig.getImageMaxSize(layer);
//    }
}
