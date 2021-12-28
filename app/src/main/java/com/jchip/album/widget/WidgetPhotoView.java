package com.jchip.album.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import com.jchip.album.R;
import com.jchip.album.common.ImageHelper;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.data.PhotoData;

public class WidgetPhotoView {
    private Context context;
    private RemoteViews views;
    private PhotoData photo;

    public WidgetPhotoView(Context context, RemoteViews views, PhotoData photo) {
        this.context = context;
        this.views = views;
        this.photo = photo;
    }

    public void updateView() {
        try {
            this.setPhotoView();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updatePhotoScale() {
        this.photo.setScaleIndex((this.photo.getScaleIndex() + 1) % 4);
        this.setPhotoImage();
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
        this.setPhotoImage(imageIds[this.photo.getScaleIndex()]);
    }

    private void setPhotoImage(int photoImageId) {
        int FIT_CENTER = 1, FIT_PADDING = 15;
        int gap = photo.getScaleIndex() == FIT_CENTER ? PhotoHelper.dpToPx(FIT_PADDING) : 0;
        this.views.setViewPadding(photoImageId, gap, gap, gap, gap);
        Bitmap bitmap = null;
        if (photo.getPhotoPath() != null && !photo.getPhotoPath().trim().isEmpty()) {
            bitmap = PhotoHelper.loadBitmap(photo.getPhotoPath());
            if (bitmap != null) {
                int screenHeight = (int) (PhotoHelper.getScreenHeight() * 0.65);
                bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), screenHeight);
            }
        }
        if (bitmap != null) {
            this.views.setImageViewBitmap(photoImageId, bitmap);
        } else {
            this.views.setImageViewResource(photoImageId, R.drawable.photo_default);
        }
        this.views.setViewVisibility(photoImageId, View.VISIBLE);
    }

    private void setPhotoFrame() {
        this.setPhotoFrame(R.id.photo_container, R.id.photo_frame);
    }

    private void setPhotoFrame(int containerViewId, int frameViewId) {
        int frameId = this.photo.getFrameIndex() > 0 ? this.photo.getFrameIndex() : PhotoHelper.DEFAULT_FRAME_ID;
        this.views.setInt(containerViewId, "setBackgroundResource", frameId);
        this.views.setInt(frameViewId, "setBackgroundResource", frameId);
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
        int fontIndex = PhotoHelper.getFontIndex(photo.getFontType());
        int labelIndex = fontIndex * alignCount + fontIndex % alignCount;
        this.setLabelFont(labelIds[labelIndex]);
        this.setLabelLocation(R.id.label_container);
    }

    private void setLabelFont(int photoLabelId) {
        this.views.setViewVisibility(photoLabelId, View.INVISIBLE);
        if (photo.getFontText() != null && !photo.getFontText().trim().isEmpty()) {
            this.views.setTextViewText(photoLabelId, photo.getFontText());
            this.views.setTextColor(photoLabelId, photo.getFontColor());
            this.views.setTextViewTextSize(photoLabelId, TypedValue.COMPLEX_UNIT_PX, photo.getFontSize());
            this.views.setViewVisibility(photoLabelId, View.VISIBLE);
        }
    }

    private void setLabelLocation(int labelContainerId) {
        this.views.setInt(labelContainerId, "setGravity", photo.getFontLocation());
    }
}
