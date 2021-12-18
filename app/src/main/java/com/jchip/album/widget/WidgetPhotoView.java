package com.jchip.album.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.widget.RemoteViews;

import java.util.Map;
import java.util.Random;

public class WidgetPhotoView {

    public static final String ACTION_APP = "actionApp";
    public static final String ACTION_SETTING = "actionSetting";
    public static final String ACTION_NEXT = "actionNext";
    public static final String ACTION_TOAST = "actionToast";

    public static final String WIDGET_ITEM = "widgetItem";
    public static final String WIDGET_TEXT = "widgetText";

    private Context context;
    private Intent intent;
    private RemoteViews views;
    private int appWidgetId;

    private static Random random;

    static {
        random = random != null ? random : new Random();
    }

    public WidgetPhotoView(Context context, Intent intent, RemoteViews views, int appWidgetId) {
        this.context = context;
        this.intent = intent;
        this.views = views;
        this.appWidgetId = appWidgetId;
    }

    public void updateView() {
//        Map<String, String[]> info = MainHelper.getISOInfo();
//        int index = this.random.nextInt(info.size());
//        String item = String.valueOf(info.keySet().toArray()[index]);
//        String[] iso = info.get(item);
//        this.setTextView(R.id.widget_title, FallUtility.getSourceText(this.context, item, "string", "short"));
//        this.setTextView(R.id.widget_marker, iso[FallHelper.OFFICIAL]);
//        this.setTextView(R.id.widget_symbol, iso[FallHelper.ALPHA_2] + " " + iso[FallHelper.CURRENCY]);
//        this.setTextView(R.id.widget_detail, FallUtility.getSourceText(this.context, item, "string", "capital"));
//        this.setTextView(R.id.widget_patch, ("(") + iso[FallHelper.POPULATION] + ")");
//
//        int orientation = context.getResources().getConfiguration().orientation;
//        boolean isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE;
//        this.setImageView(isLandscape ? R.id.widget_image_landscape : R.id.widget_image_portrait, FallUtility.getSourceId(this.context, item, "drawable", "flag"));
//        this.setVisibility(isLandscape ? R.id.widget_image_landscape : R.id.widget_image_portrait, true);
//        this.setVisibility(isLandscape ? R.id.widget_image_portrait : R.id.widget_image_landscape, false);
//
//        intent.putExtra(WIDGET_ITEM, item);
//        intent.putExtra(WIDGET_TEXT, FallUtility.getSourceText(this.context, item, "string", "short"));
//
//        this.setViewAction(R.id.widget_view, ACTION_NEXT);
//        this.setViewAction(isLandscape ? R.id.widget_image_landscape : R.id.widget_image_portrait, ACTION_APP);
    }

    public void setViewAction(int viewId, String action) {
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(viewId, pendingIntent);
    }

    public final void setTextView(int textViewId, String text) {
        this.views.setTextViewText(textViewId, text);
    }

    public final void setImageView(int imageViewId, int imageSourceId) {
        this.views.setImageViewResource(imageViewId, imageSourceId);
    }

    public void setVisibility(int viewId, boolean isOn) {
        this.views.setViewVisibility(viewId, isOn ? View.VISIBLE : View.GONE);
    }

}
