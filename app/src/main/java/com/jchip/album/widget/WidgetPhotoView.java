package com.jchip.album.widget;

import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RemoteViews;

import com.jchip.album.R;
import com.jchip.album.common.ImageHelper;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.data.PhotoData;

public class WidgetPhotoView {
    private RemoteViews views;
    private PhotoData photo;

    public WidgetPhotoView(RemoteViews views, PhotoData photo) {
        this.views = views;
        this.photo = photo;
    }

    public void updateView() {
        try {
            this.setPhotoView();
        } catch (Exception ex) {
        }
    }

    public void updatePhotoScale() {
        int[] imageIds = {
                R.id.photo_image_0, R.id.photo_image_1, R.id.photo_image_2, R.id.photo_image_3
        };
        for (int imageId : imageIds) {
            this.views.setImageViewBitmap(imageId, null);
            this.views.setViewVisibility(imageId, View.GONE);
        }
        this.photo.setScaleIndex((this.photo.getScaleIndex() + 1) % imageIds.length);
        int photoImageId = this.photo.getScaleIndex() == 3 ? R.id.photo_image_3 :
                this.photo.getScaleIndex() == 2 ? R.id.photo_image_2 :
                        this.photo.getScaleIndex() == 1 ? R.id.photo_image_1 : R.id.photo_image_0;
        this.setPhotoImage(photoImageId);
    }

    private void setPhotoView() {
        this.setPhotoFrame();
        this.setPhotoImage();
        this.setPhotoLabel();
    }

    private void setPhotoImage() {
        int[] imageIds = {
                R.id.photo_image_0, R.id.photo_image_1, R.id.photo_image_2, R.id.photo_image_3
        };
        for (int imageId : imageIds) {
            this.views.setImageViewResource(imageId, 0);
            this.views.setImageViewBitmap(imageId, null);
            this.views.setViewVisibility(imageId, View.GONE);
        }
        int photoImageId = this.photo.getScaleIndex() == 3 ? R.id.photo_image_3 :
                this.photo.getScaleIndex() == 2 ? R.id.photo_image_2 :
                        this.photo.getScaleIndex() == 1 ? R.id.photo_image_1 : R.id.photo_image_0;
        this.setPhotoImage(photoImageId);
    }

    private void setPhotoImage(int photoImageId) {
        int FIT_CENTER = 1, FIT_PADDING = 15;
        int gap = photo.getScaleIndex() == FIT_CENTER ? PhotoHelper.dpToPx(FIT_PADDING) : 0;
        this.views.setViewPadding(photoImageId, gap, gap, gap, gap);
        Bitmap bitmap = null;
        if (photo.getPhotoPath() != null && !photo.getPhotoPath().trim().isEmpty()) {
            bitmap = PhotoHelper.loadBitmap(photo.getPhotoPath());
            if (bitmap != null) {
                bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), 1200);
            }
        }
        if (bitmap != null) {
            this.views.setImageViewResource(photoImageId, 0);
            this.views.setImageViewBitmap(photoImageId, bitmap);
        } else {
            this.views.setImageViewBitmap(photoImageId, null);
            this.views.setImageViewResource(photoImageId, R.drawable.photo_default);
        }
        this.views.setViewVisibility(photoImageId, View.VISIBLE);
    }

    private void setPhotoFrame() {
        this.setPhotoFrame(R.id.photo_container);
        this.setPhotoFrame(R.id.photo_frame);
    }

    private void setPhotoFrame(int photoFrameId) {
        int frameIndex = this.photo.getFrameIndex() > 0 ? this.photo.getFrameIndex() : R.drawable.frame_default;
        this.views.setInt(photoFrameId, "setBackgroundResource", frameIndex);
    }

    private void setPhotoLabel() {
        int alignCount = 3;
        int[] labelIds = {
                R.id.photo_label_0, R.id.photo_label_1, R.id.photo_label_2,
                R.id.photo_label_3, R.id.photo_label_4, R.id.photo_label_5,
                R.id.photo_label_6, R.id.photo_label_7, R.id.photo_label_8,
                R.id.photo_label_9, R.id.photo_label_10, R.id.photo_label_11,
                R.id.photo_label_12, R.id.photo_label_13, R.id.photo_label_14,
                R.id.photo_label_15, R.id.photo_label_16, R.id.photo_label_17
        };
        for (int labelId : labelIds) {
            this.views.setViewVisibility(labelId, View.GONE);
        }
        int index = photo.getFontType() * alignCount + photo.getFontLocation() % alignCount;
        this.setLabelFont(labelIds[index]);
        this.setLabelLocation(R.id.label_container);
    }

    private void setLabelFont(int photoLabelId) {
        this.views.setTextViewText(photoLabelId, photo.getFontText());
        this.views.setTextColor(photoLabelId, photo.getFontColor());
        this.views.setTextViewTextSize(photoLabelId, TypedValue.COMPLEX_UNIT_PX, photo.getFontSize());
        this.views.setViewVisibility(photoLabelId, View.VISIBLE);
    }

    private void setLabelLocation(int labelContainerId) {
        int[] gravity = {
                Gravity.START | Gravity.TOP, Gravity.CENTER_HORIZONTAL | Gravity.TOP, Gravity.END | Gravity.TOP,
                Gravity.START | Gravity.CENTER_VERTICAL, Gravity.CENTER, Gravity.END | Gravity.CENTER_VERTICAL,
                Gravity.START | Gravity.BOTTOM, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, Gravity.END | Gravity.BOTTOM
        };
        this.views.setInt(labelContainerId, "setGravity", gravity[photo.getFontLocation() % gravity.length]);
    }
}
