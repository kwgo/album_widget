package com.jchip.album.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.widget.ImageView.ScaleType;

import androidx.core.content.res.ResourcesCompat;

import com.jchip.album.common.FontHelper;
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

    public Drawable getFrameDrawable(Rect padding) {
        // x 40 to change density
        float densityFactor = this.getDensityFactor();
        return NinePatchHelper.getDrawable(context.getResources(), this.getFrameIndex(), densityFactor, padding);
    }

    public void setPhotoPadding(Rect padding) {
        Rect frameRect = this.getFrameRect();
        padding = padding != null ? padding : new Rect(0, 0, frameRect.right, frameRect.bottom);
        frameRect.left = frameRect.right - (int) (1f * padding.left / PhotoViewConfig.getFrameSizeFactor(this.layer));
        frameRect.top = frameRect.bottom - (int) (1f * padding.top / PhotoViewConfig.getFrameSizeFactor(this.layer));
        this.setFrameRect(frameRect);
    }

    private Rect getPhotoRect() {
        Rect photoRect = new Rect(this.getFrameRect());
        if (photoRect.left > 0 && photoRect.top > 0) {
            photoRect.right = photoRect.right - photoRect.left >= 0 ? photoRect.left : photoRect.right;
            photoRect.bottom = photoRect.bottom - photoRect.top >= 0 ? photoRect.top : photoRect.bottom;
        }
        photoRect.left = this.photo.getPhotoWidth();
        photoRect.top = this.photo.getPhotoHeight();
        return photoRect;
    }

    public Bitmap getPhotoImage() {
        Rect photoRect = this.getPhotoRect();
        Bitmap bitmap = null;
        if (photo.getPhotoPath() != null && !photo.getPhotoPath().trim().isEmpty()) {
            bitmap = ImageHelper.decodeBitmap(context.getResources(), photo.getPhotoPath(), photoRect);
        }
        if (bitmap == null) {
            bitmap = ImageHelper.decodeBitmap(context.getResources(), PhotoViewConfig.DEFAULT_PHOTO_ID, photoRect);
            if (PhotoViewConfig.isImageSolid(this.layer)) {
                return ImageHelper.convertBitmap(bitmap, 1f, 0, 0, 0, photoRect.right, photoRect.bottom);
            }
        }
        if (bitmap != null) {
            bitmap = ImageHelper.convertBitmap(bitmap, 1f, this.getScaleIndex(), this.getRotationIndex(), this.getFlipIndex(), photoRect.right, photoRect.bottom);
//            Log.d("", "convert view with width = " + photoRect.right + " height = " + photoRect.bottom);
//            Log.d("", "convert bitmap to width = " + bitmap.getWidth() + " height = " + bitmap.getHeight());
        }
        return bitmap;
    }

    public Bitmap getPhotoFont() {
        if (!this.isFontEmpty()) {
            String text = this.getFontText();
            Typeface font = this.getFontFaceType();
            int color = this.getFontColor();
            int textSize = this.getFontTextSize();
            int location = this.getLocationIndex() % 3;
            Layout.Alignment alignment = location == 0 ? Layout.Alignment.ALIGN_NORMAL :
                    location == 2 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER;
            Rect fontRect = new Rect(this.getFrameRect());
            return FontHelper.getTextBitmap(text, font, textSize, color, alignment, fontRect.right, fontRect.bottom);
        }
        return null;
    }

    public void setFrameRect(Rect frameRect) {
        if (frameRect != null) {
            this.frameRect = frameRect;
        }
    }

    public Rect getFrameRect() {
        if (this.frameRect.right <= 0 || this.frameRect.bottom <= 0) {
            Rect imageRect = PhotoViewConfig.getImageRect(this.layer);
            this.frameRect.right = imageRect.right;
            this.frameRect.bottom = imageRect.bottom;
        }
        return this.frameRect;
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
        return this.getFontText() == null || this.getFontText().trim().isEmpty();
    }

    public boolean isFullSize() {
        return PhotoViewConfig.fullSizeFrames.contains(this.getFrameIndex());
    }

    public boolean isImageOn() {
        return layer != PhotoViewConfig.LAYER_FRAME_SETTING;
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

    public float getDensityFactor() {
        int densitySize = PhotoViewConfig.getDensitySizeFactor(layer);
        int densityDisplay = context.getResources().getDisplayMetrics().densityDpi;
        return 1f * densityDisplay / densitySize;
    }

    public int getFontTextSize() {
        return (int) (PhotoViewConfig.getFontSizeFactor(layer) * this.getFontSize());
    }

    public int getImageBorder() {
        return ScaleType.FIT_CENTER != this.getPhotoScale() ? 0 : PhotoViewConfig.getImageBorder(layer);
    }

    //
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