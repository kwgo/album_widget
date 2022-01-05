package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.jchip.album.common.NinePatchHelper;
import com.jchip.album.data.PhotoData;
import com.jchip.album.view.PhotoView;
import com.jchip.album.view.PhotoViewConfig;
import com.jchip.album.view.PhotoViewHelper;

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
            PhotoViewHelper.setPhotoView(this.photoView, this.views);
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


    private Rect getWidgetRect(int appWidgetId) {
        try {
            int orientation = context.getResources().getConfiguration().orientation;
            boolean isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT;
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
            int width = options.getInt(isPortrait ? AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH : AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
            int height = options.getInt(isPortrait ? AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT : AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            return new Rect(0, 0, PhotoViewConfig.dpToPx((int) (1.1f * width)), PhotoViewConfig.dpToPx((int) (1.1f * height)));
        } catch (Exception ex) {
            return PhotoViewConfig.getImageRect(PhotoViewConfig.WIDGET_ALBUM_PHOTO);
        }
    }
}
