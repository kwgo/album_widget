package com.jchip.album.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView.ScaleType;

import androidx.core.content.res.ResourcesCompat;

import com.jchip.album.common.ImageHelper;
import com.jchip.album.common.NinePatchHelper;
import com.jchip.album.data.PhotoData;

public class PhotoView {
    private final int layer;
    private final Context context;
    private final PhotoData photo;
    private Rect frameRect;

    public PhotoView(Context context, int layer) {
        this(context, new PhotoData(), layer);
    }

    public PhotoView(Context context, PhotoData photo, int layer) {
        this.layer = layer;
        this.context = context;
        this.photo = photo;
        this.frameRect = new Rect();
    }

    public Drawable getFrameDrawable() {
        Log.d("", " this.getLayer()===" + this.layer);
        Log.d("", " this.getDensity()===" + PhotoViewConfig.getDensity());
        Log.d("", " this.getImageGap()===" + this.getImageGap());
        Log.d("", " this.getImageDensitySize()===" + this.getFrameDensitySize());
        Log.d("", " this.getImageWidth()===" + PhotoViewConfig.getImageWidth(layer));
        Log.d("", " this.getImageHeight()===" + PhotoViewConfig.getImageHeight(layer));
        Log.d("", " this.getScreenWidth()===" + PhotoViewConfig.getScreenWidth());
        Log.d("", " this.getScreenHeight()===" + PhotoViewConfig.getScreenHeight());
        Log.d("", " this.getFrameRect()===" + this.frameRect);
        Log.d("", " ---------");

        // x 40 to change density
        int densitySize = this.getFrameDensitySize();
        Rect padding = new Rect();
        Drawable drawable = NinePatchHelper.getImageDrawable(context.getResources(), this.getFrameIndex(), densitySize, padding);
        this.frameRect.set(padding.left + padding.right, padding.top + padding.bottom, this.frameRect.right, this.frameRect.bottom);
        Log.d("", "after nine this.getFrame()===" + this.frameRect);
        return drawable;
    }

    public Bitmap getPhotoImage() {
        Bitmap bitmap = null;
        if (photo.getPhotoPath() != null && !photo.getPhotoPath().trim().isEmpty()) {
            bitmap = this.decodePhotoImage(photo.getPhotoPath().trim());
        }
        if (bitmap == null) {
            //bitmap = ImageHelper.loadBitmap(context.getResources(), PhotoViewConfig.DEFAULT_PHOTO_ID, false);
            bitmap = this.decodePhotoImage(PhotoViewConfig.DEFAULT_PHOTO_ID);
            if (PhotoViewConfig.isImageRotation(this.layer)) {
                return this.convertPhotoImage(bitmap);
            }
        } else {
            return this.convertPhotoImage(bitmap);
        }
        return bitmap;
    }

    private Bitmap decodePhotoImage(String imageUrl) {
        int width = frameRect.right > 0 ? frameRect.right : PhotoViewConfig.getScreenWidth() - frameRect.left;
        int height = frameRect.bottom > 0 ? frameRect.bottom : PhotoViewConfig.getScreenHeight() - frameRect.top;
        return ImageHelper.decodeBitmap(context.getResources(), imageUrl, photo.getPhotoWidth(), photo.getPhotoHeight(), width, height);
    }

    private Bitmap decodePhotoImage(int imageId) {
        int width = frameRect.right > 0 ? frameRect.right : PhotoViewConfig.getScreenWidth() - frameRect.left;
        int height = frameRect.bottom > 0 ? frameRect.bottom : PhotoViewConfig.getScreenHeight() - frameRect.top;
        return ImageHelper.decodeBitmap(context.getResources(), imageId, photo.getPhotoWidth(), photo.getPhotoHeight(), width, height);
    }

    private Bitmap convertPhotoImage(Bitmap bitmap) {
        Rect viewRect = PhotoViewConfig.pxToDp(this.frameRect);
        if (viewRect.right <= 0 || viewRect.bottom <= 0) {
            viewRect = PhotoViewConfig.pxToDp(PhotoViewConfig.getImageRect(this.layer));
        }
//        Rect viewRect = new Rect(this.frameRect);
//        if (viewRect.right <= 0 || viewRect.bottom <= 0) {
//            viewRect = new Rect(PhotoViewConfig.getImageRect(this.layer));
//        }
        return ImageHelper.convertBitmap(bitmap, 1f, photo.getScaleIndex(), photo.getRotationIndex(), photo.getFlipIndex(), viewRect.right, viewRect.bottom);
    }

    public void setFrameRect(Rect frameRect) {
        this.frameRect = frameRect;
        Log.d("", "set this.frameRectRect() object ===" + this.frameRect.hashCode());
        Log.d("", "set this.frameRectRect()===" + this.frameRect);
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
            return ResourcesCompat.getFont(context, PhotoViewConfig.fonts.get(this.getFontIndex()));
        } catch (Exception ignored) {
        }
        return null;
    }

    public int getFlipIndex() {
        return photo.getFlipIndex() == 0 ? 0 : 1;
    }

    public int getRotationIndex() {
        return photo.getRotationIndex() >= 0 && photo.getRotationIndex() < (360 / 90) ? photo.getRotationIndex() : 0;
    }

    public int getScaleIndex() {
        return photo.getScaleIndex() >= 0 && photo.getScaleIndex() < PhotoViewConfig.scaleTypes.size() ? photo.getScaleIndex() : 0;
    }

    public ScaleType getPhotoScale() {
        return PhotoViewConfig.scaleTypes.get(this.getScaleIndex());
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
        return fontIndex >= 0 ? photo.getFontType() : PhotoViewConfig.fonts.get(PhotoViewConfig.DEFAULT_FONT_INDEX);
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
        this.photo.setFontType(type != -1 ? type : this.photo.getFontType());
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

    public PhotoData getPhotoData() {
        return this.photo;
    }
}