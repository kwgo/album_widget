package com.jchip.album.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;

import com.jchip.album.R;
import com.jchip.album.data.PhotoData;
public class WidgetAlbumProvider extends WidgetPhotoProvider {

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
        for (int appWidgetId : appWidgetIds) {
            SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(context);
            String item = pres.getString(String.valueOf(appWidgetId), (String) null);
            if (item != null && !item.isEmpty()) {
                updateAppWidget(context, appWidgetId, item);
            }
        }
    }

    protected void updateAppWidget(Context context, int appWidgetId, String item) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), this.getWidgetLayoutId());
//        int sourceId = FallUtility.getSourceId(context, item, "drawable", "good");
//        if (this.isRotatedImage(item)) {
//            remoteViews.setImageViewBitmap(R.id.widget_image_landscape, FallUtility.rotateBitmap(context, sourceId, 90));
//            remoteViews.setImageViewResource(R.id.widget_image_portrait, sourceId);
//        } else {
//            remoteViews.setImageViewResource(R.id.widget_image_landscape, sourceId);
//            remoteViews.setImageViewBitmap(R.id.widget_image_portrait, FallUtility.rotateBitmap(context, sourceId, 90));
//        }
//        Intent intent = new Intent(context, WidgetAlbumProvider.class);
//        this.updateWidgetAction(context, remoteViews, intent, appWidgetId, item);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, remoteViews);
    }

    protected boolean isRotatedImage(String item) {
        return "NP".equalsIgnoreCase(item);
    }

    protected int getWidgetLayoutId() {
        return R.layout.widget_album;
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
            prefs.remove(String.valueOf(appWidgetId));
        }
        prefs.commit();
    }
}