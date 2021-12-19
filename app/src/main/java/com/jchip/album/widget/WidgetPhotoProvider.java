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
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            this.updateAppWidget(context, appWidgetId);
        }
    }

    @Override
    public void nextAppWidget(Context context, Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        WidgetData widgetData = (WidgetData) intent.getSerializableExtra(WIDGET_ITEM);
        if (widgetData != null && widgetData.isSaved()) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_album);
            new WidgetPhotoView(remoteViews, widgetData.getPhoto()).updatePhotoScale();
            AppWidgetManager.getInstance(context).partiallyUpdateAppWidget(appWidgetId, remoteViews);
        }
    }

    protected void updateAppWidget(Context context, int appWidgetId) {
        WidgetData widgetData = DataHelper.getInstance(context).queryWidgetPhoto(appWidgetId, -1);
        if (widgetData != null && widgetData.isSaved()) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_album);
            new WidgetPhotoView(remoteViews, widgetData.getPhoto()).updateView();
            remoteViews.setOnClickPendingIntent(R.id.widget_album_view, this.getPendingIntent(context, appWidgetId, widgetData));
            AppWidgetManager.getInstance(context).partiallyUpdateAppWidget(appWidgetId, remoteViews);
        }
    }
}