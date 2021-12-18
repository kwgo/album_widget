package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.jchip.album.R;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.WidgetData;

public class WidgetAlbumProvider extends WidgetProvider {

//    @Override
//    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), this.getWidgetLayoutId());
//        float width = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
//        float height = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
//        boolean landscape = width / height >= 0.85f;
//        remoteViews.setViewVisibility(R.id.widget_image_landscape, landscape ? View.VISIBLE : View.GONE);
//        remoteViews.setViewVisibility(R.id.widget_image_portrait, landscape ? View.GONE : View.VISIBLE);
//        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, remoteViews);
//    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("", "onUpdate ++++++++++++++++++++++++++++++++++++++++++");
        for (int appWidgetId : appWidgetIds) {
            Log.d("", "onUpdate +++++++++++++++++++appWidgetId+++++++++++++++++++++++ " + appWidgetId);
            WidgetData widgetData = DataHelper.getInstance(context).queryWidgetAlbum(appWidgetId, -1);
            Log.d("", "updateAppWidget ++++++++++++++++++++++++++++++++++++++++++ widgetData=" + widgetData.getAlbumId());
            if (widgetData != null) {
                updateAppWidget(context, appWidgetId, widgetData);
            }
        }
    }

    protected void updateAppWidget(Context context, int appWidgetId, WidgetData widgetData) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_album);

        Log.d("", "updateAppWidget ++++++++++++++++++++++++++++++++++++++++++");

//        int sourceId = FallUtility.getSourceId(context, item, "drawable", "good");
//        if (this.isRotatedImage(item)) {
//            remoteViews.setImageViewBitmap(R.id.widget_image_landscape, FallUtility.rotateBitmap(context, sourceId, 90));
//            remoteViews.setImageViewResource(R.id.widget_image_portrait, sourceId);
//        } else {
//            remoteViews.setImageViewResource(R.id.widget_image_landscape, sourceId);
//            remoteViews.setImageViewBitmap(R.id.widget_image_portrait, FallUtility.rotateBitmap(context, sourceId, 90));
//        }
        Intent intent = new Intent(context, WidgetAlbumProvider.class);
       // this.updateWidgetAction(context, remoteViews, intent, appWidgetId, item);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, remoteViews);
    }


    protected void updateWidgetAction(Context context, RemoteViews remoteViews, Intent intent, int appWidgetId, String item) {
//        intent.setAction(FallWidgetView.ACTION_APP);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//        intent.putExtra(FallWidgetView.WIDGET_ITEM, item);
//        intent.putExtra(FallWidgetView.WIDGET_TEXT, FallUtility.getSourceText(context, item, "string", "short"));
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.widget_image_landscape, pendingIntent);
//        remoteViews.setOnClickPendingIntent(R.id.widget_image_portrait, pendingIntent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
        for (int appWidgetId : appWidgetIds) {
            WidgetData widgetData = new WidgetData();
            widgetData.setWidgetId(appWidgetId);
            DataHelper.getInstance(context).deleteWidget(widgetData);
        }
        prefs.commit();
    }
}