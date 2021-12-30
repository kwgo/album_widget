package com.jchip.album.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView.ScaleType;

import androidx.core.content.res.ResourcesCompat;

import com.jchip.album.common.ImageHelper;
import com.jchip.album.common.NinePatchHelper;
import com.jchip.album.data.PhotoData;

public class PhotoView {
    private final Context context;
    private final PhotoData photo;
    private final int layer;

    public PhotoView(Context context, int layer) {
        this(context, new PhotoData(), layer);
    }

    public PhotoView(Context context, PhotoData photo, int layer) {
        this.context = context;
        this.photo = photo;
        this.layer = layer;
    }

    public PhotoData getPhotoData() {
        return this.photo;
    }

    public Drawable getFrameDrawable() {
        Log.d("", " this.getLayer()===" + this.layer);
        Log.d("", " this.getDensity()===" + PhotoViewConfig.getDensity());
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
        return PhotoViewConfig.scaleTypes[photo.getScaleIndex()];
    }

    public Bitmap getPhotoImage() {
        return this.loadPhotoImage();
    }

    private Bitmap loadPhotoImage() {
        Bitmap bitmap = null;
        if (photo.getPhotoPath() != null && !photo.getPhotoPath().trim().isEmpty()) {
            bitmap = ImageHelper.decodeBitmap(photo.getPhotoPath(), photo.getPhotoWidth(), photo.getPhotoHeight(), this.getImageMaxWidth(), this.getImageMaxHeight());
        }
        if (bitmap == null) {
            bitmap = ImageHelper.loadBitmap(context.getResources(), PhotoViewConfig.DEFAULT_PHOTO_ID, false);
            if (this.getDefaultImageRotation()) {
                bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), 0, 0);
            }
        } else {
            bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), 0, 0);
        }
        return bitmap;
    }

    public boolean isSaved() {
        return this.photo.isSaved();
    }

    public int getAlbumId() {
        return this.photo.getAlbumId();
    }

    public int getPhotoId() {
        return this.photo.getPhotoId();
    }

    public boolean isFontEmpty() {
        return this.getFontText() != null && !this.getFontText().trim().isEmpty();
    }

    public boolean isImageOn() {
        return layer != PhotoViewConfig.LAYER_FRAME_SETTING;
    }

    public boolean isFrameOn() {
        return true;
    }

    public boolean isFontOn() {
        return true;
    }

    public int getFontIndex() {
        int fontIndex = PhotoViewConfig.fonts.indexOf(photo.getFontType());
        return fontIndex < 0 ? PhotoViewConfig.DEFAULT_FONT_INDEX : fontIndex;
    }

    public Typeface getFontFaceType() {
        try {
            int fontIndex = this.getFontIndex();
            return ResourcesCompat.getFont(context, PhotoViewConfig.fonts.get(fontIndex));
        } catch (Exception ignored) {
        }
        return null;
    }


    public int getFlipIndex() {
        return photo.getFlipIndex() >= 0 ? photo.getFlipIndex() : 0;
    }

    public int getRotationIndex() {
        return photo.getRotationIndex() >= 0 ? photo.getRotationIndex() : 0;
    }

    public int getScaleIndex() {
        return photo.getScaleIndex() >= 0 ? photo.getScaleIndex() : 0;
    }

    public int getFrameIndex() {
        return photo.getFrameIndex() > 0 ? photo.getFrameIndex() : PhotoViewConfig.DEFAULT_FRAME_ID;
    }

    public int getLocationIndex() {
        int locationIndex = PhotoViewConfig.locations.indexOf(photo.getFontLocation());
        return locationIndex < 0 ? PhotoViewConfig.DEFAULT_FONT_LOCATION : locationIndex;
    }

    public int getFontLocation() {
        int locationIndex = this.getLocationIndex();
        return PhotoViewConfig.locations.get(locationIndex);
    }

    public int getFontType() {
        int fontIndex = PhotoViewConfig.fonts.indexOf(photo.getFontType());
        return fontIndex >= 0 ? fontIndex : PhotoViewConfig.fonts.get(PhotoViewConfig.DEFAULT_FONT_INDEX);
    }

    public String getFontText() {
        return photo.getFontText();
    }

    public int getFontColor() {
        return photo.getFontColor();
    }

    public int getFontSize() {
        return photo.getFontSize() > 0 ? photo.getFontSize() : PhotoViewConfig.DEFAULT_FONT_SIZE;
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

    public boolean getDefaultImageRotation() {
        return PhotoViewConfig.getDefaultImageRotation(layer);
    }

    public void setPhotoInfo(int albumId, String path, int width, int height) {
        this.photo.setAlbumId(albumId >= 0 ? albumId : this.photo.getAlbumId());
        this.photo.setPhotoWidth(width > 0 ? width : this.photo.getPhotoWidth());
        this.photo.setPhotoHeight(height > 0 ? height : this.photo.getPhotoHeight());
        this.photo.setPhotoPath(path != null ? path : this.photo.getPhotoPath());
    }

    public void setPhotoFrame(int frameIndex) {
        this.photo.setFrameIndex(frameIndex);
    }

    public void setPhotoImage(int flip, int rotation, int scale) {
        this.photo.setFlipIndex(flip >= 0 ? flip : this.photo.getFlipIndex());
        this.photo.setRotationIndex(rotation >= 0 ? rotation : this.photo.getRotationIndex());
        this.photo.setScaleIndex(scale >= 0 ? scale : this.photo.getScaleIndex());
    }

    public void setPhotoFont(int type, int size, int color, int location, String text) {
        this.photo.setFontType(type >= 0 ? type : this.photo.getFontType());
        this.photo.setFontSize(size >= 0 ? size : this.photo.getFontSize());
        this.photo.setFontColor(color != -1 ? color : this.photo.getFontColor());
        this.photo.setFontLocation(location >= 0 ? location : this.photo.getFontLocation());

        this.photo.setFontText(text != null ? text : this.photo.getFontText());
    }

    public void setPhotoView(PhotoView photoView) {
        this.photo.setFrameIndex(photoView.getFrameIndex());

        this.photo.setFontType(photoView.getFontType());
        this.photo.setFontSize(photoView.getFontSize());
        this.photo.setFontColor(photoView.getFontColor());
        this.photo.setFontLocation(photoView.getFontLocation());
        this.photo.setFontText(photoView.getFontText());
    }

}