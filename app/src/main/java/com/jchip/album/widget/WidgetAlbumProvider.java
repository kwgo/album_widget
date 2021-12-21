package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jchip.album.R;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.WidgetData;

import java.util.ArrayList;
import java.util.List;

public class WidgetAlbumProvider extends WidgetProvider {

    @Override
    public void onUpdateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            this.updateAppWidget(context, appWidgetId, -1);
        }
    }

    @Override
    public void onNextAppWidget(Context context, Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        WidgetData widgetData = (WidgetData) intent.getSerializableExtra(WIDGET_ITEM);
        if (widgetData != null) {
            updateAppWidget(context, appWidgetId, widgetData.getPhotoId());
        }
    }

    protected void updateAppWidget(Context context, int appWidgetId, int photoId) {
        WidgetData widgetData = DataHelper.getInstance(context).queryWidgetPhoto(appWidgetId, photoId);
        if (widgetData != null) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_photo);
            new WidgetPhotoView(remoteViews, widgetData.getPhoto()).updateView();

            widgetData.setPhotoId(this.getNextPhotoId(widgetData));

            remoteViews.setOnClickPendingIntent(R.id.widget_view, this.getPendingIntent(context, appWidgetId, widgetData));
            AppWidgetManager.getInstance(context).partiallyUpdateAppWidget(appWidgetId, remoteViews);
        }
    }

    private int getNextPhotoId(WidgetData widgetData) {
        PhotoData photoData = widgetData.getPhoto();
        if (photoData.isSaved()) {
            if (widgetData.getPhotoIds() != null && !widgetData.getPhotoIds().trim().isEmpty()) {
                List<Integer> photoIds = new ArrayList<>();
                for (String itemId : widgetData.getPhotoIds().split(",")) {
                    photoIds.add(Integer.parseInt(itemId));
                }
                int index = photoIds.indexOf(photoData.getPhotoId());
                return index < 0 ? -1 : photoIds.get((index + 1) % photoIds.size());
            }
        }
        return -1;
    }
}