package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jchip.album.ActivitySplash;

public class WidgetProvider extends AppWidgetProvider {
    public static final String ACTION_APP = "actionApp";
    public static final String ACTION_NEXT = "actionNext";
    public static final String ACTION_TOAST = "actionToast";

    public static final String WIDGET_ITEM = "widgetItem";
    public static final String WIDGET_TEXT = "widgetText";

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            if (intent.getAction().equals(WidgetProvider.ACTION_TOAST)) {
                final String item = intent.getStringExtra(WidgetProvider.WIDGET_ITEM);
                Toast.makeText(context, "Widget toast:" + item, Toast.LENGTH_SHORT).show();
            } else if (WidgetProvider.ACTION_NEXT.equals(intent.getAction())) {
                int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                this.onUpdate(context, AppWidgetManager.getInstance(context), new int[]{appWidgetId});
            } else if (WidgetProvider.ACTION_APP.equals(intent.getAction())) {
                this.activeApp(context, intent, ActivitySplash.class);
            } else {
                super.onReceive(context, intent);
            }
        } catch (Exception ex) {
            Log.e("widget", "widget action " + intent.getAction() + " error:", ex);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        this.onUpdate(context, AppWidgetManager.getInstance(context), new int[]{appWidgetId});
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fall_widget);
//        //ComponentName componentName = new ComponentName(context, MainWidget.class);
//        //int[] widgetIds = appWidgetManager.getAppWidgetIds(componentName);
//        for (int appWidgetId : appWidgetIds) {
//            Log.d("", "parent widget is onUpdate............ widget id =" + appWidgetId);
//            Intent intent = new Intent(context, WidgetProvider.class);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//            new WidgetPhotoView(context, intent, views, appWidgetId).updateView();
//            //appWidgetManager.updateAppWidget(componentName, views);
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
    }

    private void activeApp(Context context, Intent intent, Class<?> clazz) {
        Intent activityIntent = new Intent(context, clazz);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activityIntent.putExtra(WidgetProvider.WIDGET_ITEM, intent.getStringExtra(WidgetProvider.WIDGET_ITEM));
        activityIntent.putExtra(WidgetProvider.WIDGET_TEXT, intent.getStringExtra(WidgetProvider.WIDGET_TEXT));
        context.startActivity(activityIntent);
    }
}