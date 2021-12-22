package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.jchip.album.ActivityPhotoSetting;
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

    @Override
    public void onNextAppWidget(Context context, Intent intent) {
        Log.d("","onNextAppWidget ========================onNextAppWidget ==== ");
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        WidgetData widgetData = (WidgetData) intent.getSerializableExtra(WIDGET_ITEM);
        if (widgetData != null) {
            Log.d("","onNextAppWidget 1 ========================onNextAppWidget ==== " + widgetData.getPhoto());
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_photo);
            new WidgetPhotoView(remoteViews, widgetData.getPhoto()).updatePhotoScale();
            Log.d("","onNextAppWidget 1.5 ========================getScaleIndex ==== " + widgetData.getPhoto().getScaleIndex());
            Log.d("","onNextAppWidget 1.50 ========================getScaleIndex ==== " + widgetData.getPhoto());
            remoteViews.setOnClickPendingIntent(R.id.widget_view, this.getPendingIntent(context, appWidgetId, widgetData));
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
            Log.d("","onNextAppWidget 2 ========================onNextAppWidget ==== ");
        }
    }

    protected void updateAppWidget(Context context, int appWidgetId) {
        Log.d("","photo ========================updateAppWidget ==== " + appWidgetId);
        WidgetData widgetData = DataHelper.getInstance(context).queryWidgetPhoto(appWidgetId);
        Log.d("","photo ========================updateAppWidget ==== NOT NULL");
        if (widgetData != null) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_photo);
            Log.d("","photo 1 ========================updateView ==== done " + widgetData.getPhoto());
            new WidgetPhotoView(remoteViews, widgetData.getPhoto()).updateView();
            Log.d("","photo 2 ========================updateView ==== done " + widgetData.getPhoto());
            remoteViews.setOnClickPendingIntent(R.id.widget_view, this.getPendingIntent(context, appWidgetId, widgetData));
            Log.d("","photo ========================setOnClickPendingIntent ==== done");
            AppWidgetManager.getInstance(context).partiallyUpdateAppWidget(appWidgetId, remoteViews);
            Log.d("","photo ========================partiallyUpdateAppWidget ==== done");
        }
    }
}