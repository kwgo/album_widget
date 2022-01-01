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
    public void onUpdateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            this.updateAppWidget(context, appWidgetId);
        }
    }

//    @Override
//    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_photo_layer);
//        float width = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
//        float height = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
//        boolean landscape = width / height >= 0.85f;
//        remoteViews.setViewVisibility(R.id.widget_image_landscape, landscape ? View.VISIBLE : View.GONE);
//        remoteViews.setViewVisibility(R.id.widget_image_portrait, landscape ? View.GONE : View.VISIBLE);
//        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, remoteViews);
//    }


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

    protected void updateAppWidget(Context context, int appWidgetId) {
        WidgetData widgetData = DataHelper.getInstance(context).queryWidgetPhoto(appWidgetId);
        if (widgetData != null) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_photo_layer);
            new WidgetPhotoView(context, remoteViews, appWidgetId, widgetData.getPhoto()).updateView();
            remoteViews.setOnClickPendingIntent(R.id.widget_view, this.getPendingIntent(context, appWidgetId, widgetData));
            AppWidgetManager.getInstance(context).partiallyUpdateAppWidget(appWidgetId, remoteViews);
        }
    }
}