package com.jchip.album.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import androidx.core.content.res.ResourcesCompat;

import com.jchip.album.R;
import com.jchip.album.common.ImageHelper;
import com.jchip.album.common.NinePatchHelper;
import com.jchip.album.data.PhotoData;

public class PhotoView {
    public static final int DEFAULT_FRAME_ID = R.drawable.frame_item_0;
    public static final int DEFAULT_PHOTO_ID = R.drawable.photo_default;

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

    private final Context context;
    private final PhotoData photo;
    private final int layer;

    public PhotoView(Context context, PhotoData photo, int layer) {
        this.context = context;
        this.photo = photo;
        this.layer = layer;
    }

    public Drawable getFrameDrawable() {
        Log.d("", " this.getLayer()===" + this.getLayer());
        Log.d("", " this.getImageGap()===" + this.getImageGap());
        Log.d("", " this.getImageDensitySize()===" + this.getImageDensitySize());

        Log.d("", " this.getImageMaxSize()===" + this.getImageMaxSize());
        Log.d("", " this.getScreenWidth()===" + this.getScreenWidth());
        Log.d("", " this.getScreenHeight()===" + this.getScreenHeight());

        int frameId = photo.getFrameIndex() > 0 ? photo.getFrameIndex() : DEFAULT_FRAME_ID;
        // x 40 to change density
        int densitySize = this.getImageDensitySize();
        return NinePatchHelper.getImageDrawable(context, frameId, densitySize);
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
            bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), this.getImageMaxSize());
        }
        return bitmap;
    }

    public Typeface getPhotoFontType() {
        try {
            return ResourcesCompat.getFont(context, photo.getFontType());
        } catch (Exception ignored) {
        }
        return null;
    }

    public boolean isImageOn() {
        return layer != LAYER_FRAME_SETTING;
    }

    public boolean isFrameOn() {
        return true;
    }

    public boolean isLabelOn() {
        return layer == WIDGET_ALBUM_SETTING || layer == WIDGET_PHOTO_SETTING;
    }

    public int getLayer() {
        return layer;
    }

    public int getImageMaxSize() {
        return layer == LAYER_ALBUM_PHOTO ? (int) (0.64 * this.getScreenHeight()) :
                layer == LAYER_FRAME_SETTING ? this.getScreenWidth() :
                        layer == LAYER_FONT_SETTING ? this.getScreenWidth() :
                                layer == WIDGET_ALBUM_PHOTO ? (int) (0.70 * this.getScreenHeight()) :
                                        layer == WIDGET_ALBUM_SETTING ? this.getScreenWidth() / 4 :
                                                layer == WIDGET_PHOTO_SETTING ? this.getScreenWidth() / 2 : 0;
    }

    public int getImageGap() {
        return ImageView.ScaleType.FIT_CENTER != getPhotoScale() ? 0 :
                layer == LAYER_ALBUM_PHOTO ? 16 :
                        layer == LAYER_FRAME_SETTING ? 8 :
                                layer == LAYER_FONT_SETTING ? 8 :
                                        layer == WIDGET_ALBUM_PHOTO ? 12 :
                                                layer == WIDGET_ALBUM_SETTING ? 2 :
                                                        layer == WIDGET_PHOTO_SETTING ? 4 : 0;
    }

    public int getImageDensitySize() {
        return layer == LAYER_ALBUM_PHOTO ? 1 :
                layer == LAYER_FRAME_SETTING ? 4 :
                        layer == LAYER_FONT_SETTING ? 4 :
                                layer == WIDGET_ALBUM_PHOTO ? 12 :
                                        layer == WIDGET_ALBUM_SETTING ? 0 :
                                                layer == WIDGET_PHOTO_SETTING ? 8 : 0;
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

    public void setScaleIndex(int scaleIndex) {
        photo.setScaleIndex(scaleIndex);
    }

    public int getScaleIndex() {
        return photo.getScaleIndex();
    }

    public int getFrameIndex() {
        return photo.getFrameIndex();
    }

    public int getFontLocation() {
        return photo.getFontLocation();
    }

    public int getFontType() {
        return photo.getFontType();
    }

    public String getFontText() {
        return photo.getFontText();
    }

    public int getFontColor() {
        return photo.getFontColor();
    }

    public int getFontSize() {
        return photo.getFontSize();
    }
}
