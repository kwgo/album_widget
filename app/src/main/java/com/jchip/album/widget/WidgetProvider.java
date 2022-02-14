package com.jchip.album.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.jchip.album.ActivitySlideshow;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.WidgetData;

import java.util.HashMap;
import java.util.Map;

public class WidgetProvider extends AppWidgetProvider {
    public static final String ACTION_APP = "actionApp";
    public static final String ACTION_NEXT = "actionNext";
    public static final String WIDGET_ITEM = "widgetItem";

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            if (ACTION_APP.equals(intent.getAction())) {
                this.startAppActivity(context, intent);
            } else if (ACTION_NEXT.equals(intent.getAction())) {
                this.onItemTap(-1, true,
                        () -> this.onNextAppWidget(context, intent),
                        () -> this.onJumpAppWidget(context, intent)
                );
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
        try {
            for (int appWidgetId : appWidgetIds) {
                this.onUpdateAppWidget(context, appWidgetId, -1);
            }
        } catch (Exception ex) {
            Log.e("widget", "widget update action error:", ex);
        }
    }

    protected void onUpdateAppWidget(Context context, int appWidgetId, int photoId) {
    }

    protected void onNextAppWidget(Context context, Intent intent) {
    }

    protected void onJumpAppWidget(Context context, Intent intent) {
        this.startAppActivity(context, intent);
    }

    protected PendingIntent getPendingIntent(Context context, int appWidgetId, WidgetData widgetData) {
        Intent intent = new Intent(context, this.getClass());
        intent.setAction(ACTION_NEXT);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra(WIDGET_ITEM, widgetData.getCopy());
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

    public void updateWidgetStatus(Context context, WidgetData widgetData, int status) {
        widgetData.setStatus(status);
        DataHelper.getInstance(context).updateWidget(widgetData);
    }

    private void startAppActivity(Context context, Intent intent) {
        Intent activityIntent = new Intent(context, ActivitySlideshow.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activityIntent.putExtra(WidgetProvider.WIDGET_ITEM, intent.getSerializableExtra(WidgetProvider.WIDGET_ITEM));
        context.startActivity(activityIntent);
    }

    private static final long DOUBLE_CLICK_PERIOD = 250;
    private static Map<Integer, Integer> itemTapCounts;

    private void onItemTap(int itemId, boolean doubleTap, Runnable onSingleTap, Runnable onDoubleTap) {
        if (doubleTap) {
            if (itemTapCounts == null) {
                itemTapCounts = new HashMap<>();
            }
            int itemTapCount = itemTapCounts.get(itemId) == null ? 0 : itemTapCounts.get(itemId);
            itemTapCounts.put(itemId, ++itemTapCount);

            final Handler handler = new Handler(Looper.getMainLooper()) {
                public void handleMessage(Message message) {
                    int itemTapCount = itemTapCounts.get(itemId) == null ? 0 : itemTapCounts.get(itemId);
                    if (itemTapCount > 1) {
                        onDoubleTap.run();
                    } else {
                        onSingleTap.run();
                    }
                    itemTapCounts.put(itemId, 0);
                }
            };

            if (itemTapCount == 1) {
                new Thread(() -> {
                    try {
                        synchronized (this) {
                            wait(DOUBLE_CLICK_PERIOD);
                        }
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException ignore) {
                    }
                }).start();
            }
        } else {
            onSingleTap.run();
        }
    }
}