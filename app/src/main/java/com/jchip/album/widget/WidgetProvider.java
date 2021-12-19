package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jchip.album.ActivitySplash;

public class WidgetProvider extends AppWidgetProvider {
    public static final String ACTION_APP = "actionApp";
    public static final String ACTION_NEXT = "actionNext";
    public static final String WIDGET_ITEM = "widgetItem";

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            Log.d("widget", "received widget action " + intent.getAction());
            if (ACTION_APP.equals(intent.getAction())) {
                this.startAppActivity(context, intent);
            } else if (ACTION_NEXT.equals(intent.getAction())) {
                this.nextAppWidget(context, intent);
            } else {
                super.onReceive(context, intent);
            }
        } catch (Exception ex) {
            Log.e("widget", "widget action " + intent.getAction() + " error:", ex);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
//        this.onUpdate(context, AppWidgetManager.getInstance(context), new int[]{appWidgetId});
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("", "onUpdate ------ ------ ------ ------");

    }

    public void nextAppWidget(Context context, Intent intent) {
        Log.d("", "nextAppWidget ------ ------ ------ ------");
//        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//        this.onUpdate(context, AppWidgetManager.getInstance(context), new int[]{appWidgetId});
    }

    private void startAppActivity(Context context, Intent intent) {
        Intent activityIntent = new Intent(context, ActivitySplash.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activityIntent.putExtra(WidgetProvider.WIDGET_ITEM, intent.getSerializableExtra(WidgetProvider.WIDGET_ITEM));
        context.startActivity(activityIntent);
    }
}