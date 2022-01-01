package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jchip.album.R;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.WidgetData;

public class WidgetPhotoProvider extends WidgetProvider {

    @Override
    public void onNextAppWidget(Context context, Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        WidgetData widgetData = (WidgetData) intent.getSerializableExtra(WIDGET_ITEM);
        if (widgetData != null) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_photo_layer);
            new WidgetPhotoView(context, remoteViews, appWidgetId, widgetData.getPhoto()).updatePhotoScale();
            remoteViews.setOnClickPendingIntent(R.id.widget_view, this.getPendingIntent(context, appWidgetId, widgetData));
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    protected void onUpdateAppWidget(Context context, int appWidgetId, int photoId) {
        WidgetData widgetData = DataHelper.getInstance(context).queryWidgetPhoto(appWidgetId);
        if (widgetData != null) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_photo_layer);
            new WidgetPhotoView(context, remoteViews, appWidgetId, widgetData.getPhoto()).updateView();
            remoteViews.setOnClickPendingIntent(R.id.widget_view, this.getPendingIntent(context, appWidgetId, widgetData));
            AppWidgetManager.getInstance(context).partiallyUpdateAppWidget(appWidgetId, remoteViews);
        }
    }
}