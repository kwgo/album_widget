package com.jchip.album.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RemoteViews;

import com.jchip.album.R;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.common.ImageHelper;
import com.jchip.album.data.PhotoData;

public class WidgetPhotoView {

    private Context context;
    private PhotoData photoData;
    private RemoteViews views;

    public WidgetPhotoView(Context context, RemoteViews views, PhotoData photoData) {
        this.context = context;
        this.views = views;
        this.photoData = photoData;
    }

    public void updateView() {
        try {
            this.setPhotoView();
        } catch (Exception ex) {
        }
    }

    public void setPhotoView() {
        this.setScalePhoto(this.photoData);

        int labelId = this.getPhotoLabelId(this.photoData);
        this.setLabelLocation(R.id.label_container, labelId, this.photoData);
        this.setLabelFont(labelId, this.photoData);

        this.setPhotoFrame(R.id.photo_container, this.photoData);
        this.setPhotoFrame(R.id.photo_frame, this.photoData);
    }

    public void setScalePhoto(PhotoData photo) {
        this.views.setViewVisibility(R.id.photo_image_0, View.INVISIBLE);
        this.views.setViewVisibility(R.id.photo_image_1, View.INVISIBLE);
        this.views.setViewVisibility(R.id.photo_image_2, View.INVISIBLE);
        this.views.setViewVisibility(R.id.photo_image_3, View.INVISIBLE);

        int scaleIndex = photo.getScaleIndex();
        int photoViewId = scaleIndex == 3 ? R.id.photo_image_3 :
                scaleIndex == 2 ? R.id.photo_image_2 :
                        scaleIndex == 1 ? R.id.photo_image_1 : R.id.photo_image_0;
        this.setPhotoImage(photoViewId, photo);
    }

    public void setPhotoImage(int photoViewId, PhotoData photo) {
        Bitmap bitmap = null;
        if (photo.getPhotoPath() != null && !photo.getPhotoPath().trim().isEmpty()) {
            bitmap = AlbumHelper.loadBitmap(photo.getPhotoPath());
            if (bitmap != null) {
                bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), 0);
            }
        }
        this.views.setImageViewBitmap(photoViewId, bitmap);
        this.views.setViewVisibility(photoViewId, View.VISIBLE);
    }

    public void setLabelFont(int textViewId, PhotoData photo) {
        this.views.setTextViewText(textViewId, photo.getFontText());
        this.views.setTextColor(textViewId, photo.getFontColor());
        this.views.setTextViewTextSize(textViewId, TypedValue.COMPLEX_UNIT_PX, photo.getFontSize());
    }

    public int getPhotoLabelId(PhotoData photo) {
        int[] fontIds = {
                R.id.label_font_0, R.id.label_font_1, R.id.label_font_2,
                R.id.label_font_3, R.id.label_font_4, R.id.label_font_5
        };
        int[] alignIds = {
                R.id.photo_label_0, R.id.photo_label_1, R.id.photo_label_2,
                R.id.photo_label_3, R.id.photo_label_4, R.id.photo_label_5,
                R.id.photo_label_6, R.id.photo_label_7, R.id.photo_label_8,
                R.id.photo_label_9, R.id.photo_label_10, R.id.photo_label_11,
                R.id.photo_label_12, R.id.photo_label_13, R.id.photo_label_14,
                R.id.photo_label_15, R.id.photo_label_16, R.id.photo_label_17
        };
        int alignCount = 3;
        for (int fountId : fontIds) {
            this.views.setViewVisibility(fountId, fountId == photo.getFontType() ? View.VISIBLE : View.INVISIBLE);
        }
        int idIndex = photo.getFontType() * alignCount + photo.getFontLocation() % alignCount;
        for (int alignIndex = photo.getFontType() * alignCount; alignIndex < photo.getFontType() * alignCount + alignCount; alignIndex++) {
            this.views.setViewVisibility(alignIds[idIndex], alignIndex == idIndex ? View.VISIBLE : View.INVISIBLE);
        }
        return alignIds[idIndex];
    }

    public void setLabelLocation(int containerId, int labelId, PhotoData photo) {
        int[] gravity = {
                Gravity.START | Gravity.TOP, Gravity.CENTER_HORIZONTAL | Gravity.TOP, Gravity.END | Gravity.TOP,
                Gravity.START | Gravity.CENTER_VERTICAL, Gravity.CENTER, Gravity.END | Gravity.CENTER_VERTICAL,
                Gravity.START | Gravity.BOTTOM, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, Gravity.END | Gravity.BOTTOM
        };
        //this.views.setInt(labelId, "setGravity", gravity[photo.getFontLocation() % gravity.length]);
        this.views.setInt(containerId, "setGravity", gravity[photo.getFontLocation() % gravity.length]);
    }

    public void setPhotoFrame(int imageViewId, PhotoData photo) {
        int frameIndex = photo.getFrameIndex() > 0 ? photo.getFrameIndex() : R.drawable.frame_default;
        this.views.setInt(imageViewId, "setBackgroundResource", frameIndex);
    }
}
