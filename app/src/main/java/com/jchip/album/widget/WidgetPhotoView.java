package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
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
        this.views.setViewVisibility(R.id.photo_border, this.photoView.isBorderOn() ? View.VISIBLE : View.INVISIBLE);
        this.views.setViewVisibility(R.id.photo_border, true ? View.VISIBLE : View.INVISIBLE);
        if (this.photoView.isBorderOn()) {
            int gap = this.photoView.getImageBorder();
            this.views.setViewPadding(R.id.photo_border, gap, gap, gap, gap);
        }
    }

    private void setPhotoLabel() {
        this.views.setViewVisibility(R.id.photo_label, this.photoView.isFontEmpty() ? View.GONE : View.VISIBLE);
        if (!this.photoView.isFontEmpty()) {
            String text = this.photoView.getFontText();
            Typeface font = this.photoView.getFontFaceType();
            int color = this.photoView.getFontColor();
            int textSize = this.photoView.getFontTextSize();
            int location = this.photoView.getLocationIndex() % 3;
            Layout.Alignment alignment = location == 0 ? Layout.Alignment.ALIGN_NORMAL :
                    location == 2 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER;
            Bitmap bitmap = FontHelper.getTextBitmap(text, font, textSize, color, alignment);
            this.views.setImageViewBitmap(R.id.photo_label, bitmap);
            this.views.setInt(R.id.label_container, "setGravity", this.photoView.getFontLocation());
        }
    }

    private Rect getWidgetRect(int appWidgetId) {
        try {
            int orientation = context.getResources().getConfiguration().orientation;
            boolean isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT;
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
            int width = options.getInt(isPortrait ? AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH : AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
            int height = options.getInt(isPortrait ? AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT : AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            return new Rect(0, 0, PhotoViewConfig.dpToPx(width), PhotoViewConfig.dpToPx(height));
        } catch (Exception ex) {
            return PhotoViewConfig.getImageRect(PhotoViewConfig.WIDGET_ALBUM_PHOTO);
        }
    }
}
