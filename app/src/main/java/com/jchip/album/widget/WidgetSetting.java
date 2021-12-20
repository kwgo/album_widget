package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;

import com.jchip.album.activity.AbstractActivity;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.WidgetData;

public class WidgetSetting extends AbstractActivity {

    protected int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    protected int resultValue = RESULT_CANCELED;

    @Override
    public void initContentView() {
        super.initContentView();

        this.appWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (this.appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    protected void notifyWidget(int value) {
        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, this.appWidgetId);
        setResult(value, intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (resultValue == RESULT_CANCELED) {
            notifyWidget(RESULT_CANCELED);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (resultValue == RESULT_CANCELED) {
            notifyWidget(RESULT_CANCELED);
        }
    }

    @Override
    protected void onDestroy() {
        if (resultValue == RESULT_CANCELED) {
            notifyWidget(RESULT_CANCELED);
        }
        super.onDestroy();
    }

    protected void updateWidget(Class provider) {
        notifyWidget(resultValue = RESULT_OK);

        Intent intent = new Intent(this, provider);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, this.appWidgetId);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{this.appWidgetId});
        this.sendBroadcast(intent);
    }

    protected void saveWidget(WidgetData widgetData) {
        Log.d("", "save widget id:" + widgetData.getWidgetId() + " album:" + widgetData.getAlbumId() + " photo:" + widgetData.getPhotoId());
        DataHelper.getInstance(this).saveWidget(widgetData);
    }

}