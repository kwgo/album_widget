package com.jchip.album.view;

import android.content.Context;
import android.content.res.Resources;
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
        //Log.d("", " this.getLayer()===" + this.getLayer());
        Log.d("", " this.getImageGap()===" + this.getImageGap());
        Log.d("", " this.getImageDensitySize()===" + this.getFrameDensitySize());

        Log.d("", " this.getImageMaxSize()===" + this.getImageMaxSize());
        Log.d("", " this.getScreenWidth()===" + this.getScreenWidth());
        Log.d("", " this.getScreenHeight()===" + this.getScreenHeight());

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
            bitmap = ImageHelper.loadBitmap(photo.getPhotoPath(), false);
        }
        if (bitmap == null) {
            bitmap = ImageHelper.loadBitmap(context.getResources(), DEFAULT_PHOTO_ID, false);
        }
        if (bitmap != null) {
            return ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), this.getImageMaxSize());
        }
        return null;
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

    public int getImageMaxSize() {
        return layer == LAYER_ALBUM_PHOTO ? (int) (0.64 * this.getScreenHeight()) :
                layer == LAYER_FRAME_SETTING ? this.getScreenWidth() :
                        layer == LAYER_FONT_SETTING ? this.getScreenWidth() :
                                layer == WIDGET_ALBUM_PHOTO ? (int) (0.70 * this.getScreenHeight()) :
                                        layer == WIDGET_ALBUM_SETTING ? this.getScreenWidth() / 5 :
                                                layer == WIDGET_PHOTO_SETTING ? this.getScreenWidth() / 2 : 0;
    }

    public int getImageGap() {
        return ImageView.ScaleType.FIT_CENTER != this.getPhotoScale() ? 0 :
                layer == LAYER_ALBUM_PHOTO ? 16 :
                        layer == LAYER_FRAME_SETTING ? 8 :
                                layer == LAYER_FONT_SETTING ? 8 :
                                        layer == WIDGET_ALBUM_PHOTO ? 12 :
                                                layer == WIDGET_ALBUM_SETTING ? 2 :
                                                        layer == WIDGET_PHOTO_SETTING ? 4 : 0;
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
        return layer == LAYER_ALBUM_PHOTO ? 1 :
                layer == LAYER_FRAME_SETTING ? 4 :
                        layer == LAYER_FONT_SETTING ? 4 :
                                layer == WIDGET_ALBUM_PHOTO ? 0 :
                                        layer == WIDGET_ALBUM_SETTING ? 25 :
                                                layer == WIDGET_PHOTO_SETTING ? 12 : 0;
    }

    public int getFontTextSize() {
        float factor = layer == LAYER_ALBUM_PHOTO ? 0.75f :
                layer == LAYER_FRAME_SETTING ? 0.45f :
                        layer == LAYER_FONT_SETTING ? 0.45f :
                                layer == WIDGET_ALBUM_PHOTO ? 1.0f :
                                        layer == WIDGET_ALBUM_SETTING ? 0.1f :
                                                layer == WIDGET_PHOTO_SETTING ? 0.21f : 1.0f;
        return (int) (factor * this.getFontSize());
    }

    public int getScreenWidth() {
        try {
            return Resources.getSystem().getDisplayMetrics().widthPixels;
        } catch (Exception ex) {
            return 0;
        }
    }

    public int getScreenHeight() {
        try {
            return Resources.getSystem().getDisplayMetrics().heightPixels;
        } catch (Exception ex) {
            return 0;
        }
    }
}
