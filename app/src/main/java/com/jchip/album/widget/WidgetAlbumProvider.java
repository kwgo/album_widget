package com.jchip.album.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.jchip.album.R;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.WidgetData;

import java.util.ArrayList;
import java.util.List;

public class WidgetAlbumProvider extends WidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent();
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.putExtra(WIDGET_ITEM, new WidgetData());
            this.nextAppWidget(context, intent);
        }
    }

    @Override
    public void nextAppWidget(Context context, Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        WidgetData widgetData = (WidgetData) intent.getSerializableExtra(WIDGET_ITEM);
        widgetData = DataHelper.getInstance(context).queryWidgetPhoto(appWidgetId, widgetData.getPhotoId());
        if (widgetData != null && widgetData.isSaved()) {
            updateAppWidget(context, appWidgetId, widgetData);
        }
    }

    protected void updateAppWidget(Context context, int appWidgetId, WidgetData widgetData) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_album);
        new WidgetPhotoView(remoteViews, widgetData.getPhoto()).updateView();
        remoteViews.setOnClickPendingIntent(R.id.widget_album_view, this.getPendingIntent(context, appWidgetId, widgetData));
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, remoteViews);
    }

    protected PendingIntent getPendingIntent(Context context, int appWidgetId, WidgetData widgetData) {
        Log.d("", "updateWidgetAction photo ids ------" + widgetData.getPhotoIds());
        Log.d("", "updateWidgetAction current photo id ------" + widgetData.getPhoto().getPhotoId());
        widgetData.setPhotoId(this.getNextPhotoId(widgetData));
        Log.d("", "updateWidgetAction next photo id ------" + widgetData.getPhotoId());

        Intent intent = new Intent(context, getClass());
        intent.setAction(ACTION_NEXT);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra(WIDGET_ITEM, widgetData);
        return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            WidgetData widgetData = new WidgetData();
            widgetData.setWidgetId(appWidgetId);
            DataHelper.getInstance(context).deleteWidget(widgetData);
        }
    }
}