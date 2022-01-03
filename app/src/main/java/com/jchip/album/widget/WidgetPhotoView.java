package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import com.jchip.album.R;
import com.jchip.album.common.FontHelper;
import com.jchip.album.common.NinePatchHelper;
import com.jchip.album.data.PhotoData;
import com.jchip.album.view.PhotoView;
import com.jchip.album.view.PhotoViewConfig;

public class WidgetPhotoView {
    private final Context context;
    private final RemoteViews views;
    private final PhotoView photoView;

    public WidgetPhotoView(Context context, RemoteViews views, int appWidgetId, PhotoData photoData) {
        this.context = context;
        this.views = views;
        this.photoView = new PhotoView(context, photoData, PhotoViewConfig.WIDGET_ALBUM_PHOTO);
        this.photoView.setFrameRect(this.getWidgetRect(appWidgetId));

        Rect padding = new Rect();
        NinePatchHelper.getImagePadding(context.getResources(), this.photoView.getFrameIndex(), this.photoView.getDensityFactor(), padding);
        Log.d("", "photo widget drawable setPhotoPadding .. .. ..");
        this.photoView.setPhotoPadding(padding);
    }

    public void updateView() {
        try {
            this.setPhotoView();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int updateScale(int scaleIndex) {
        scaleIndex = scaleIndex < 0 ? (this.photoView.getScaleIndex() + 1) % 4 : scaleIndex;
        this.photoView.setPhotoImage(-1, -1, scaleIndex);
        this.updateView();
        return this.photoView.getScaleIndex();
    }

    private void setPhotoView() {
        this.setPhotoFrame();
        this.setPhotoBorder();
        this.setPhotoImage();
        this.setPhotoLabel();
    }


    private void setPhotoImage() {
        Bitmap bitmap = this.photoView.getPhotoImage();
        if (bitmap != null) {
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

    private void setPhotoBorder() {
        int gap = this.photoView.getImageGap();
        this.views.setViewVisibility(R.id.photo_board, View.VISIBLE);
        this.views.setViewPadding(R.id.photo_board, gap, gap, gap, gap);
    }

    private void setPhotoLabel() {
//        int imageId = R.drawable.photo_default;
//        Bitmap bitmap = FontHelper.drawMultilineTextToBitmap(context, imageId, "aaa\nbbb\nccc\nddddddd\nde", Layout.Alignment.ALIGN_OPPOSITE);
//        this.views.setImageViewBitmap(R.id.photo_label_image, bitmap);
        this.views.setViewVisibility(R.id.photo_label_image, this.photoView.isFontEmpty() ? View.GONE : View.VISIBLE);
        if (!this.photoView.isFontEmpty()) {
            String text = this.photoView.getFontText();
            Typeface font = this.photoView.getFontFaceType();
            int color = this.photoView.getFontColor();
            int textSize = this.photoView.getFontTextSize();
            Log.d("", " this.photoView.getFontLocation() = " + this.photoView.getFontLocation());
            int location = this.photoView.getLocationIndex() % 3;
            Layout.Alignment alignment = location == 0 ? Layout.Alignment.ALIGN_NORMAL :
                    location == 2 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER;
            Log.d("", " alignment = " + alignment);

            Bitmap bitmap = FontHelper.getTextBitmap(text, font, textSize, color, alignment);
            this.views.setImageViewBitmap(R.id.photo_label_image, bitmap);
        }

        //        int[] labelIds = {
//                R.id.photo_label_0, R.id.photo_label_1, R.id.photo_label_2,
//                R.id.photo_label_3, R.id.photo_label_4, R.id.photo_label_5,
//                R.id.photo_label_6, R.id.photo_label_7, R.id.photo_label_8,
//                R.id.photo_label_9, R.id.photo_label_10, R.id.photo_label_11,
//                R.id.photo_label_12, R.id.photo_label_13, R.id.photo_label_14,
//                R.id.photo_label_15, R.id.photo_label_16, R.id.photo_label_17
//        };
//        for (int labelId : labelIds) {
//            this.views.setViewVisibility(labelId, View.GONE);
//        }
//        int alignCount = 3;
//        int fontIndex = photoView.getFontIndex();
//        int labelIndex = fontIndex * alignCount + fontIndex % alignCount;
//        this.setLabelFont(labelIds[labelIndex]);
//        this.setLabelLocation();
    }

    private void setLabelFont(int photoLabelId) {
        if (!this.photoView.isFontEmpty()) {
            this.views.setTextViewText(photoLabelId, this.photoView.getFontText());
            this.views.setTextColor(photoLabelId, this.photoView.getFontColor());
            this.views.setTextViewTextSize(photoLabelId, TypedValue.COMPLEX_UNIT_PX, this.photoView.getFontTextSize());
            this.views.setViewVisibility(photoLabelId, View.VISIBLE);
        }
    }

    private void setLabelLocation() {
        this.views.setInt(R.id.label_container, "setGravity", this.photoView.getFontLocation());
    }

    private Rect getWidgetRect(int appWidgetId) {
        try {
            int orientation = context.getResources().getConfiguration().orientation;
            boolean isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT;
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
            int width = options.getInt(isPortrait ? AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH : AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
            int height = options.getInt(isPortrait ? AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT : AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            Log.d("", "widget size is width = " + PhotoViewConfig.dpToPx(width) + " height = " + PhotoViewConfig.dpToPx(height));
            return new Rect(0, 0, PhotoViewConfig.dpToPx(width), PhotoViewConfig.dpToPx(height));
        } catch (Exception ex) {
            return PhotoViewConfig.getImageRect(PhotoViewConfig.WIDGET_ALBUM_PHOTO);
        }
    }
}
