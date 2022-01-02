package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
            int scaleIndex = new WidgetPhotoView(context, remoteViews, appWidgetId, widgetData.getPhoto()).updateScale(-1);
            remoteViews.setOnClickPendingIntent(R.id.widget_view, this.getPendingIntent(context, appWidgetId, widgetData));
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
            Log.d("", "photo widget save appWidgetId = " + appWidgetId);
            Log.d("", "photo widget save scale type = " + scaleIndex);
            this.updateWidgetStatus(context, widgetData, scaleIndex);
        }
    }

    @Override
    protected void onUpdateAppWidget(Context context, int appWidgetId, int photoId) {
        WidgetData widgetData = DataHelper.getInstance(context).queryWidgetPhoto(appWidgetId);
        if (widgetData != null) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_photo_layer);
            int scaleIndex = widgetData.getStatus() >= 0 ? widgetData.getStatus() : widgetData.getPhoto().getScaleIndex();
            Log.d("", "photo widget load appWidgetId = " + appWidgetId);
            Log.d("", "photo widget load scale type = " + scaleIndex);
            Log.d("", "photo widget load db scale type = " +  widgetData.getStatus());
            new WidgetPhotoView(context, remoteViews, appWidgetId, widgetData.getPhoto()).updateScale(scaleIndex);
            remoteViews.setOnClickPendingIntent(R.id.widget_view, this.getPendingIntent(context, appWidgetId, widgetData));
            AppWidgetManager.getInstance(context).partiallyUpdateAppWidget(appWidgetId, remoteViews);
        }
    }
}