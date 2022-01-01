package com.jchip.album.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import com.jchip.album.R;
import com.jchip.album.data.PhotoData;
import com.jchip.album.view.PhotoView;
import com.jchip.album.view.PhotoViewConfig;

public class WidgetPhotoView {
    private final Context context;
    private final RemoteViews views;
    private final PhotoView photoView;

    public WidgetPhotoView(Context context, RemoteViews views, PhotoData photoData) {
        this.context = context;
        this.views = views;
        this.photoView = new PhotoView(context, photoData, PhotoViewConfig.WIDGET_ALBUM_PHOTO);
    }

    public void updateView() {
        try {
            this.setPhotoView();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updatePhotoScale() {
        this.photoView.setPhotoImage(-1, -1, (this.photoView.getScaleIndex() + 1) % 4);
        this.setPhotoImage();
    }

    private void setPhotoView() {
        this.setPhotoFrame();
        this.setPhotoImage();
        this.setPhotoLabel();
    }

    private void setPhotoImage() {
        int gap = this.photoView.getImageGap();
        this.views.setViewPadding(R.id.photo_board, gap, gap, gap, gap);

        Log.d("", "widget use ......");
        Bitmap bitmap = this.photoView.getPhotoImage();
        if (bitmap != null) {
            Log.d("", "widget set image bitmap ......");
            this.views.setImageViewResource(R.id.photo_image, 0);
            this.views.setImageViewBitmap(R.id.photo_image, bitmap);
        } else {
            this.views.setImageViewBitmap(R.id.photo_image, null);
            this.views.setImageViewResource(R.id.photo_image, PhotoViewConfig.DEFAULT_PHOTO_ID);
        }
    }

    private void setPhotoFrame() {
        int frameId = this.photoView.getFrameIndex();
        this.views.setInt(R.id.photo_container, "setBackgroundResource", frameId);
        this.views.setInt(R.id.photo_frame, "setBackgroundResource", frameId);


    }

    private void setPhotoLabel() {
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
        int alignCount = 3;
        int fontIndex = photoView.getFontIndex();
        int labelIndex = fontIndex * alignCount + fontIndex % alignCount;
        this.setLabelFont(labelIds[labelIndex]);
        this.setLabelLocation();
    }

    private void setLabelFont(int photoLabelId) {
        if (this.photoView.isFontEmpty()) {
            this.views.setTextViewText(photoLabelId, this.photoView.getFontText());
            this.views.setTextColor(photoLabelId, this.photoView.getFontColor());
            this.views.setTextViewTextSize(photoLabelId, TypedValue.COMPLEX_UNIT_PX, this.photoView.getFontTextSize());
            this.views.setViewVisibility(photoLabelId, View.VISIBLE);
        }
    }

    private void setLabelLocation() {
        this.views.setInt(R.id.label_container, "setGravity", this.photoView.getFontLocation());
    }
}
