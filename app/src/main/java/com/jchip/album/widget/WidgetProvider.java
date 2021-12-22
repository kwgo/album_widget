package com.jchip.album.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jchip.album.ActivitySplash;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.WidgetData;

public class WidgetProvider extends AppWidgetProvider {
    public static final String ACTION_APP = "actionApp";
    public static final String ACTION_NEXT = "actionNext";
    public static final String WIDGET_ITEM = "widgetItem";

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            // Log.d("widget", "received widget action " + intent.getAction());
            if (ACTION_APP.equals(intent.getAction())) {
                this.startAppActivity(context, intent);
            } else if (ACTION_NEXT.equals(intent.getAction())) {
                this.onNextAppWidget(context, intent);
            } else {
                super.onReceive(context, intent);
            }
        } catch (Exception ex) {
            Log.e("widget", "widget action " + intent.getAction() + " error:", ex);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        // this.onUpdate(context, AppWidgetManager.getInstance(context), new int[]{appWidgetId});
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        try {
            this.onUpdateAppWidget(context, appWidgetManager, appWidgetIds);
        } catch (Exception ex) {
            Log.e("widget", "widget update action error:", ex);
        }
    }

    public void onUpdateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("", "onUpdate ------ ------ ------ ------");
    }

    public void onNextAppWidget(Context context, Intent intent) {
        Log.d("", "nextAppWidget ------ ------ ------ ------");
//        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//        this.onUpdate(context, AppWidgetManager.getInstance(context), new int[]{appWidgetId});
    }

    protected PendingIntent getPendingIntent(Context context, int appWidgetId, WidgetData widgetData) {
        WidgetData widget = new WidgetData();
        widget.setWidgetId(widgetData.getWidgetId());
        widget.setAlbumId(widgetData.getAlbumId());
        widget.setPhotoId(widgetData.getPhotoId());
        widget.setPhotoIds(widgetData.getPhotoIds());
        widget.setPhoto(widgetData.getPhoto());

        PhotoData photo = widget.getPhoto();
        Log.d("", "photo ========================getPendingIntent ==== 0:   " + widgetData.getPhoto());
        Log.d("", "photo ========================getPendingIntent ===| 0:   " + photo);
        Intent intent = new Intent(context, this.getClass());
        Log.d("", "photo ========================getPendingIntent ==== 1:   " + widgetData.getPhoto());
        Log.d("", "photo ========================getPendingIntent ===| 1:   " + photo);
        intent.setAction(ACTION_NEXT);
        Log.d("", "photo ========================getPendingIntent ==== 2:   " + widgetData.getPhoto());
        Log.d("", "photo ========================getPendingIntent ===| 2:   " + photo);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        Log.d("", "photo ========================getPendingIntent ==== 3:   " + widgetData.getPhoto());
        Log.d("", "photo ========================getPendingIntent ===| 3:   " + photo);
        //widgetData.setPhoto(photo);
        intent.putExtra(WIDGET_ITEM, widget);
        //    intent.putExtra(WIDGET_ITEM, photo);
        Log.d("", "photo ========================getPendingIntent ==== 4:   " + widgetData.getPhoto());
        Log.d("", "photo ========================getPendingIntent ===| 4:   " + photo);
        return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            WidgetData widgetData = new WidgetData();
            widgetData.setWidgetId(appWidgetId);
            DataHelper.getInstance(context).deleteWidget(widgetData);
        }
    }

    private void startAppActivity(Context context, Intent intent) {
        Intent activityIntent = new Intent(context, ActivitySplash.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activityIntent.putExtra(WidgetProvider.WIDGET_ITEM, intent.getSerializableExtra(WidgetProvider.WIDGET_ITEM));
        context.startActivity(activityIntent);
    }
}